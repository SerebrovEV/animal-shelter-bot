package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.service.CatUserService;
import com.animalshelter.animalshelterbot.service.ValidatorCatUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <i>Контроллер для добавления, редактирования, проверки наличия и получения
 * всех {@link CatUser} в/из базы данных приюта для кошек администратором. Обычный пользователь не имеет доступа к данным командам.
 * </i>
 */
@Component
@RequiredArgsConstructor
public class AdminCatUserController implements CommandController {
    private final ValidatorCatUserService validatorCatUserService;
    private final CatUserService catUserService;

    private final Logger LOG = LoggerFactory.getLogger(AdminCatUserController.class);
    private final String ADMIN_COMMAND = "Правила использования: \n" +
            "/infoAboutAdminCatUser - команды для использования;\n" +
            "Сохранить КП 89871234567 Иван - добавить усыновителя;\n" +
            "Найти КП 10 - найти усыновителя с id = 10;\n" +
            "Изменить КП 10 89871234567 Миша - изменить усыновителя с id = 10;\n" +
            "Удалить КП 10 - удалить усыновителя с id = 10;\n" +
            "/getAllCatUser - получить список всех усыновителей;\n" +
            "/badCatUser - получить список усыновителей, которые не прислали отчеты за сегодняшний день;\n" +
            "Отчет КП 10 - получить последний отчет от усыновителя с id = 10;\n" +
            "Предупреждение 10 - отправить предупреждение усыновителю id = 10.";

    private static final String SAVE_CONTACT_PATTERN = "Сохранить КП ([\\d]{11})(\\s)([\\W]+)";
    private static final String EDIT_CONTACT_PATTERN = "Изменить КП ([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)";
    private static final String DELETE_CONTACT_PATTERN = "Удалить КП ([\\d]+)";
    private static final String FIND_CONTACT_PATTERN = "Найти КП ([\\d]+)";

    //Список id чатов волонтеров для администрирования
    // private final List<Long> ADMIN_ID_CHAT = List.of();

    /**
     * <i>Метод для получения инструкции по использованию команд администратора.
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/infoAboutAdminCatUser")
    public SendMessage handleInfoAboutAdminCatUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} запросил инструкцию по использованию бота", idAdmin);
        return new SendMessage(idAdmin, ADMIN_COMMAND);

    }

    /**
     * <i>Метод для записи контактных данных усыновителя в базу данных приюта для кошек администратором
     * <br>
     * Используется метод {@link ValidatorCatUserService#validateCatUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = SAVE_CONTACT_PATTERN)
    public SendMessage handleCreateCatUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} сохраняет контакт усыновителя в базу данных", idAdmin);
        String answer = validatorCatUserService.validateCatUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для получения контактных данных усыновителя из базы данных приюта для кошек администратором
     * <br>
     * Используется метод {@link ValidatorCatUserService#validateGetCatUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = FIND_CONTACT_PATTERN)
    public SendMessage handleGetCatUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запрашивает контакт усыновителя в базе данных приюта для кошек", idAdmin);
        String answer = validatorCatUserService.validateGetCatUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для удаления контактных данных усыновителя из базы данных приюта для кошек администратором
     * <br>
     * Используется метод {@link ValidatorCatUserService#validateDeleteCatUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_CONTACT_PATTERN)
    public SendMessage handleDeleteCatUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} запросил удаление усыновителя из базы данных", idAdmin);
        String answer = validatorCatUserService.validateDeleteCatUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для редактирования контактных данных усыновителя в базе данных приюта для кошек администратором
     * <br>
     * Используется метод {@link ValidatorCatUserService#validateEditCatUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = EDIT_CONTACT_PATTERN)
    public SendMessage handleEditCatUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} изменяет контакт усыновителя в базе данных приюта для кошек", idAdmin);
        String answer = validatorCatUserService.validateEditCatUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для получения контактных данных всех усыновителей из базы данных приюта для кошек администратором
     * <br>
     * Используется метод {@link CatUserService#getAllCatUser()} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllCatUser")
    public SendMessage handleGetAllCatUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех пользователей из базы данных приюта для кошек", idAdmin);
        List<CatUser> allUsers = catUserService.getAllCatUser();
        return new SendMessage(idAdmin, allUsers.toString());
    }

}
