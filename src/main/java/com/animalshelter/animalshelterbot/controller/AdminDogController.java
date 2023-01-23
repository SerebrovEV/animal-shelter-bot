package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.service.AdoptedDogService;
import com.animalshelter.animalshelterbot.service.ValidateAdoptedDogService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class AdminDogController implements CommandController {

    private final ValidateAdoptedDogService validateAdoptedDogService;
    private final AdoptedDogService adoptedDogService;
    private final Logger logger = LoggerFactory.getLogger(AdminDogController.class);
    private final String adminCommand = "Правила по работе с БД приюта для собак: \n" +
            "/infoAboutAdminDog - команды для использования;\n" +
            "Сохранить c Белка - добавить собаку в базу данных приюта;\n" +
            "Найти с 10 - найти собаку с id = 10;\n" +
            "Изменить с 10 Стрелка - изменить собаку с id = 10;\n" +
            "Удалить с 10 - удалить собаку с id = 10;\n" +
            "Усыновить 11 с 10 - усыновить собаку с id = 11, новый хозяин с id = 10;\n" +
            "Вернуть с 10 - вернуть собаку с id = 10 в приют от плохого усыновителя;\n" +
            "Продлить с 2 на 14 (30) - продлить период адаптации собаки с id=2 на 14 дней(или на 30 дней) для плохого усыновителя;\n" +
            "/getAllDog - получить список всех собак;\n" +
            "/getAllFreeDog - получить список всех свободных собак в приюте;\n" +
            "/getAllDogOnTrialPeriod  - получить список всех собак на испытательном периоде." +
            "/getAllDogWithEndPeriod - получить список всех собак с окончаниям испытательного срока;\n";

    private static final String SAVE_DOG_PATTERN = "Сохранить с ([\\W]+)";
    private static final String EDIT_DOG_PATTERN = "Изменить с ([\\d]+)(\\s)([\\W]+)";
    private static final String DELETE_DOG_PATTERN = "Удалить с ([\\d]+)";
    private static final String FIND_DOG_PATTERN = "Найти с ([\\d]+)";
    private static final String TAKE_DOG_PATTERN = "Усыновить ([\\d]+) с ([\\d]+)";
    private static final String RETURN_DOG_PATTERN = "Вернуть с ([\\d]+)";
    private static final String EXTEND_DOG_PATTERN = "Продлить с ([\\d]+) на ([\\d]+)";

    /**
     * <i>Метод для получения инструкции по использованию команд администратора.
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/infoAboutAdminDog")
    public SendMessage handleInfoAboutAdminDog(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        logger.info("Администратор {} запросил инструкцию по использованию бота", idAdmin);
        return new SendMessage(idAdmin, adminCommand);
    }
    /**
     * <i>Метод для записи собак в базу данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command (pattern = SAVE_DOG_PATTERN)
    public SendMessage handleCreateDog(Message message) {
        Long idAdmin = message.from().id();
        logger.info("Администратор {} сохраняет собаку в базу данных приюта для собак", idAdmin);
        String answer = validateAdoptedDogService.validateAddDog(message);
        return new SendMessage(idAdmin, answer);
    }
    /**
     * <i>Метод для удаления собак из базы данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_DOG_PATTERN)
    public SendMessage handleDeleteDog(Message message) {

        Long idAdmin = message.from().id();
        logger.warn("Администратор {} удаляет собаку из базы данных приюта для собак", idAdmin);
        String answer = validateAdoptedDogService.validateDeleteDog(message);
        return new SendMessage(idAdmin, answer);
    }
    /**
     * <i>Метод для поиска собаки в базе данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = FIND_DOG_PATTERN)
    public SendMessage handleGetDog(Message message) {

        Long idAdmin = message.from().id();
        logger.info("Администратор {} выполняет поиск собаки в базе данных приюта для собак", idAdmin);
        String answer = validateAdoptedDogService.validateGetDog(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для изменения данных о собаке в базе данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = EDIT_DOG_PATTERN)
    public SendMessage handleEditDog(Message message) {

        Long idAdmin = message.from().id();
        logger.warn("Администратор {} выполняет изменение собаки в базе данных приюта для собак", idAdmin);
        String answer = validateAdoptedDogService.validateEditDog(message);
        return new SendMessage(idAdmin, answer);
    }
    /**
     * <i>Метод для поиска списка всех собак в базе данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllDog")
    public List<SendMessage> handleGetAllDog(Message message) {

        Long idAdmin = message.from().id();
        logger.info("Администратор {} запросил всех собак в базе данных приюта для собак", idAdmin);
        List<AdoptedDog> answer = adoptedDogService.getAllDog();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }

    /**
     * <i>Метод для поиска всех свободных собак в базе данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllFreeDog")
    public List<SendMessage> handleGetAllFreeDog(Message message) {

        Long idAdmin = message.from().id();
        logger.info("Администратор {} запросил всех свободных собак в базе данных приюта для собак", idAdmin);
        List<AdoptedDog> answer = adoptedDogService.getAllFreeDog();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }

    /**
     * <i>Метод для поиска всех усыновленных собак в базе данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllDogOnTrialPeriod")
    public List<SendMessage> handleGetAllDogOnTrialPeriod(Message message) {

        Long idAdmin = message.from().id();
        logger.info("Администратор {} запросил всех усыновленных собак в базе данных приюта для собак", idAdmin);
        List<AdoptedDog> answer = adoptedDogService.getAllDogOnTrialPeriod();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }
    /**
     * <i>Метод для поиска всех усыновленных собак с оконченным испытательным сроком в базе данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllDogWithEndPeriod")
    public List<SendMessage> handleGetAllDogWithEndPeriod(Message message) {

        Long idAdmin = message.from().id();
        logger.info("Администратор {} запросил всех усыновленных собак с окончанием испытательного срока в базе данных приюта для собак", idAdmin);
        List<AdoptedDog> answer = adoptedDogService.getAllDogWithEndPeriod();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }
    /**
     * <i>Метод для добавления данных об усыновителе в базе данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = TAKE_DOG_PATTERN)
    public SendMessage handleTakeDog(Message message) {

        Long idAdmin = message.from().id();
        logger.info("Администратор {} добавляет усыновителя у собаки в базе данных приюта для собак", idAdmin);
        String answer = validateAdoptedDogService.validateTakeDog(message);
        return new SendMessage(idAdmin, answer);
    }
    /**
     * <i>Метод для удаления записи об усыновителе в базе данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = RETURN_DOG_PATTERN)
    public SendMessage handleReturnDog(Message message) {

        Long idAdmin = message.from().id();
        logger.info("Администратор {} удаляет усыновителя у собаки в базе данных приюта для собак", idAdmin);
        String answer = validateAdoptedDogService.validateReturnDog(message);
        return new SendMessage(idAdmin, answer);
    }
    /**
     * <i>Метод для добавления информации о продлении испытательного срока в базе данных приюта для собак администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = EXTEND_DOG_PATTERN)
    public SendMessage handleExtendCDog(Message message) {

        Long idAdmin = message.from().id();
        logger.info("Администратор {} продлевает испытательный срок для усыновителя в базе данных приюта для собак", idAdmin);
        String answer = validateAdoptedDogService.validateExtendDog(message);
        return new SendMessage(idAdmin, answer);
    }

}
