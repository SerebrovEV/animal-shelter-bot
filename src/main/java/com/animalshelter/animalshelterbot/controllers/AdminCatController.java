package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.service.AdoptedCatService;
import com.animalshelter.animalshelterbot.service.ValidateAdoptedCatService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdminCatController implements CommandController {

    private final AdoptedCatService adoptedCatService;
    private final ValidateAdoptedCatService validateAdoptedCatService;
    private final Logger LOG = LoggerFactory.getLogger(AdminCatUserController.class);
    private final String ADMIN_COMMAND = "Команды для работы с кошками: \n" +
            "/infoAboutAdminCat - команды для использования;\n" +
            "Сохранить к Мурзик - добавить кошку в базу данных приюта;\n" +
            "Найти к 10 - найти кошку с id = 10;\n" +
            "Изменить к 10 Мила - изменить кошку с id = 10;\n" +
            "Удалить к 10 - удалить кошку с id = 10;\n" +
            "Усыновить 11 к 10 - усыновить кошку с id = 11, новый хозяин с id = 10;\n" +
            "Вернуть к 10 - вернуть кошку с id = 10 в приют от плохого усыновителя;\n" +
            "Продлить к 2 на 14 (30) - продлить период адаптации кошке с id=2 на 14 дней(или на 30 дней) для плохого усыновителя;\n" +
            "/getAllCat - получить список всех кошек;\n" +
            "/getAllFreeCat - получить список всех свободных кошек в приюте;\n" +
            "/getAllBusyCat  - получить список всех кошек на испытательном периоде."+
            "/getAllCatWithEndPeriod - получить список всех кошек с окончаниям испытательного срока;\n";

    private static final String SAVE_CAT_PATTERN = "Сохранить к ([\\W]+)";
    private static final String EDIT_CAT_PATTERN = "Изменить к ([\\d]+)(\\s)([\\W]+)";
    private static final String DELETE_CAT_PATTERN = "Удалить к ([\\d]+)";
    private static final String FIND_CAT_PATTERN = "Найти к ([\\d]+)";
    private static final String TAKE_CAT_PATTERN = "Усыновить ([\\d]+) к ([\\d]+)";
    private static final String RETURN_CAT_PATTERN = "Вернуть к ([\\d]+)";
    private static final String EXTEND_CAT_PATTERN = "Продлить к ([\\d]+) на ([\\d]+)";

    //Список id чатов волонтеров для администрирования
//    @Value("${telegram.volunteer.chat.id}")
//    private Long VOLUNTEER_CHAT_ID;

    /**
     * <i>Метод для получения инструкции по использованию команд администратора.
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/infoAboutAdminCat")
    public SendMessage handleInfoAboutAdminCat(Message message) {
        //   if (VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил инструкцию по использованию бота", idAdmin);
        return new SendMessage(idAdmin, ADMIN_COMMAND);

    }

    /**
     * <i>Метод для записи кошек в базу данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = SAVE_CAT_PATTERN)
    public SendMessage handleCreateCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} сохраняет кошку в базу данных приюта для кошек", idAdmin);
        String answer = validateAdoptedCatService.validateAddCat(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для удаления кошек из базы данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_CAT_PATTERN)
    public SendMessage handleDeleteCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} удаляет кошку из базы данных приюта для кошек", idAdmin);
        String answer = validateAdoptedCatService.validateDeleteCat(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для поиска кошки в базе данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = FIND_CAT_PATTERN)
    public SendMessage handleGetCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} выполняет поиск кошки в базе данных приюта для кошек", idAdmin);
        String answer = validateAdoptedCatService.validateGetCat(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для поиска кошки в базе данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = EDIT_CAT_PATTERN)
    public SendMessage handleEditCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} выполняет изменение кошки в базе данных приюта для кошек", idAdmin);
        String answer = validateAdoptedCatService.validateEditCat(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для получения списка всех кошек в базе данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllCat")
    public List<SendMessage> handleGetAllCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех кошек в базе данных приюта для кошек", idAdmin);
        List<AdoptedCat> answer = adoptedCatService.getAllCat();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }

    /**
     * <i>Метод для получения списка свободных кошек в базе данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllFreeCat")
    public List<SendMessage> handleGetAllFreeCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех свободных кошек в базе данных приюта для кошек", idAdmin);
        List<AdoptedCat> answer = adoptedCatService.getAllFreeCat();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }

    /**
     * <i>Метод для получения списка усыновленных кошек в базе данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllBusyCat")
    public List<SendMessage> handleGetAllBusyCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех усыновленных кошек в базе данных приюта для кошек", idAdmin);
        List<AdoptedCat> answer = adoptedCatService.getAllBusyCat();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }

    /**
     * <i>Метод для получения списка усыновленных кошек c оконченым испытательным сроком в базе данных приюта для
     * кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/getAllCatWithEndPeriod")
    public List<SendMessage> handleGetAllCatWithEndPeriod(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех усыновленных кошек с окончанием испытательного срока в базе данных приюта для кошек", idAdmin);
        List<AdoptedCat> answer = adoptedCatService.getAllCatWithEndPeriod();
        return answer.stream()
                .map(s -> new SendMessage(idAdmin, s.toString()))
                .collect(Collectors.toList());
    }

    /**
     * <i>Метод для назначения усыновителя кошке в базе данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = TAKE_CAT_PATTERN)
    public SendMessage handleTakeCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} добавляет усыновителя у кошки в базе данных приюта для кошек", idAdmin);
        String answer = validateAdoptedCatService.validateTakeCat(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для удаления усыновителя кошке в базе данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = RETURN_CAT_PATTERN)
    public SendMessage handleReturnCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} удаляет усыновителя у кошки в базе данных приюта для кошек", idAdmin);
        String answer = validateAdoptedCatService.validateReturnCat(message);
        return new SendMessage(idAdmin, answer);
    }

    /**
     * <i>Метод для продления периода адаптации для кошки в базе данных приюта для кошек администратором
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = EXTEND_CAT_PATTERN)
    public SendMessage handleExtendCat(Message message) {
        //  if(VOLUNTEER_CHAT_ID == message.from().id())
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} продлевает срок адаптации для усыновителя в базе данных приюта для кошек", idAdmin);
        String answer = validateAdoptedCatService.validateExtendCat(message);
        return new SendMessage(idAdmin, answer);
    }
}
