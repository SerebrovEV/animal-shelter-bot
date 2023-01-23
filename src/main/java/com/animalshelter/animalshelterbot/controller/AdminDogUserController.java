package com.animalshelter.animalshelterbot.controller;

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
 * всех {@link DogUser} в/из базы данных приюта для собак администратором. Обычный пользователь не имеет доступа к
 * данным командам.
 * </i>
 */
@Component
@RequiredArgsConstructor
public class AdminDogUserController implements CommandController {
    private final DogUserService dogUserService;
    private final ValidatorDogUserService validatorDogUserService;
    private final Logger logger = LoggerFactory.getLogger(AdminDogUserController.class);


    private final String adminCommand = "Правила использования: \n" +
            "/infoAboutAdminDogUser - команды для использования;\n" +
            "Сохранить СП 89871234567 Иван - добавить усыновителя;\n" +
            "Найти СП 10 - найти усыновителя с id = 10;\n" +
            "Изменить СП 10 89871234567 Миша - изменить усыновителя с id = 10;\n" +
            "Удалить СП 10 - удалить усыновителя с id = 10;\n" +
            "Поздравить CП 2 - поздравить усыновителя с id = 2 с окончанием испытательного срока;\n" +
            "Неудача CП 3 - направить усыновителю с id = 3 сообщение о том, что он не прошел испытательный срок;\n" +
            "/getAllDogUser - получить список всех усыновителей;\n";

    private static final String SAVE_CONTACT_PATTERN = "Сохранить СП ([\\d]{11})(\\s)([\\W]+)";

    private static final String EDIT_CONTACT_PATTERN = "Изменить СП ([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)";
    private static final String DELETE_CONTACT_PATTERN = "Удалить СП ([\\d]+)";

    private static final String FIND_CONTACT_PATTERN = "Найти СП ([\\d]+)";
    private static final String CONGRATULATION_CONTACT_PATTERN = "Поздравить СП ([\\d]+)";
    private static final String RETURN_CONTACT_PATTERN = "Неудача СП ([\\d]+)";


    /**
     * <i>Метод для получения инструкции по использованию команд администратора.
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/infoAboutAdminDogUser")
    public SendMessage handleInfoAboutAdminDogUser(Message message) {
        Long idAdmin = message.from().id();
        logger.warn("Администратор {} запросил инструкцию по использованию бота", idAdmin);
        return new SendMessage(idAdmin, adminCommand);

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
        Long idAdmin = message.from().id();
        logger.info("Администратор {} сохраняет контакт усыновителя в базу данных приюта для собак", idAdmin);
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
        Long idAdmin = message.from().id();
        logger.info("Администратор {} запрашивает контакт усыновителя в базе данных приюта для собак", idAdmin);
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
        Long idAdmin = message.from().id();
        logger.warn("Администратор {} запросил удаление усыновителя из базы данных приюта для собак", idAdmin);
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
        Long idAdmin = message.from().id();
        logger.warn("Администратор {} изменяет контакт усыновителя в базе данных приюта для собак", idAdmin);
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
        Long idAdmin = message.from().id();
        logger.info("Администратор {} запросил всех пользователей из базы данных приюта для собак", idAdmin);
        List<DogUser> allUsers = dogUserService.getAllDogUser();
        return new SendMessage(idAdmin, allUsers.toString());
    }
    /**
     * <i>Метод для отправки поздравления усыновителю из базы данных приюта для собак администратором
     * <br>
     * Используется метод {@link ValidatorDogUserService#validateCongratulationDogUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = CONGRATULATION_CONTACT_PATTERN)
    public SendMessage handleCongratulationDogUser(Message message) {
        Long idAdmin = message.from().id();
        logger.info("Администратор {} направил поздравление усыновителю из базы данных приюта для собак", idAdmin);
        String answer = validatorDogUserService.validateCongratulationDogUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }
    /**
     * <i>Метод для отправки данных усыновителю о неуспешном испытательном сроке из базы данных приюта для собак администратором
     * <br>
     * Используется метод {@link ValidatorDogUserService#validateReturnDogUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = RETURN_CONTACT_PATTERN)
    public SendMessage handleReturnDogUser(Message message) {
        Long idAdmin = message.from().id();
        logger.info("Администратор {} направил уведомление на возврат животного усыновителю из базы данных приюта для собак", idAdmin);
        String answer = validatorDogUserService.validateReturnDogUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

}
