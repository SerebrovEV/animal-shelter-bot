package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.service.PetService;
import com.animalshelter.animalshelterbot.service.ValidatePetService;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
/**
 * /**
 *  * Контроллер для добавления, редактирования, получения, удаления, назначения/удаления хозяина, продления периода адаптации или
 *  * получения всех AdoptedDog (собак), а также списка всех не усыновленных собак, питомцев
 *  * находящихся у хозяев с испытательном сроком и списка собак, у которых период адаптации закончился в/из базы
 *  * данных приюта для собак администратором.
 *  * Обычный пользователь не имеет доступа к данным командам.
 *  */

@Component
public class AdminDogController implements CommandController {

    private final ValidatePetService validatePetService;
    private final PetService petService;
    private final Logger LOG = LoggerFactory.getLogger(AdminDogController.class);
    private final String ADMIN_COMMAND = "Правила по работе с БД приюта для собак: \n" +
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
            "/getAllDogOnTrialPeriod  - получить список всех собак на испытательном периоде;\n" +
            "/getAllDogWithEndPeriod - получить список всех собак с окончаниям испытательного срока.";

    private static final String SAVE_DOG_PATTERN = "Сохранить с ([\\W]+)";
    private static final String EDIT_DOG_PATTERN = "Изменить с ([\\d]+)(\\s)([\\W]+)";
    private static final String DELETE_DOG_PATTERN = "Удалить с ([\\d]+)";
    private static final String FIND_DOG_PATTERN = "Найти с ([\\d]+)";
    private static final String TAKE_DOG_PATTERN = "Усыновить ([\\d]+) с ([\\d]+)";
    private static final String RETURN_DOG_PATTERN = "Вернуть с ([\\d]+)";
    private static final String EXTEND_DOG_PATTERN = "Продлить с ([\\d]+) на ([\\d]+)";

    public AdminDogController(@Qualifier("validateAdoptedDogService") ValidatePetService validatePetService,
                              @Qualifier("adoptedCatService") PetService petService) {
        this.validatePetService = validatePetService;
        this.petService = petService;
    }

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
        LOG.info("Администратор {} запросил инструкцию по использованию бота", idAdmin);
        return new SendMessage(idAdmin, ADMIN_COMMAND);
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
        LOG.info("Администратор {} сохраняет собаку в базу данных приюта для собак", idAdmin);
        String answer = validatePetService.validateAddPet(message);
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
        LOG.warn("Администратор {} удаляет собаку из базы данных приюта для собак", idAdmin);
        String answer = validatePetService.validateDeletePet(message);
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
        LOG.info("Администратор {} выполняет поиск собаки в базе данных приюта для собак", idAdmin);
        String answer = validatePetService.validateGetPet(message);
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
        LOG.warn("Администратор {} выполняет изменение собаки в базе данных приюта для собак", idAdmin);
        String answer = validatePetService.validateEditPet(message);
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
        LOG.info("Администратор {} запросил всех собак в базе данных приюта для собак", idAdmin);
        List<AdoptedDog> answer = petService.getAllPet();
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
        LOG.info("Администратор {} запросил всех свободных собак в базе данных приюта для собак", idAdmin);
        List<AdoptedDog> answer = petService.getAllFreePet();
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
        LOG.info("Администратор {} запросил всех усыновленных собак в базе данных приюта для собак", idAdmin);
        List<AdoptedDog> answer = petService.getAllBusyPet();
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
        LOG.info("Администратор {} запросил всех усыновленных собак с окончанием испытательного срока в базе данных приюта для собак", idAdmin);
        List<AdoptedDog> answer = petService.getAllPetWithEndPeriod();
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
        LOG.info("Администратор {} добавляет усыновителя у собаки в базе данных приюта для собак", idAdmin);
        String answer = validatePetService.validateTakePet(message);
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
        LOG.info("Администратор {} удаляет усыновителя у собаки в базе данных приюта для собак", idAdmin);
        String answer = validatePetService.validateReturnPet(message);
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
        LOG.info("Администратор {} продлевает испытательный срок для усыновителя в базе данных приюта для собак", idAdmin);
        String answer = validatePetService.validateExtendPet(message);
        return new SendMessage(idAdmin, answer);
    }

}
