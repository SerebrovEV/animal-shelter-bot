package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.service.BotUserService;
import com.animalshelter.animalshelterbot.service.ValidatorUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <i>Контроллер для добавления, редактирования, проверки наличия и получения
 * всех {@link BotUser} в/из базы данных администратором. Обычный пользователь не имеет доступа к данным командам.
 * </i>
 */
@Component
@RequiredArgsConstructor
public class AdminBotController implements CommandController {
    private final BotUserService botUserService;
    private final ValidatorUserService validatorUserService;
    private final Logger LOG = LoggerFactory.getLogger(AdminBotController.class);

    private final String ADMIN_COMMAND = "Правила использования: \n" +
            "Сохранить 89871234567 Иван - добавление контакта усыновителя;\n" +
            "Найти 10 - поиск пользователя с id = 10;\n" +
            "Изменить 10 89871234567 Миша - изменение пользователя с id = 10;\n" +
            "Удалить 10 - удаление пользователя с id = 10;\n" +
            "/getAll - получить список всех усыновителей;\n" +
            "/badUser - получить список усыновителей, которые не прислали отчеты;\n" +
            "Отчет 10 - получить последний отчет от усыновителя id = 10.";

    private static final String SAVE_CONTACT_PATTERN = "([\\W]{9})(\\s)([\\d]{11})(\\s)([\\W]+)";
    private static final String EDIT_CONTACT_PATTERN = "([\\W]{8})(\\s)([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)";
    private static final String DELETE_CONTACT_PATTERN = "([\\W]{7})(\\s)([\\d]+)";
    private static final String FIND_CONTACT_PATTERN = "([\\W]{5})(\\s)([\\d]+)";
    private static final String REPORT_PATTERN = "[Отчет]";

    private final List<Long> ADMIN_ID_CHAT = List.of();

    @Command(name = "/infoForUse")
    public SendMessage infoAboutUseBot(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} запросил инструкцию по использованию бота", idAdmin);
        return new SendMessage(message.from().id(), ADMIN_COMMAND);

    }

    @Command(pattern = SAVE_CONTACT_PATTERN)
    public SendMessage createBotUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} сохраняет контакт усыновителя в базу данных", idAdmin);
        String answer = validatorUserService.validateUserFromAdmin(message);
        return new SendMessage(message.from().id(), answer);
    }

    @Command(pattern = FIND_CONTACT_PATTERN)
    public SendMessage getBotUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запрашивает контакт усыновителя в базе данных", idAdmin);
        String answer = validatorUserService.validateGetUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    @Command(pattern = DELETE_CONTACT_PATTERN)
    public SendMessage deleteBotUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} запросил удаление усыновителя из базы данных", idAdmin);
        String answer = validatorUserService.validateDeleteUser(message);
        return new SendMessage(message.from().id(), answer);
    }

    @Command(pattern = EDIT_CONTACT_PATTERN)
    public SendMessage editBotUser(Message message) {
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} изменяет контакт усыновителя в базе данных", idAdmin);
        String answer = validatorUserService.validateEditUser(message);
        return new SendMessage(message.from().id(), answer);
    }


    @Command(name = "/getAll")
    public SendMessage getAllBotUser(Message message) {
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех пользователей из базы данных", idAdmin);
        List<BotUser> allUsers = botUserService.getAll();
        return new SendMessage(idAdmin, allUsers.toString());
    }


    @Command(name = "/badUser")
    public SendMessage getBadUser(Message message) {
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех пользователей из базы данных", idAdmin);
        return new SendMessage(idAdmin, "badUSer");
    }

    @Command(pattern = REPORT_PATTERN)
    public SendMessage getLastReport(Message message) {
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех пользователей из базы данных", idAdmin);
        return new SendMessage(idAdmin, "getLastReport");
    }

}
