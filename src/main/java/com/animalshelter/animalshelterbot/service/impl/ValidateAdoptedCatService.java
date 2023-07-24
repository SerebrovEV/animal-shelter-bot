package com.animalshelter.animalshelterbot.service.impl;

import com.animalshelter.animalshelterbot.controller.AdminCatController;
import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.service.PetService;
import com.animalshelter.animalshelterbot.service.UserService;
import com.animalshelter.animalshelterbot.service.ValidatePetService;
import com.animalshelter.animalshelterbot.service.impl.CatUserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений от контроллера
 * {@link AdminCatController}
 * и подготовки ответного сообщения пользователю</i>
 */
@Service
public class ValidateAdoptedCatService implements ValidatePetService {
    private final PetService petService;
    private final UserService userService;
    private final TelegramBot telegramBot;

    private static final String catButtonText = "Вернуться";

    public ValidateAdoptedCatService(@Qualifier("adoptedCatService") PetService petService,
                                     @Qualifier("catUserService") UserService userService,
                                     TelegramBot telegramBot) {
        this.petService = petService;
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на сохранение кошек от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleCreateCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateAddPet(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(5);
            AdoptedCat cat =(AdoptedCat) petService.addPet(new AdoptedCat(name));
            return "Добавлена запись кота в базу данных приюта для кошек: " + cat.getCatName();
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на удаление кошек от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleDeleteCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateDeletePet(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idCat = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> deleteCat = petService.getPet(idCat);
            if (deleteCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            petService.deletePet(idCat);
            return deleteCat.get() + " удалена из базы данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на поиск кошек от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleGetCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateGetPet(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> findCat = petService.getPet(id);
            if (findCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            return findCat.get() + " из базы данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на изменение кошек от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleEditCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateEditPet(Message message) {
        Matcher matcher = EDIT_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> editCat = petService.getPet(id);
            if (editCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            AdoptedCat newCat = editCat.get();
            newCat.setCatName(matcher.group(3));
            petService.editPet(newCat);
            return editCat.get() + " изменен в базе данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на усыновление кошки от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleTakeCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateTakePet(Message message) {
        Matcher matcher = TAKE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idCat = Long.valueOf(matcher.group(3));
            Optional<AdoptedCat> editCat = petService.getPet(idCat);
            if (editCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            Long idUser = Long.valueOf(matcher.group(7));
            Optional<CatUser> catUser = userService.getUser(idUser);
            if (catUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            AdoptedCat newCat = editCat.get();
            newCat.setCatUser(catUser.get());
            newCat.setAdoptionDate(Date.valueOf(LocalDate.now()));
            newCat.setTrialPeriod(30);
            petService.editPet(newCat);
            return newCat.toString();
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на возврат кошки от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleReturnCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateReturnPet(Message message) {
        Matcher matcher = RETURN_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idCat = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> editCat = petService.getPet(idCat);
            if (editCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            AdoptedCat newCat = editCat.get();
            newCat.setCatUser(null);
            newCat.setAdoptionDate(null);
            newCat.setTrialPeriod(30);
            petService.editPet(newCat);
            return newCat + " изменен в базе данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на продления периода адаптации кошки от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatController#handleExtendCat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateExtendPet(Message message) {
        Matcher matcher = EXTEND_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idCat = Long.valueOf(matcher.group(1));
            Optional<AdoptedCat> editCat = petService.getPet(idCat);
            if (editCat.isEmpty()) {
                return "Кошка не найдена в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            AdoptedCat newCat = editCat.get();
            Long chatIdUser = newCat.getCatUser().getChatId();

            if (chatIdUser == null) {
                return "Продление не выполнено! Для корректной работы необходимо попросить усыновителя добавить" +
                        " контактные данные через телеграм-бота прописав сообщение:\n Взял кота 89817885244 Иван";
            }

            int newPeriod = Integer.parseInt(matcher.group(5));

            if (newPeriod == 14 || newPeriod == 30) {
                newCat.setTrialPeriod(editCat.get().getTrialPeriod() + newPeriod);
                petService.editPet(newCat);
                telegramBot.execute(new SendMessage(chatIdUser, ATTENTION_MESSAGE + newPeriod)
                        .replyMarkup(new InlineKeyboardMarkup(
                                new InlineKeyboardButton(catButtonText).callbackData(Callbacks.CAT_MENU.name())
                        )));
                return newCat + " изменен в базе данных приюта для кошек.";
            }

            return "Некорректный запрос на добавление дней к периоду адаптации, можно добавить либо 14, либо 30 дней.";
        }
        return "Некорректный запрос";
    }
}
