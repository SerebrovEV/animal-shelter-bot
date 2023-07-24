package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.service.UserService;
import com.animalshelter.animalshelterbot.service.ValidateUserService;
import com.animalshelter.animalshelterbot.service.impl.CatUserService;
import com.animalshelter.animalshelterbot.service.impl.ValidateCatUserService;
import com.animalshelter.animalshelterbot.service.impl.ValidateDogUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <i>Контроллер для добавления, редактирования, поиска, удаления, получения всех {@link CatUser} (хозяев) в/из базы
 * данных приюта для кошек администратором. А также отправки поздравления администратором об успешном окончании
 * испытательного срока и уведомлении о неуспешности адаптационного периода.
 * Обычный пользователь не имеет доступа к данным командам.
 * </i>
 */
@Component
public class AdminCatUserController implements CommandController {
    private final ValidateUserService validateUserService;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(AdminCatUserController.class);
    private final String adminCommand = "Команды для работы с усыновителями кошек: \n" +
            "/infoAboutAdminCatUser - команды для использования;\n" +
            "Сохранить КП 89871234567 Иван - добавить усыновителя;\n" +
            "Найти КП 10 - найти усыновителя с id = 10;\n" +
            "Изменить КП 10 89871234567 Миша - изменить усыновителя с id = 10;\n" +
            "Удалить КП 10 - удалить усыновителя с id = 10;\n" +
            "Поздравить КП 2 - поздравить усыновителя с id = 2 с окончанием испытательного срока;\n" +
            "Неудача КП 3 - направить усыновителю с id = 3 сообщение о том, что он не прошел испытательный срок;\n" +
            "/getAllCatUser - получить список всех усыновителей.";

    private static final String SAVE_CONTACT_PATTERN = "Сохранить КП ([\\d]{11})(\\s)([\\W]+)";
    private static final String EDIT_CONTACT_PATTERN = "Изменить КП ([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)";
    private static final String DELETE_CONTACT_PATTERN = "Удалить КП ([\\d]+)";
    private static final String FIND_CONTACT_PATTERN = "Найти КП ([\\d]+)";
    private static final String CONGRATULATION_CONTACT_PATTERN = "Поздравить КП ([\\d]+)";
    private static final String RETURN_CONTACT_PATTERN = "Неудача КП ([\\d]+)";

    public AdminCatUserController(@Qualifier("validateCatUserService")ValidateUserService validateUserService,
                                  @Qualifier("catUserService") UserService userService) {
        this.validateUserService = validateUserService;
        this.userService = userService;
    }


    /**
     * <i>Метод для получения инструкции по использованию команд администратора.
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/infoAboutAdminCatUser")
    public SendMessage handleInfoAboutAdminCatUser(Message message) {
        Long idAdmin = message.from().id();
        logger.info("Администратор {} запросил инструкцию по использованию бота", idAdmin);
        return new SendMessage(idAdmin, adminCommand);

    }

    /**
     * <i>Метод для записи контактных данных усыновителя в базу данных приюта для кошек администратором
     * <br>
     * Используется метод {@link ValidateCatUserService#validateUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = SAVE_CONTACT_PATTERN)
    public SendMessage handleCreateCatUser(Message message) {
        Long idAdmin = message.from().id();
        logger.info("Администратор {} сохраняет контакт усыновителя в базу данных", idAdmin);
        String answer = validateUserService.validateUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для получения контактных данных усыновителя из базы данных приюта для кошек администратором
     * <br>
     * Используется метод {@link ValidateCatUserService#validateGetUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = FIND_CONTACT_PATTERN)
    public SendMessage handleGetCatUser(Message message) {
        Long idAdmin = message.from().id();
        logger.info("Администратор {} запрашивает контакт усыновителя в базе данных приюта для кошек", idAdmin);
        String answer = validateUserService.validateGetUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для удаления контактных данных усыновителя из базы данных приюта для кошек администратором
     * <br>
     * Используется метод {@link ValidateCatUserService#validateDeleteUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_CONTACT_PATTERN)
    public SendMessage handleDeleteCatUser(Message message) {
        Long idAdmin = message.from().id();
        logger.warn("Администратор {} запросил удаление усыновителя из базы данных", idAdmin);
        String answer = validateUserService.validateDeleteUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для редактирования контактных данных усыновителя в базе данных приюта для кошек администратором
     * <br>
     * Используется метод {@link ValidateCatUserService#validateEditUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = EDIT_CONTACT_PATTERN)
    public SendMessage handleEditCatUser(Message message) {
        Long idAdmin = message.from().id();
        logger.warn("Администратор {} изменяет контакт усыновителя в базе данных приюта для кошек", idAdmin);
        String answer = validateUserService.validateEditUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для получения контактных данных всех усыновителей из базы данных приюта для кошек администратором
     * <br>
     * Используется метод {@link CatUserService#getAllUser()} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllCatUser")
    public List<SendMessage> handleGetAllCatUser(Message message) {
        Long idAdmin = message.from().id();
        logger.info("Администратор {} запросил всех пользователей из базы данных приюта для кошек", idAdmin);
        List<CatUser> answer = userService.getAllUser();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }
    /**
     * <i>Метод для отправки поздравления усыновителю из базы данных приюта для кошек администратором
     * <br>
     * Используется метод {@link ValidateCatUserService#validateCongratulationUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = CONGRATULATION_CONTACT_PATTERN)
    public SendMessage handleCongratulationCatUser(Message message) {
        Long idAdmin = message.from().id();
        logger.info("Администратор {} направил поздравление усыновителю из базы данных приюта для кошек", idAdmin);
        String answer = validateUserService.validateCongratulationUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }
    /**
     * <i>Метод для отправки данных усыновителю о неуспешном испытательном сроке из базы данных приюта для собак администратором
     * <br>
     * Используется метод {@link ValidateDogUserService#validateReturnUserFromAdmin(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = RETURN_CONTACT_PATTERN)
    public SendMessage handleReturnCatUser(Message message) {
        Long idAdmin = message.from().id();
        logger.info("Администратор {} направил возврат животного усыновителю из базы данных приюта для кошек", idAdmin);
       String answer = validateUserService.validateReturnUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

}
