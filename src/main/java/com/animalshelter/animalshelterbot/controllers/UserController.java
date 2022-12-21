package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.service.BotUserService;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Контроллер для получения, обработки и сохранения контактных данных пользователя в базу данных.</i>
 * <br>
 * Запрос через {@link com.pengrad.telegrambot.model.Update} на добавление контакта осуществляется по команде
 * {@link #ADD_CONTACT}
 * <br>
 * Запрос через {@link com.pengrad.telegrambot.model.Update} на проверку записи осуществляется по команде
 * {@link #GET_CONTACT}
 * <br>
 */
@Component
@RequiredArgsConstructor
public class UserController implements CommandController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final BotUserService botUserService;
    private final String ADD_CONTACT = "/add_contact";
    private final String GET_CONTACT = "/get_contact";
    private static final Pattern pattern = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");

    private final String ADD_MESSAGE = "Для того, чтобы оставить контактные данные для обратной " +
            "связи введите информацию в форме:\n 89871234567 Иван \n и мы вам перезвоним.";

    @Command(name = ADD_CONTACT)
    public SendMessage addMessage(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} запросил пример для записи контакта", idUser);
        return new SendMessage(idUser, ADD_MESSAGE);
    }

    @Command(name = GET_CONTACT)
    public SendMessage getContactMessage(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} запросил проверку своего контакта", idUser);
        String text = getBotUser(idUser);
        return new SendMessage(message.from().id(), text);
    }

    public SendMessage addBotUser(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} произвел запись контактных данных в БД", idUser);
        BotUser botUser = handlerBotUser(message);
        String answer = "Добвлена запись контакта: " + botUser;
        return new SendMessage(idUser, answer);
    }

    private BotUser handlerBotUser(Message message) {
        Matcher matcher = pattern.matcher(message.text());
        matcher.find();
        String name = matcher.group(3);
        long phone = Long.parseLong(matcher.group(1));
        long idUser = message.from().id();
        BotUser botUser = new BotUser(name, phone, idUser);
        botUserService.addBotUser(botUser);
        return botUser;
    }

    private String getBotUser(long idUser) {
        return botUserService.getBotUser(idUser);

    }
}
