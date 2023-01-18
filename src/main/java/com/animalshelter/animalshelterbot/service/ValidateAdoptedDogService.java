package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.controllers.AdminDogController;
import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений с контроллеров
 * {@link AdminDogController } и  из телеграма
 * и подготовки ответного сообщения пользователю</i>
 */
@Service
@RequiredArgsConstructor
public class ValidateAdoptedDogService {
    private final AdoptedDogService adoptedDogService;
    private final DogUserService dogUserService;
    private final TelegramBot telegramBot;
    private final Pattern ADD_PATTERN = Pattern.compile("([\\W]{9})(\\s)([\\W]{1})(\\s)([\\W]+)");
    private final Pattern FIND_AND_DELETE_PATTERN = Pattern.compile("([\\d]+)");

    private final Pattern EDIT_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\W]+)");
    private final Pattern TAKE_PATTERN = Pattern.compile("([\\W]{9})(\\s)([\\d]+)(\\s)([\\W]{1})(\\s)([\\d]+)");
    private final Pattern RETURN_PATTERN = Pattern.compile("([\\d]+)");
    private final Pattern EXTEND_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\W]{2})(\\s)([\\d]+)");

    private final String ATTENTION_MESSAGE = "Добрый день! Вам было назначено дополнительное время испытательного срока," +
            " новых дней: + ";

    private static final String dogButtonText = "Вернуться";

    /**
     * <i> Метод для проверки и обработки входящего сообщения на сохранение данных о собаке от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogController#handleCreateDog(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateAddDog(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(5);
            AdoptedDog dog = adoptedDogService.addAdoptedDog(new AdoptedDog(name));
            return "Добавлена запись собаки в базу данных приюта для кошек: " + dog.getDogName();
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
    public String validateDeleteDog(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long dogId = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> deleteDog = adoptedDogService.getAdoptedDog(dogId);
            if (deleteDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            adoptedDogService.deleteAdoptedDog(dogId);
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
    public String validateGetDog(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> findDog = adoptedDogService.getAdoptedDog(id);
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
    public String validateEditDog(Message message) {
        Matcher matcher = EDIT_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> editDog = adoptedDogService.getAdoptedDog(id);
            if (editDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            AdoptedDog newDog = editDog.get();
            newDog.setDogName(matcher.group(3));
            adoptedDogService.editAdoptedDog(newDog);
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
    public String validateTakeDog(Message message) {
        Matcher matcher = TAKE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idDog = Long.valueOf(matcher.group(3));
            Optional<AdoptedDog> editDog = adoptedDogService.getAdoptedDog(idDog);
            if (editDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            Long idUser = Long.valueOf(matcher.group(7));
            Optional<DogUser> dogUser = dogUserService.getDogUser(idUser);
            if (dogUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
            }
            AdoptedDog newDog = editDog.get();
            newDog.setDogUser(dogUser.get());
            newDog.setAdoptionDate(Date.valueOf(LocalDate.now()));
            newDog.setTrialPeriod(30);
            adoptedDogService.editAdoptedDog(newDog);
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
    public String validateReturnDog(Message message) {
        Matcher matcher = RETURN_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idDog = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> editDog = adoptedDogService.getAdoptedDog(idDog);
            if (editDog.isEmpty()) {
                return "Собака не найдена в базе данных приюта для собак, проверьте правильность введения id.";
            }
            AdoptedDog newDog = editDog.get();
            newDog.setDogUser(null);
            newDog.setAdoptionDate(null);
            newDog.setTrialPeriod(30);
            adoptedDogService.editAdoptedDog(newDog);
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
    public String validateExtendDog(Message message) {
        Matcher matcher = EXTEND_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long idDog = Long.valueOf(matcher.group(1));
            Optional<AdoptedDog> editDog = adoptedDogService.getAdoptedDog(idDog);
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
                adoptedDogService.editAdoptedDog(newDog);
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
