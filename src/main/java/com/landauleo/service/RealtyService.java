package com.landauleo.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.landauleo.PepaWebhookBot;
import com.landauleo.document.Realty;
import com.landauleo.validation.ValidationService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

@Slf4j
@ApplicationScoped
public class RealtyService {

    @Inject
    HtmlParser htmlParser;

    @Inject
    ValidationService validationService;

    @Inject
    MongoClient mongoClient;

    @Inject
    PepaWebhookBot bot;

    public void saveNewRealty(String url, String chatId) {
        validationService.validateUrl(url);
        String parsedPrice = htmlParser.parseAndGetPrice(url);
        validationService.validatePrice(parsedPrice);
        Realty realtyObject = new Realty();
        realtyObject.setPrice(new BigInteger(parsedPrice));
        realtyObject.setChatId(chatId);
        realtyObject.setUrl(url);
        add(realtyObject);
    }

    @Scheduled(every = "${homebot.check-every}")
    public void checkRealty() {
        list().forEach(item -> {
            try {
                String newPrice = htmlParser.parseAndGetPrice(item.getUrl());
                BigInteger oldPrice = item.getPrice();

                validationService.validatePrice(newPrice);

                if (!oldPrice.equals(new BigInteger(newPrice))) {
                    log.info("Price for realty with id:{} has changed {} -> {}", item.getId(), oldPrice, newPrice);

                    updatePrice(newPrice, item.getId());

                    bot.sendMessageWithPriceUpdate(item.getChatId(), oldPrice.toString(), newPrice);
                }
            } catch (Exception e) {
                log.error("Unable to update price for realty with id:{}, cause:{}", item.getId(), e);
            }
        });
    }

    //TODO
    public List<Realty> list() {
        List<Realty> list = new ArrayList<>();
        MongoCursor<Document> cursor = getCollection().find().iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Realty realty = new Realty();
                realty.setId(document.getObjectId("_id"));
                realty.setPrice(document.get("price", Decimal128.class).bigDecimalValue().toBigIntegerExact());
                realty.setChatId(document.getString("chatId"));
                realty.setIsPaused(document.getBoolean("isPaused"));
                realty.setIsClosed(document.getBoolean("isClosed"));
                realty.setUrl(document.getString("url"));
                realty.setCreatedAt(document.getDate("createdAt"));
                realty.setUpdatedAt(document.getDate("updatedAt"));
                list.add(realty);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public void add(Realty realty) {
        Decimal128 parsedPrice = Decimal128.parse(realty.getPrice().toString());

        Document document = new Document()
                .append("price", parsedPrice)
                .append("chatId", realty.getChatId())
                .append("isPaused", false)
                .append("isClosed", false)
                .append("url", realty.getUrl())
                .append("createdAt", LocalDateTime.now())
                .append("updatedAt", LocalDateTime.now());
        getCollection().insertOne(document);
    }

    public void updatePrice(String newPrice, ObjectId id) {
        Decimal128 parsedPrice = Decimal128.parse(newPrice);
        Document query = new Document().append("id", id);
        Bson updates = Updates.combine(Updates.set("price", parsedPrice),
                Updates.currentTimestamp("updatedAt"));
        UpdateOptions options = new UpdateOptions().upsert(false);
        getCollection().updateOne(query, updates, options);
        log.info("Update price result for realty with id: {} is: {}", id, newPrice);
    }

    private MongoCollection getCollection() { //TODO: завернуть в константы и вынести в класс-репо
        return mongoClient.getDatabase("pepa").getCollection("realty");
    }

}
