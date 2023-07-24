package com.animalshelter.animalshelterbot.service.impl;


import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.controller.CatUserController;
import com.animalshelter.animalshelterbot.controller.AdminCatUserController;
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
 * {@link CatUserController} и {@link AdminCatUserController}
 * и подготовки ответного сообщения пользователю</i>
 */

@Service
public class ValidateCatUserService implements ValidateUserService {

    private final UserService userService;
    private final TelegramBot telegramBot;

    private static final String catButtonText = "Вернуться";

    public ValidateCatUserService(@Qualifier("catUserService") UserService userService,
                                  TelegramBot telegramBot) {
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения от усыновителя.
     * <br>
     * Запрос выполняется через метод {@link CatUserController#handleAddCatUser(Message)}. </i>
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
                CatUser catUser = (CatUser) userService.addUser(new CatUser(name, phone, chatId));
                return "Добавлена запись контакта: " + catUser.toStringUser();
            }
            return "Данный пользователь уже есть, свяжитесь с волонтером для уточнения информации";
        }
        return "Некорректный запрос";
    }


    /**
     * <i> Метод для проверки и обработки входящего сообщения на добавление Id chat от усыновителя.
     * <br>
     * Запрос выполняется через метод {@link CatUserController#handleAddCatUserIDChat(Message)}. </i>
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

            Optional<CatUser> catUser = userService.getUserByPhoneNumber(phone);
            if (catUser.isEmpty()) {
                return "Телефон не найден, свяжитесь с волонтером для уточнения информации";
            }

            CatUser newCatUser = catUser.get();
            newCatUser.setChatId(chatId);
            userService.editUser(newCatUser);
            return "Обновлена запись контакта: " + newCatUser.toStringUser() + ". Спасибо!";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на сохранение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatUserController#handleCreateCatUser(Message)}. </i>
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
                CatUser catUser = (CatUser) userService.addUser(new CatUser(name, phone));
                return "Добавлена запись контакта: " + catUser.toString() + " в базу данных приюта для кошек.";
            }
            return "Данный усыновитель уже есть";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на получение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatUserController#handleGetCatUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateGetUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<CatUser> findCatUser = userService.getUser(id);
            if (findCatUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            return findCatUser.get().toString() + " из базы данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на удаление контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatUserController#handleDeleteCatUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateDeleteUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<CatUser> deleteCatUser = userService.getUser(id);
            if (deleteCatUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            userService.deleteUser(id);
            return deleteCatUser.get() + " удален из базы данных приюта для кошек";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на изменение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatUserController#handleEditCatUser(Message)}. </i>
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
            Optional<CatUser> editCatUser = userService.getUser(id);
            if (editCatUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            CatUser newCatUser = editCatUser.get();
            newCatUser.setUserName(matcher.group(5));
            newCatUser.setPhoneNumber(Long.parseLong(matcher.group(3)));
            userService.editUser(newCatUser);
            return editCatUser.get() + " изменен в базе данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на отправку поздравления усыновителю от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatUserController#handleCongratulationCatUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateCongratulationUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<CatUser> findCatUser = userService.getUser(id);
            if (findCatUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            Long chatIdUser = findCatUser.get().getChatId();
            if (chatIdUser == null) {
                return "Команда не выполнена! Для корректной работы необходимо попросить усыновителя добавить" +
                        " контактные данные через телеграм-бота прописав сообщение:\n Взял кота 89817885244 Иван";
            }
            telegramBot.execute(new SendMessage(chatIdUser, CONGRATULATION_MESSAGE));
            return findCatUser.get() + " направлено поздравление.";
        }
        return "Некорректный запрос";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на отправку условий возврата усыновителю от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatUserController#handleCongratulationCatUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    @Override
    public String validateReturnUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<CatUser> findCatUser = userService.getUser(id);
            if (findCatUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            Long chatIdUser = findCatUser.get().getChatId();
            if (chatIdUser == null) {
                return "Команда не выполнена! Для корректной работы необходимо попросить усыновителя добавить" +
                        " контактные данные через телеграм-бота прописав сообщение:\n Взял кота 89817885244 Иван";
            }
            telegramBot.execute(new SendMessage(chatIdUser, RETURN_MESSAGE)
                    .replyMarkup(new InlineKeyboardMarkup(
                            new InlineKeyboardButton(catButtonText).callbackData(Callbacks.CAT_MENU.name())
                    )));
            return findCatUser.get() + " направлено уведомление.";
        }
        return "Некорректный запрос";
    }
}
