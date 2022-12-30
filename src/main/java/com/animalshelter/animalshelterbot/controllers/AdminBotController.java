package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.service.BotUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import liquibase.pro.packaged.P;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i>Контроллер для добавления, редактирования, проверки наличия и получения
 * всех {@link BotUser} в/из базы данных администратором. Обычный пользователь не имеет доступа к данным командам.
 * </i>
 */
@Component
@RequiredArgsConstructor
public class AdminBotController implements CommandController {
    private final BotUserService botUserService;
    private final Logger LOG = LoggerFactory.getLogger(AdminBotController.class);

    private final String ADMIN_COMMAND = "Правила использования: \n" +
            "Добавить 89871234567 Иван - добавление контакта усыновителя;\n" +
            "Найти 10 - поиск пользователя с id = 10;\n" +
            "Изменить 10 89871234567 Миша - изменение пользователя с id = 10;\n" +
            "Удалить 10 - удаление пользователя с id = 10;\n" +
            "/getAll - получить список всех усыновителей;\n" +
            "/badUser - получить список усыновителей, которые не прислали отчеты;\n" +
            "Отчет 10 - получить последний отчет от усыновителя id = 10.";

    private static final String DELETE_CONTACT_PATTERN = "([\\W]+){7}(\\s)(\\d)";
    private static final String FIND_CONTACT_PATTERN = "([\\W]+){5}(\\s)(\\d)";

    private final List<Long> ADMIN_ID_CHAT = List.of();

    @Command(name = "/infoForUse")
    public SendMessage infoAboutUseBot(Message message) {
        return new SendMessage(message.from().id(), ADMIN_COMMAND);
    }

    @Command
    public SendMessage createBotUser(Message message) {

        return null;
    }

    @Command(pattern = FIND_CONTACT_PATTERN)
    public SendMessage getBotUser(Message message) {
        return null;

    }

    @Command(pattern = DELETE_CONTACT_PATTERN)
    public SendMessage deleteBotUser(Message message) {
        Pattern pattern = Pattern.compile(DELETE_CONTACT_PATTERN);
        Matcher matcher = pattern.matcher(message.text());
        matcher.find();
        Long id = Long.valueOf(matcher.group(3));
        botUserService.deleteBotUser(id);
        return new SendMessage(message.from().id(), "test");
    }

    @Command(pattern = "")
    public SendMessage editBotUser(Message message) {

        return null;
    }


    @Command(name = "/getAll")
    public SendMessage getAllBotUser(Message message) {
        Long idUser = message.from().id();
        LOG.info("Администратор {} запросил все пользователей из базы данных", idUser);
        List<BotUser> allUsers = botUserService.getAll();
        return new SendMessage(idUser, allUsers.toString());
    }


    @Command(name = "/badUser")
    public SendMessage getBadUser(Message message) {
        return null;
    }

    @Command
    public SendMessage getLastReport(Message message) {
        return null;
    }

}
