package com.animalshelter.animalshelterbot.service;


import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.controllers.CatUserController;
import com.animalshelter.animalshelterbot.controllers.AdminCatUserController;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений с контроллеров
 * {@link CatUserController} и  из телеграма
 * и подготовки ответного сообщения пользователю</i>
 */

@Service
@RequiredArgsConstructor
public class ValidatorCatUserService {

    private final CatUserService catUserService;
    private final Pattern ADD_PATTERN = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");
    private final Pattern FIND_AND_DELETE_PATTERN = Pattern.compile("([\\d]+)");
    private final Pattern EDIT_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)");

    /**
     * <i> Метод для проверки и обработки входящего сообщения от пользователя.
     * <br>
     * Запрос выполняется через метод {@link CatUserController#handleAddCatUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateCatUser(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long phone = Long.valueOf(matcher.group(1));
            Long chatId = message.from().id();
            if (catUserService.getCatUserByChatId(chatId) == null) {
                CatUser catUser = catUserService.addCatUser(new CatUser(name, phone, chatId));
                return "Добавлена запись контакта: " + catUser.toStringUser();
            }
            return "Данный пользователь уже есть, свяжитесь с волонтером для уточнения информации";
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


    /**
     * <i> Метод для проверки и обработки входящего сообщения на сохранение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatUserController#handleCreateCatUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateCatUserFromAdmin(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        matcher.find();
        String name = matcher.group(3);
        if (!matcher.group(1).startsWith("8")) {
            return "Некорректный номер телефона";
        }
        Long phone = Long.valueOf(matcher.group(1));
        if (catUserService.getCatUserByPhoneNumber(phone) == null) {
            CatUser catUser = catUserService.addCatUser(new CatUser(name, phone));
            return "Добавлена запись контакта: " + catUser.toString() + " в базу данных приюта для кошек.";
        }
        return "Данный усыновитель уже есть";
    }

    /**
     * <i> Метод для проверки и обработки входящего сообщения на получение контактных данных от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatUserController#handleGetCatUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateGetCatUserFromAdmin(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<CatUser> findCatUser = catUserService.getCatUser(id);
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
    public String validateDeleteCatUserFromAdmin(Message message) {
        Matcher matcher = FIND_AND_DELETE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<CatUser> deleteCatUser = catUserService.getCatUser(id);
            if (deleteCatUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            catUserService.deleteCatUser(id);
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
    public String validateEditCatUserFromAdmin(Message message) {
        Matcher matcher = EDIT_PATTERN.matcher(message.text());
        if (matcher.find()) {
            if (!matcher.group(3).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long id = Long.valueOf(matcher.group(1));
            Optional<CatUser> editCatUser = catUserService.getCatUser(id);
            if (editCatUser.isEmpty()) {
                return "Усыновитель не найден в базе данных приюта для кошек, проверьте правильность введения id.";
            }
            CatUser newCatUser = editCatUser.get();
            newCatUser.setUserName(matcher.group(5));
            newCatUser.setPhoneNumber(Long.parseLong(matcher.group(3)));
            catUserService.editCatUser(newCatUser);
            return editCatUser.get() + " изменен в базе данных приюта для кошек.";
        }
        return "Некорректный запрос";
    }


}
