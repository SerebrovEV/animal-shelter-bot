package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.controllers.DogUserController;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений с контроллеров
 * {@link DogUserController} и {@link com.animalshelter.animalshelterbot.controllers.AdminDogUserController} из телеграма
 * и подготовки ответного сообщения пользователю</i>
 */

@Service
@RequiredArgsConstructor
public class ValidatorDogUserService {

    private final DogUserService dogUserService;
    private final Pattern ADD_PATTERN = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");
   // private final Pattern ADD_PATTERN_FROM_ADMIN = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");
    private final Pattern FIND_PATTERN = Pattern.compile("([\\W]{5})(\\s)([\\d]+)");
    private final Pattern EDIT_PATTERN = Pattern.compile("([\\W]{8})(\\s)([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)");
    private final Pattern DELETE_PATTERN = Pattern.compile("([\\W]{7})(\\s)([\\d]+)");

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
            if (dogUserService.getDogUserByChatId(chatId) == null) {
                DogUser dogUser = dogUserService.addDogUser(new DogUser(name, phone, chatId));
                return "Добавлена запись контакта: " + dogUser.toStringUser();
            }
            return "Данный пользователь уже есть";
        }
        return "Некорректный запрос";
    }

//    /**
//     * <i>Метод для проверки входящего сообщения от пользователя для проверки контакта.
//     * <br>
//     * Запрос выполняется через метод {@link DogUserController#getContactMessage(Message)}.</i>
//     *
//     * @param message
//     * @return String в зависимости от проверки сообщения
//     */
//    public String validateGetUser(Message message) {
//        DogUser dogUser = dogUserService.getDogUserByChatId(message.from().id());
//        if (dogUser != null) {
//            return dogUser.toStringUser();
//        }
//        return "Клиент не найден! Пожалуйста добавьте контакты для обратной связи или" +
//                " запросите вызов волонтера. Спасибо!";
//    }

    // Ниже в работе!!!!

    /**
     * <i> Метод для проверки и обработки входящего сообщения на сохранение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.AdminDogUserController#createBotUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateUserFromAdmin(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long phone = Long.valueOf(matcher.group(1));
            if (dogUserService.getDogUserByPhoneNumber(phone) == null) {
                DogUser dogUser = dogUserService.addDogUser(new DogUser(name, phone));
                return "Добавлена запись контакта: " + dogUser.toString();
            }
            return "Данный усыновитель уже есть";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на получение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.AdminDogUserController#getBotUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateGetUserFromAdmin(Message message) {
        Matcher matcher = FIND_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(3));
            Optional<DogUser> findBotUser = dogUserService.getDogUser(id);
            if (findBotUser.isEmpty()) {
                return "Усыновитель не найден, проверти правильность введения id.";
            }
            return findBotUser.get().toString();
        }
        return "Некорректный запрос";

    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на удаление контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.AdminDogUserController#deleteBotUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateDeleteUser(Message message) {
        Matcher matcher = DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(3));
            Optional<DogUser> deleteBotUser = dogUserService.getDogUser(id);
            if (deleteBotUser.isEmpty()) {
                return "Усыновитель не найден, проверти правильность введения id.";
            }
            dogUserService.deleteDogUser(id);
            return deleteBotUser.get() + "удален";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на изменение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.AdminDogUserController#editBotUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateEditUser(Message message) {
        Matcher matcher = EDIT_PATTERN.matcher(message.text());
        if (matcher.find()) {
            if (!matcher.group(5).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long id = Long.valueOf(matcher.group(3));
            Optional<DogUser> editBotUser = dogUserService.getDogUser(id);
            if (editBotUser.isEmpty()) {
                return "Усыновитель не найден, проверти правильность введения id.";
            }
            DogUser newDogUser = editBotUser.get();
            newDogUser.setUserName(matcher.group(7));
            newDogUser.setPhoneNumber(Long.parseLong(matcher.group(5)));
            dogUserService.editDogUser(newDogUser);
            return editBotUser.get() + " изменен";
        }
        return "Некорректный запрос";
    }


}
