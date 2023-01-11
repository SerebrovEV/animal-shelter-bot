package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений с контроллеров
 * {@link com.animalshelter.animalshelterbot.controllers.BotUserController} и {@link com.animalshelter.animalshelterbot.controllers.AdminBotController} из телеграма
 * и подготовки ответного сообщения пользователю</i>
 */

@Service
@RequiredArgsConstructor
public class ValidatorUserService {

    private final BotUserService botUserService;
    private final Pattern ADD_PATTERN_FROM_USER = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");
    private final Pattern ADD_PATTERN_FROM_ADMIN = Pattern.compile("([\\W]{9})(\\s)([\\d]{11})(\\s)([\\W]+)");
    private final Pattern FIND_PATTERN = Pattern.compile("([\\W]{5})(\\s)([\\d]+)");
    private final Pattern EDIT_PATTERN = Pattern.compile("([\\W]{8})(\\s)([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)");
    private final Pattern DELETE_PATTERN = Pattern.compile("([\\W]{7})(\\s)([\\d]+)");

    /**
     * <i> Метод для проверки и обработки входящего сообщения от пользователя.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.BotUserController#addBotUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateUser(Message message) {
        Matcher matcher = ADD_PATTERN_FROM_USER.matcher(message.text());
        matcher.find();
        String name = matcher.group(3);
        if (!matcher.group(1).startsWith("8")) {
            return "Некорректный номер телефона";
        }
        Long phone = Long.valueOf(matcher.group(1));
        Long chatId = message.from().id();
        if (botUserService.getBotUserByChatId(chatId) == null) {
            BotUser botUser = botUserService.addBotUser(new BotUser(name, phone, chatId));
            return "Добавлена запись контакта: " + botUser.toStringUser();
        }
        return "Данный пользователь уже есть";
    }

    /**
     * <i>Метод для проверки входящего сообщения от пользователя для проверки контакта.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.BotUserController#getContactMessage(Message)}.</i>
     *
     * @param message
     * @return String в зависимости от проверки сообщения
     */
    public String validateGetUser(Message message) {
        BotUser botUser = botUserService.getBotUserByChatId(message.from().id());
        if (botUser != null) {
            return botUser.toStringUser();
        }
        return "Клиент не найден! Пожалуйста добавьте контакты для обратной связи или" +
                " запросите вызов волонтера. Спасибо!";
    }

    public String validateGetUser(CallbackQuery callbackQuery) {
        BotUser botUser = botUserService.getBotUserByChatId(callbackQuery.from().id());
        if (botUser != null) {
            return botUser.toStringUser();
        }
        return "Клиент не найден! Пожалуйста добавьте контакты для обратной связи или" +
                " запросите вызов волонтера. Спасибо!";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на сохранение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.AdminBotController#createBotUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateUserFromAdmin(Message message) {
        Matcher matcher = ADD_PATTERN_FROM_ADMIN.matcher(message.text());
        matcher.find();
        String name = matcher.group(5);
        if (!matcher.group(3).startsWith("8")) {
            return "Некорректный номер телефона";
        }
        Long phone = Long.valueOf(matcher.group(3));
        if (botUserService.getByPhoneNumber(phone) == null) {
            BotUser botUser = botUserService.addBotUser(new BotUser(name, phone));
            return "Добавлена запись контакта: " + botUser.toString();
        }
        return "Данный усыновитель уже есть";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на получение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.AdminBotController#getBotUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateGetUserFromAdmin(Message message) {
        Matcher matcher = FIND_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(3));
            Optional<BotUser> findBotUser = botUserService.getBotUser(id);
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
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.AdminBotController#deleteBotUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateDeleteUser(Message message) {
        Matcher matcher = DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(3));
            Optional<BotUser> deleteBotUser = botUserService.getBotUser(id);
            if (deleteBotUser.isEmpty()) {
                return "Усыновитель не найден, проверти правильность введения id.";
            }
            botUserService.deleteBotUser(id);
            return deleteBotUser.get() + "удален";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на изменение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.AdminBotController#editBotUser(Message)}. </i>
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
            Optional<BotUser> editBotUser = botUserService.getBotUser(id);
            if (editBotUser.isEmpty()) {
                return "Усыновитель не найден, проверти правильность введения id.";
            }
            BotUser newBotUser = editBotUser.get();
            newBotUser.setUserName(matcher.group(7));
            newBotUser.setPhoneNumber(Long.parseLong(matcher.group(5)));
            botUserService.editBotUser(newBotUser);
            return editBotUser.get() + " изменен";
        }
        return "Некорректный запрос";
    }


}
