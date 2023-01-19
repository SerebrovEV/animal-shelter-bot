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
import java.util.stream.Collectors;

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
    private final String ADMIN_COMMAND = "Команды для работы с усыновителями кошек: \n" +
            "/infoAboutAdminCatUser - команды для использования;\n" +
            "Сохранить КП 89871234567 Иван - добавить усыновителя;\n" +
            "Найти КП 10 - найти усыновителя с id = 10;\n" +
            "Изменить КП 10 89871234567 Миша - изменить усыновителя с id = 10;\n" +
            "Удалить КП 10 - удалить усыновителя с id = 10;\n" +
            "Поздравить КП 2 - поздравить усыновителя с id = 2 с окончанием испытательного срока;\n" +
            "Неудача КП 3 - направить усыновителю с id = 3 сообщение о том, что он не прошел испытательный срок;\n" +
            "/getAllCatUser - получить список всех усыновителей;\n";

    private static final String SAVE_CONTACT_PATTERN = "Сохранить КП ([\\d]{11})(\\s)([\\W]+)";
    private static final String EDIT_CONTACT_PATTERN = "Изменить КП ([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)";
    private static final String DELETE_CONTACT_PATTERN = "Удалить КП ([\\d]+)";
    private static final String FIND_CONTACT_PATTERN = "Найти КП ([\\d]+)";
    private static final String CONGRATULATION_CONTACT_PATTERN = "Поздравить КП ([\\d]+)";
    private static final String RETURN_CONTACT_PATTERN = "Неудача КП ([\\d]+)";


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
        LOG.info("Администратор {} запросил инструкцию по использованию бота", idAdmin);
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
    public List<SendMessage> handleGetAllCatUser(Message message) {
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех пользователей из базы данных приюта для кошек", idAdmin);
        List<CatUser> answer = catUserService.getAllCatUser();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }

    @Command(pattern = CONGRATULATION_CONTACT_PATTERN)
    public SendMessage handleCongratulationCatUser(Message message) {
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} направил поздравление усыновителю из базы данных приюта для кошек", idAdmin);
        String answer = validatorCatUserService.validateCongratulationCatUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }

    @Command(pattern = RETURN_CONTACT_PATTERN)
    public SendMessage handleReturnCatUser(Message message) {
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} направил возврат животного усыновителю из базы данных приюта для кошек", idAdmin);
       String answer = validatorCatUserService.validateReturnCatUserFromAdmin(message);
        return new SendMessage(idAdmin, answer);
    }



}
