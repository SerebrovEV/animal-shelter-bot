package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.service.BotUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AddController implements CommandController {
    private final Logger logger = LoggerFactory.getLogger(AddController.class);
    private final BotUserService botUserService;
    private static final Pattern pattern = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");

    private final String ADD_MESSAGE = "Для того, чтобы оставить контактные данные для обратной " +
            "связи введите информацию в форме:\n 89871234567 Иван \n и мы вам перезвоним.";

    @Command(name = "/add_contact")
    public SendMessage addMessage(Message message) {
        return new SendMessage(message.from().id(), ADD_MESSAGE);
    }

    public SendMessage addBotUser(Message message) {
        BotUser botUser = handlerBotUser(message);
        botUserService.addBotUser(botUser);
        return new SendMessage(message.from().id(), "ADD_MESSAGE");
    }

    private BotUser handlerBotUser(Message message) {
        Matcher matcher = pattern.matcher(message.text());
        BotUser botUser = new BotUser();
        botUser.setUserName(matcher.group(3));
        botUser.setPhoneNumber(Long.getLong(matcher.group(1)));
        botUser.setUserId(message.from().id());
        return botUser;
    }
}
