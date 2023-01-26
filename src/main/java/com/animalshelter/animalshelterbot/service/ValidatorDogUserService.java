package com.animalshelter.animalshelterbot.service;
import com.animalshelter.animalshelterbot.controller.DogUserController;
import com.animalshelter.animalshelterbot.controller.AdminDogUserController;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений с контроллеров
 * {@link DogUserController} и {@link AdminDogUserController}
 * и подготовки ответного сообщения пользователю</i>
 */

@Service
@RequiredArgsConstructor
public class ValidatorDogUserService {

    private final DogUserService dogUserService;
    private final TelegramBot telegramBot;
    private final Pattern ADD_PATTERN = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");
    private final Pattern NUMBER_PATTERN = Pattern.compile("([\\d]+)");
    private final Pattern EDIT_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)");

    private final String CONGRATULATION_MESSAGE = "Поздравляем, вы прошли испытательный срок. Продолжайте и" +
            " впредь заботится о своем новом любимце и он ответит вам любовью ответ:)";

    private final String RETURN_MESSAGE = "К сожалению, вы не прошли испытательный срок. Вам требуется вернуть животное " +
            "в приют. Если вы не можете к нам приехать, мы можем направить к вам волонтера для возврата животного. Для " +
            "этого свяжитесь с нами.";

    private static final String dogButtonText = "Вернуться";
    /**
     * <i> Метод для проверки и обработки входящего сообщения от пользователя.
     * <br>
     * Запрос выполняется через метод {@link DogUserController#handleAddDogUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateDogUser(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long phone = Long.valueOf(matcher.group(1));
            Long chatId = message.from().id();
            if (dogUserService.getDogUserByChatId(chatId).isEmpty() && dogUserService.getDogUserByPhoneNumber(phone).isEmpty()) {
                DogUser dogUser = dogUserService.addDogUser(new DogUser(name, phone, chatId));
                return "Добавлена запись контакта: " + dogUser.toStringUser();
            }
            return "Данный пользователь уже есть, свяжитесь с волонтером для уточнения информации";
        }
        return "Некорректный запрос";
    }
    /**
     * <i> Метод для проверки и обработки входящего сообщения на добавление Id chat от усыновителя.
     * <br>
     * Запрос выполняется через метод {@link DogUserController#handleAddDogUserIDChat(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateDogUserIdChat(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }

            Long phone = Long.valueOf(matcher.group(1));
            Long chatId = message.from().id();

            if (dogUserService.getDogUserByChatId(chatId).isPresent()) {
                return "Обновить запись не удалось, свяжитесь с волонтером для уточнения информации";
            }

            Optional<DogUser> dogUser = dogUserService.getDogUserByPhoneNumber(phone);
            if (dogUser.isEmpty()) {
                return "Телефон не найден, свяжитесь с волонтером для уточнения информации";
            }

            DogUser newDogUser = dogUser.get();
            newDogUser.setChatId(chatId);
            dogUserService.editDogUser(newDogUser);
            return "Обновлена запись контакта: " + newDogUser.toStringUser() + ". Спасибо!";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на сохранение контактных данных в БД приюта для собак от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogUserController#handleCreateDogUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateDogUserFromAdmin(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long phone = Long.valueOf(matcher.group(1));
            if (dogUserService.getDogUserByPhoneNumber(phone).isEmpty()) {
                DogUser dogUser = dogUserService.addDogUser(new DogUser(name, phone));
                return "Добавлена запись контакта: " + dogUser.toString() + " в базу данных приюта для собак";
            }
            return "Данный усыновитель уже есть в базе данных приюта для собак";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на получение контактных данных из БД приюта собак от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogUserController#handleGetDogUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateGetDogUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> findBotUser = dogUserService.getDogUser(id);
            if (findBotUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
            }
            return findBotUser.get().toString() + " из базы данных приюта для собак.";
        }
        return "Некорректный запрос";

    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на удаление контактных данных в БД приюта собак от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogUserController#handleDeleteDogUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateDeleteDogUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> deleteBotUser = dogUserService.getDogUser(id);
            if (deleteBotUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
            }
            dogUserService.deleteDogUser(id);
            return deleteBotUser.get() + "удален из базы данных приюта для собак.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на изменение контактных данных в БД приюта собак от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogUserController#handleEditDogUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateEditDogUserFromAdmin(Message message) {
        Matcher matcher = EDIT_PATTERN.matcher(message.text());
        if (matcher.find()) {
            if (!matcher.group(3).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> editDogUser = dogUserService.getDogUser(id);
            if (editDogUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
            }
            DogUser newDogUser = editDogUser.get();
            newDogUser.setUserName(matcher.group(5));
            newDogUser.setPhoneNumber(Long.parseLong(matcher.group(3)));
            dogUserService.editDogUser(newDogUser);
            return editDogUser.get() + " изменен в базе данных приюта для собак.";
        }
        return "Некорректный запрос";
    }
    /**
     * <i> Метод для проверки и обработки входящего сообщения на отправку поздравления усыновителю от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogUserController#handleCongratulationDogUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateCongratulationDogUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> findDogUser = dogUserService.getDogUser(id);
            if (findDogUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
            }
            Long chatIdUser = findDogUser.get().getChatId();
            if (chatIdUser == null) {
                return "Команда не выполнена! Для корректной работы необходимо попросить усыновителя добавить" +
                        " контактные данные через телеграм-бота прописав сообщение:\n Взял собаку 89817885244 Иван";
            }
            telegramBot.execute(new SendMessage(chatIdUser, CONGRATULATION_MESSAGE));
            return findDogUser.get() + " направлено поздравление.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на отправку условий возврата усыновителю от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminDogUserController#handleReturnDogUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateReturnDogUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> findDogUser = dogUserService.getDogUser(id);
            if (findDogUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
            }
            Long chatIdUser = findDogUser.get().getChatId();
            if (chatIdUser == null) {
                return "Команда не выполнена! Для корректной работы необходимо попросить усыновителя добавить" +
                        " контактные данные через телеграм-бота прописав сообщение:\n Взял собаку 89817885244 Иван";
            }
            telegramBot.execute(new SendMessage(chatIdUser, RETURN_MESSAGE)
                    .replyMarkup(new InlineKeyboardMarkup(
                            new InlineKeyboardButton(dogButtonText).callbackData(Callbacks.DOG_MENU.name())
                    )));
            return findDogUser.get() + " направлено уведомление.";
        }
        return "Некорректный запрос";
    }


}
