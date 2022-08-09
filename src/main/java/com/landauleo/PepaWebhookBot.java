package com.landauleo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.landauleo.handler.MessageHandler;
import com.landauleo.utils.MessageDictionary;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@ApplicationScoped
public class PepaWebhookBot extends TelegramWebhookBot {

    @ConfigProperty(name = "homebot.token")
    String token;

    @ConfigProperty(name = "homebot.url")
    String url;

    @ConfigProperty(name = "homebot.name")
    String name;

    @ConfigProperty(name = "homebot.webhook")
    String webHook;

    @Inject
    MessageHandler messageHandler;

    public SetWebhook setWebhook() throws TelegramApiException {
        return new SetWebhook(this.webHook);
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public String getBotPath() {
        return this.token; //TODO: я пока не поняла, как это работает и почему
    }

    public String getUrl() { //TODO: ещё один вопрос, почему через getter работает в Main, а иначе - нет
        return url;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        log.info("Got new update: {}", update);
        try {
            return messageHandler.handleMessage(update.getMessage());
        } catch (Exception e) {
            log.error("Error during receiving webhook update on update {}, cause:{}", update, e);
            return new SendMessage(update.getMessage().getChatId().toString(), MessageDictionary.UNEXPECTED_ERROR);
        }
    }

    public void sendMessageWithPriceUpdate(String chatId, String oldPrice, String newPrice) {
        String messageText = MessageDictionary.PRICE_CHANGED + oldPrice + " -> " + newPrice;
        SendMessage sendMessage = new SendMessage(chatId, messageText);
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Unable to send price update for chat with id:{}", chatId);
        }
    }

    public void sendMessageForFree() {
        SendMessage sendMessage = new SendMessage("123456789", "проверка связи");
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
