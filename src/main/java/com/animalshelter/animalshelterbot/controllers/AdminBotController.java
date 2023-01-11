package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.service.DogUserService;
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
 * всех {@link DogUser} в/из базы данных администратором. Обычный пользователь не имеет доступа к данным командам.
 * </i>
 */
@Component
@RequiredArgsConstructor
public class AdminBotController implements CommandController {
    private final DogUserService dogUserService;
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


    private static final String SAVE_CONTACT_PATTERN = "Сохранить(\\s)([\\d]{11})(\\s)([\\W]+)";

    private static final String EDIT_CONTACT_PATTERN = "Изменить(\\s)([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)";
    private static final String DELETE_CONTACT_PATTERN = "Удалить(\\s)([\\d]+)";

    private static final String FIND_CONTACT_PATTERN = "Найти(\\s)([\\d]+)";
    private final String REPORT_PATTERN = "[Отчет]";

    private final List<Long> ADMIN_ID_CHAT = List.of();

    /**
     * <i>Метод для получения инструкции по использованию команд администратора.
     * <br>
     * @param {@link Message}
     * @return {@link SendMessage}
     */
    @Command(name = "/infoForUse")
    public SendMessage infoAboutUseBot(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} запросил инструкцию по использованию бота", idAdmin);
        return new SendMessage(message.from().id(), ADMIN_COMMAND);

    }

    /**
     * <i>Метод для записи контактных данных усыновителя администратором
     * <br>
     * Используется метод {@link ValidatorUserService#validateUserFromAdmin(Message)}</i>
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = SAVE_CONTACT_PATTERN)
    public SendMessage createBotUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} сохраняет контакт усыновителя в базу данных", idAdmin);
        String answer = validatorUserService.validateUserFromAdmin(message);
        return new SendMessage(message.from().id(), answer);
    }

    /**
     * <i>Метод для получения контактных данных усыновителя администратором
     * <br>
     * Используется метод {@link ValidatorUserService#validateGetUserFromAdmin(Message)} </i>
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = FIND_CONTACT_PATTERN)
    public SendMessage getBotUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запрашивает контакт усыновителя в базе данных", idAdmin);
        String answer = validatorUserService.validateGetUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для удаления контактных данных усыновителя администратором
     * <br>
     * Используется метод {@link ValidatorUserService#validateDeleteUser(Message)} </i>
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_CONTACT_PATTERN)
    public SendMessage deleteBotUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} запросил удаление усыновителя из базы данных", idAdmin);
        String answer = validatorUserService.validateDeleteUser(message);
        return new SendMessage(message.from().id(), answer);
    }

    /**
     * <i>Метод для редактирования контактных данных усыновителя администратором
     * <br>
     * Используется метод {@link ValidatorUserService#validateEditUser(Message)} </i>
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = EDIT_CONTACT_PATTERN)
    public SendMessage editBotUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} изменяет контакт усыновителя в базе данных", idAdmin);
        String answer = validatorUserService.validateEditUser(message);
        return new SendMessage(message.from().id(), answer);
    }

    /**
     * <i>Метод для получения контактных данных всех усыновителей администратором
     * <br>
     * Используется метод {@link DogUserService#getAllDogUser()} </i>
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAll")
    public SendMessage getAllBotUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех пользователей из базы данных", idAdmin);
        List<DogUser> allUsers = dogUserService.getAllDogUser();
        return new SendMessage(idAdmin, allUsers.toString());
    }

    /**
     * <i>Метод для получения контактных данных недобросовестных усыновителей администратором
     * <br>
     * Используется метод  </i>
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/badUser")
    public SendMessage getBadUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил данные недобросовестных усыновителей из базы данных", idAdmin);
        return new SendMessage(idAdmin, "badUser");
    }

    /**
     * <i>Метод для получения последнего отчета усыновителя администратором
     * <br>
     * Используется метод </i>
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = REPORT_PATTERN)
    public SendMessage getLastReport(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил отчет усыновителя из базы данных", idAdmin);
        return new SendMessage(idAdmin, "getLastReport");
    }

}
