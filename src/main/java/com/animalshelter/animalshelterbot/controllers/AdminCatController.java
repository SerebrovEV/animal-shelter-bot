package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.service.AdoptedCatService;
import com.animalshelter.animalshelterbot.service.ValidateAdoptedCatService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCatController implements CommandController {

    private final AdoptedCatService adoptedCatService;
    private final ValidateAdoptedCatService validateAdoptedCatService;
    private final Logger LOG = LoggerFactory.getLogger(AdminCatUserController.class);
    private final String ADMIN_COMMAND = "Правила работы с кошками: \n" +
            "/infoAboutAdminCat - команды для использования;\n" +
            "Сохранить к Мурзик - добавить кошку;\n" +
            "Найти к 10 - найти кошку с id = 10;\n" +
            "Изменить к 10 Мила - изменить кошку с id = 10;\n" +
            "Удалить к 10 - удалить кошку с id = 10;\n" +
            "/getAllCat - получить список всех кошек;\n" +
            "/getAllFreeCat - получить список всех кошек в приюте;\n" +
            "/getAllBusyCat  - получить всех кошек на испытательном периоде.";

    private static final String SAVE_CAT_PATTERN = "Сохранить к ([\\W]+)";
    private static final String EDIT_CAT_PATTERN = "Изменить к ([\\d]+)(\\s)([\\W]+)";
    private static final String DELETE_CAT_PATTERN = "Удалить к ([\\d]+)";
    private static final String FIND_CAT_PATTERN = "Найти к ([\\d]+)";

    //Список id чатов волонтеров для администрирования
    // private final List<Long> ADMIN_ID_CHAT = List.of();

    /**
     * <i>Метод для получения инструкции по использованию команд администратора.
     * <br>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(name = "/infoAboutAdminCat")
    public SendMessage handleInfoAboutAdminCat(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.warn("Администратор {} запросил инструкцию по использованию бота", idAdmin);
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
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
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
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
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
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
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
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} выполняет изменение кошки в базе данных приюта для кошек", idAdmin);
        String answer = validateAdoptedCatService.validateEditCat(message);
        return new SendMessage(idAdmin, answer);
    }
    @Command(name = "/getAllCat")
    public SendMessage handleGetAllCat(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех кошек в базе данных приюта для кошек", idAdmin);
        String answer = adoptedCatService.getAllCat().toString();
        return new SendMessage(idAdmin, answer);
    }

    @Command(name = "/getAllFreeCat")
    public SendMessage handleGetAllFreeCat(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех свободных кошек в базе данных приюта для кошек", idAdmin);
       // String answer = catService.getAllFreeCat().toString();
        String answer = "Test";
        return new SendMessage(idAdmin, answer);
    }
    @Command(name = "/getAllBusyCat")
    public SendMessage handleGetAllBusyCat(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
        LOG.info("Администратор {} запросил всех усыновленных кошек в базе данных приюта для кошек", idAdmin);
        // String answer = catService.getAllBusyCat().toString();
        String answer = "Test";
        return new SendMessage(idAdmin, answer);
    }
}
