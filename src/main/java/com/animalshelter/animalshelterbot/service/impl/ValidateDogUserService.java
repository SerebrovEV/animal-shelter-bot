package com.animalshelter.animalshelterbot.service.impl;

import com.animalshelter.animalshelterbot.controller.DogUserController;
import com.animalshelter.animalshelterbot.controller.AdminDogUserController;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.service.UserService;
import com.animalshelter.animalshelterbot.service.ValidateUserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ValidateDogUserService implements ValidateUserService {

    private final UserService userService;
    private final TelegramBot telegramBot;

    private static final String dogButtonText = "Вернуться";

    public ValidateDogUserService(@Qualifier("dogUserService") UserService userService,
                                  TelegramBot telegramBot) {
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения от пользователя.
     * <br>
     * Запрос выполняется через метод {@link DogUserController#handleAddDogUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateUser(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long phone = Long.valueOf(matcher.group(1));
            Long chatId = message.from().id();
            if (userService.getUserByChatId(chatId).isEmpty() && userService.getUserByPhoneNumber(phone).isEmpty()) {
                DogUser dogUser = (DogUser) userService.addUser(new DogUser(name, phone, chatId));
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
    @Override
    public String validateUserIdChat(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }

            Long phone = Long.valueOf(matcher.group(1));
            Long chatId = message.from().id();

            if (userService.getUserByChatId(chatId).isPresent()) {
                return "Обновить запись не удалось, свяжитесь с волонтером для уточнения информации";
            }

            Optional<DogUser> dogUser = userService.getUserByPhoneNumber(phone);
            if (dogUser.isEmpty()) {
                return "Телефон не найден, свяжитесь с волонтером для уточнения информации";
            }

            DogUser newDogUser = dogUser.get();
            newDogUser.setChatId(chatId);
            userService.editUser(newDogUser);
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
    @Override
    public String validateUserFromAdmin(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long phone = Long.valueOf(matcher.group(1));
            if (userService.getUserByPhoneNumber(phone).isEmpty()) {
                DogUser dogUser = (DogUser) userService.addUser(new DogUser(name, phone));
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
    @Override
    public String validateGetUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> findBotUser = userService.getUser(id);
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
    @Override
    public String validateDeleteUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> deleteBotUser = userService.getUser(id);
            if (deleteBotUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
            }
            userService.deleteUser(id);
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
    @Override
    public String validateEditUserFromAdmin(Message message) {
        Matcher matcher = EDIT_PATTERN.matcher(message.text());
        if (matcher.find()) {
            if (!matcher.group(3).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> editDogUser = userService.getUser(id);
            if (editDogUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для собак, проверьте правильность введения id.";
            }
            DogUser newDogUser = editDogUser.get();
            newDogUser.setUserName(matcher.group(5));
            newDogUser.setPhoneNumber(Long.parseLong(matcher.group(3)));
            userService.editUser(newDogUser);
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
    @Override
    public String validateCongratulationUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> findDogUser = userService.getUser(id);
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
    @Override
    public String validateReturnUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<DogUser> findDogUser = userService.getUser(id);
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
