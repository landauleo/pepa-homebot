package com.landauleo;

import javax.inject.Inject;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

@Slf4j
@QuarkusMain
public class Main implements QuarkusApplication {

    @Inject
    PepaWebhookBot bot;

    @Override
    public int run(String... args) {
        try {
            DefaultWebhook webhook = new DefaultWebhook();
            webhook.setInternalUrl(bot.getUrl());
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class, webhook);
            botsApi.registerBot(bot, bot.setWebhook());
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        Quarkus.waitForExit();
        return 0;
    }

}
