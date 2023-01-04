package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.service.ValidatorUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <i> Контроллер для получения или сохранения контактных данных пользователя в базу данных.</i>
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
public class BotUserController implements CommandController {
    private final Logger logger = LoggerFactory.getLogger(BotUserController.class);
    private final ValidatorUserService validatorUserService;
    private final String ADD_CONTACT = "/addContact";
    private final String GET_CONTACT = "/getContact";
    private static final String ADD_CONTACT_PATTERN = "([\\d]{11})(\\s)([\\W]+)";

    private final String ADD_MESSAGE = "Для того, чтобы оставить контактные данные для обратной " +
            "связи введите информацию в форме:\n 89871234567 Иван \n и мы вам перезвоним.";

    @Command(name = ADD_CONTACT)
    public SendMessage addMessage(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} запросил пример для записи контакта в БД", idUser);
        return new SendMessage(idUser, ADD_MESSAGE);
    }

    /**
     * <i>Метод для получения пользователем контактных данных, которые он записал в базу данных
     * <br>
     * Используется метод {@link ValidatorUserService#validateGetUser(Message)}</i>
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = GET_CONTACT)
    public SendMessage getContactMessage(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} запросил проверку своего контакта в БД", idUser);
        return new SendMessage(idUser, validatorUserService.validateGetUser(message));
    }

    /**
     * <i>Запись контактных данных пользователя
     * <br>
     * Используется метод {@link ValidatorUserService#validateUser(Message)}</i>
     * @param {@link Message}
     * @return {@link SendMessage}
     */
    @Command(pattern = ADD_CONTACT_PATTERN)
    public SendMessage addBotUser(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} производит запись контактных данных в БД", idUser);
        return new SendMessage(idUser, validatorUserService.validateUser(message));
    }
}
