package com.landauleo.handler;

import java.util.Locale;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.landauleo.service.RealtyService;
import com.landauleo.utils.CommandsDictionary;
import com.landauleo.utils.MessageDictionary;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.landauleo.validation.ValidationService.MIR_KVARTIR_URL;

@Slf4j
@ApplicationScoped
public class MessageHandler {

    @Inject
    RealtyService realtyService;

    public BotApiMethod<?> handleMessage(Message message) {
        String chatId = message.getChatId().toString();
        String messageText = message.getText().trim().toLowerCase(Locale.ROOT);
        String response = MessageDictionary.INVALID_MESSAGE;

        if (StringUtils.isBlank(messageText)) {
            response = MessageDictionary.BLANK_MESSAGE;
        } else if (messageText.equals(CommandsDictionary.START)) {
            response = MessageDictionary.START;
        } else if (messageText.contains(MIR_KVARTIR_URL)) {
            try {
                realtyService.saveNewRealty(messageText, chatId);
                response = MessageDictionary.REALTY_WAS_SAVED;
            } catch (Exception e) {
                log.error("Message from chat with id:{} username:{} was not handled properly, " +
                        "cause: {}", chatId, message.getFrom().getUserName(), e.getMessage());
                response = MessageDictionary.REALTY_WAS_NOT_SAVED +
                        "Если честно, то я облажалась с " + e.getMessage();
            }
        }

        return new SendMessage(chatId, response);
    }


}
