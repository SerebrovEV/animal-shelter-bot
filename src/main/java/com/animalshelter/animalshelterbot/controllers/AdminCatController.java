package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.service.CatService;
import com.animalshelter.animalshelterbot.service.ValidatorCatUserService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCatController implements CommandController {

    private final CatService catService;
    private final Logger LOG = LoggerFactory.getLogger(AdminCatUserController.class);
    private final String ADMIN_COMMAND = "Правила работы с кошками: \n" +
            "/infoAboutAdminCat - команды для использования;\n" +
            "Сохранить К Мурзик - добавить кошку;\n" +
            "Найти К 10 - найти кошку с id = 10;\n" +
            "Изменить К 10 Мила - изменить кошку с id = 10;\n" +
            "Удалить К 10 - удалить кошку с id = 10;\n" +
            "/getAllCat - получить список всех кошек;\n" +
            "/getAllFreeCat - получить список всех кошек в приюте;\n" +
            "/getAllBusyCat  - получить всех кошек на испытательном периоде;\n" +
            "/getAllCat  - получить всех кошек";

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
     //   LOG.info("Администратор {} сохраняет контакт усыновителя в базу данных", idAdmin);
        AdoptedCat newCat = new AdoptedCat();
        String answer = catService.addAdoptedCat(newCat).toString();
        return new SendMessage(idAdmin, answer);
    }

    @Command(pattern = SAVE_CAT_PATTERN)
    public SendMessage handleDeleteCat(Message message) {
        //  if(ADMIN_ID_CHAT.contains(message.from().id()))
        Long idAdmin = message.from().id();
      //  LOG.info("Администратор {} удаляет контакт усыновителя в базу данных", idAdmin);
        AdoptedCat newCat = new AdoptedCat();
        String answer = catService.addAdoptedCat(newCat).toString();
        return new SendMessage(idAdmin, answer);
    }
}
