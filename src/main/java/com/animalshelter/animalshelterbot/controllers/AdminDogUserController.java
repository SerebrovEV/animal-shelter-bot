package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.service.DogUserService;
import com.animalshelter.animalshelterbot.service.ValidatorDogUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <i>Контроллер для добавления, редактирования, проверки наличия и получения
 * всех {@link DogUser} в/из базы данных приюта для собак администратором. Обычный пользователь не имеет доступа к данным командам.
 * </i>
 */
@Component
@RequiredArgsConstructor
public class AdminDogUserController implements CommandController {
    private final DogUserService dogUserService;
    private final ValidatorDogUserService validatorDogUserService;
    private final Logger LOG = LoggerFactory.getLogger(AdminDogUserController.class);

    private final String ATTENTION_MESSAGE = "«Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно," +
            " как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта " +
            "будут обязаны самолично проверять условия содержания животного»";


    private final String ADMIN_COMMAND = "Правила использования: \n" +
            "/infoAboutAdminDogUser - команды для использования;\n" +
            "Сохранить СП 89871234567 Иван - добавить усыновителя;\n" +
            "Найти СП 10 - найти усыновителя с id = 10;\n" +
            "Изменить СП 10 89871234567 Миша - изменить усыновителя с id = 10;\n" +
            "Удалить СП 10 - удалить усыновителя с id = 10;\n" +
            "/getAllDogUser - получить список всех усыновителей;\n" +
            "/badDogUser - получить список усыновителей, которые не прислали отчеты за сегодняшний день;\n" +
            "Отчет СП 10 - получить последний отчет от усыновителя с id = 10;\n" +
            "Предупреждение 10 - отправить предупреждение усыновителю id = 10.";

    private final String ATTENTION_PATTERN = "Предупреждение ([\\d]+)";
    private static final String SAVE_CONTACT_PATTERN = "Сохранить СП ([\\d]{11})(\\s)([\\W]+)";

    private static final String EDIT_CONTACT_PATTERN = "Изменить СП ([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)";
    private static final String DELETE_CONTACT_PATTERN = "Удалить СП ([\\d]+)";

    private static final String FIND_CONTACT_PATTERN = "Найти СП ([\\d]+)";
    private final String REPORT_PATTERN = "Отчет СП ([\\d]+)";

    //Список id чатов волонтеров для администрирования
    //   private final List<Long> ADMIN_ID_CHAT = List.of();

    /**
     * <i>Метод для получения инструкции по использованию команд администратора.
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/infoAboutAdminDogUser")
    public SendMessage handleInfoAboutAdminDogUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} запросил инструкцию по использованию бота", idAdmin);
        return new SendMessage(idAdmin, ADMIN_COMMAND);

    }

    /**
     * <i>Метод для записи контактных данных усыновителя в базу данных приюта для собак администратором
     * <br>
     * Используется метод {@link ValidatorDogUserService#validateDogUserFromAdmin(Message)}</i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = SAVE_CONTACT_PATTERN)
    public SendMessage handleCreateDogUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} сохраняет контакт усыновителя в базу данных приюта для собак", idAdmin);
        String answer = validatorDogUserService.validateDogUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для получения контактных данных усыновителя из базы данных приюта для собак администратором
     * <br>
     * Используется метод {@link ValidatorDogUserService#validateGetDogUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = FIND_CONTACT_PATTERN)
    public SendMessage handleGetDogUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запрашивает контакт усыновителя в базе данных приюта для собак", idAdmin);
        String answer = validatorDogUserService.validateGetDogUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для удаления контактных данных усыновителя из базы данных приюта для собак администратором
     * <br>
     * Используется метод {@link ValidatorDogUserService#validateDeleteDogUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_CONTACT_PATTERN)
    public SendMessage handleDeleteDogUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} запросил удаление усыновителя из базы данных приюта для собак", idAdmin);
        String answer = validatorDogUserService.validateDeleteDogUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для редактирования контактных данных усыновителя в базе данных приюта для собак администратором
     * <br>
     * Используется метод {@link ValidatorDogUserService#validateEditDogUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = EDIT_CONTACT_PATTERN)
    public SendMessage handleEditDogUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} изменяет контакт усыновителя в базе данных приюта для собак", idAdmin);
        String answer = validatorDogUserService.validateEditDogUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для получения контактных данных всех усыновителей из базы данных приюта для собак администратором
     * <br>
     * Используется метод {@link DogUserService#getAllDogUser()} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllDogUser")
    public SendMessage handleGetAllDogUser(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех пользователей из базы данных приюта для собак", idAdmin);
        List<DogUser> allUsers = dogUserService.getAllDogUser();
        return new SendMessage(idAdmin, allUsers.toString());
    }

    // Методы ниже в работе!!!

    /**
     * <i>Метод для получения контактных данных недобросовестных усыновителей администратором
     * <br>
     * Используется метод  </i>
     *
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
     *
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

    /**
     * <i>Метод для отправки предупреждения усыновителю от администратором
     * <br>
     * Используется метод </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = ATTENTION_PATTERN)
    public SendMessage handleAttentionMessage(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} направил предупреждение усыновителю", idAdmin);
        return new SendMessage(idAdmin, ATTENTION_MESSAGE); //надо преобразовать телефон в idChat
    }


}
