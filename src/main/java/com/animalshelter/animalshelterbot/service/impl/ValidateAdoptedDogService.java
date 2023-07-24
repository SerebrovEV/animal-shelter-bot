package com.animalshelter.animalshelterbot.service.impl;

import com.animalshelter.animalshelterbot.controller.AdminDogController;
import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.service.PetService;
import com.animalshelter.animalshelterbot.service.UserService;
import com.animalshelter.animalshelterbot.service.ValidatePetService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений от контроллера
 * {@link AdminDogController }
 * и подготовки ответного сообщения пользователю</i>
 */
@Service
public class ValidateAdoptedDogService implements ValidatePetService {
    private final PetService petService;
    private final UserService userService;
    private final TelegramBot telegramBot;


    private static final String dogButtonText = "Вернуться";

    public ValidateAdoptedDogService(@Qualifier("adoptedDogService") PetService petService,
                                     @Qualifier("dogUserService") UserService userService,
                                     TelegramBot telegramBot) {
        this.petService = petService;
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на сохранение данных о собаке от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogController#handleCreateDog(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateAddPet(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(5);
            AdoptedDog dog =(AdoptedDog) petService.addPet(new AdoptedDog(name));
            return "Добавлена запись собаки в базу данных приюта для собак: " + dog.getDogName();
        }
        return "Некорректный запрос";
    }
    /**
     * <i> Метод для проверки и обработки входящего сообщения на удаление данных о собаке от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogController#handleDeleteDog(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateDeletePet(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long dogId = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> deleteDog = petService.getPet(dogId);
            if (deleteDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            petService.deletePet(dogId);
            return deleteDog.get() + " удалена из базы данных приюта для собак.";
        }
        return "Некорректный запрос";
    }
    /**
     * <i> Метод для проверки и обработки входящего сообщения на поиск данных о собаке от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogController#handleGetDog(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateGetPet(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> findDog = petService.getPet(id);
            if (findDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            return findDog.get() + " из базы данных приюта для собак.";
        }
        return "Некорректный запрос";
    }
    /**
     * <i> Метод для проверки и обработки входящего сообщения на изменение данных о собаке от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogController#handleEditDog(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateEditPet(Message message) {
        Matcher matcher = EDIT_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> editDog = petService.getPet(id);
            if (editDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            AdoptedDog newDog = editDog.get();
            newDog.setDogName(matcher.group(3));
            petService.editPet(newDog);
            return editDog.get() + " изменен в базе данных приюта для собак.";
        }
        return "Некорректный запрос";
    }
    /**
     * <i> Метод для проверки и обработки входящего сообщения на усыновление собаки от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogController#handleTakeDog(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateTakePet(Message message) {
        Matcher matcher = TAKE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idDog = Long.valueOf(matcher.group(3));
            Optional<AdoptedDog> editDog = petService.getPet(idDog);
            if (editDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            Long idUser = Long.valueOf(matcher.group(7));
            Optional<DogUser> dogUser = userService.getUser(idUser);
            if (dogUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
            }
            AdoptedDog newDog = editDog.get();
            newDog.setDogUser(dogUser.get());
            newDog.setAdoptionDate(Date.valueOf(LocalDate.now()));
            newDog.setTrialPeriod(30);
            petService.editPet(newDog);
            return newDog.toString();
        }
        return "Некорректный запрос";
    }
    /**
     * <i> Метод для проверки и обработки входящего сообщения на возврат собаки от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogController#handleReturnDog(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateReturnPet(Message message) {
        Matcher matcher = RETURN_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idDog = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> editDog = petService.getPet(idDog);
            if (editDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            AdoptedDog newDog = editDog.get();
            newDog.setDogUser(null);
            newDog.setAdoptionDate(null);
            newDog.setTrialPeriod(30);
            petService.editPet(newDog);
            return newDog + " изменен в базе данных приюта для собак.";
        }
        return "Некорректный запрос";
    }
    /**
     * <i> Метод для проверки и обработки входящего сообщения на продление испытательного срока от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogController#handleExtendCDog(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateExtendPet(Message message) {
        Matcher matcher = EXTEND_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idDog = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> editDog = petService.getPet(idDog);
            if (editDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            AdoptedDog newDog = editDog.get();
            Long chatIdUser = newDog.getDogUser().getChatId();

            if (chatIdUser == null) {
                return "Продление не выполнено! Для корректной работы необходимо попросить усыновителя добавить" +
                        " контактные данные через телеграм-бота прописав сообщение:\n Взял собаку 89817885244 Иван";
            }
            int newPeriod = Integer.parseInt(matcher.group(5));
            if (newPeriod == 14 || newPeriod == 30) {
                newDog.setTrialPeriod(editDog.get().getTrialPeriod()+ newPeriod);
                petService.editPet(newDog);
                telegramBot.execute(new SendMessage(chatIdUser, ATTENTION_MESSAGE + newPeriod)
                        .replyMarkup(new InlineKeyboardMarkup(
                                new InlineKeyboardButton(dogButtonText).callbackData(Callbacks.DOG_MENU.name())
                        )));
                return newDog + " изменен в базе данных приюта для собак.";
            }
            return "Некорректный запрос на добавление дней к периоду адаптации, можно добавить либо 14, либо 30 дней.";
        }
        return "Некорректный запрос";
    }

}
