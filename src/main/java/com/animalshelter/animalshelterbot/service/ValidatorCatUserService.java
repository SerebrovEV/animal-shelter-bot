package com.animalshelter.animalshelterbot.service;


import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.controllers.CatUserController;
import com.animalshelter.animalshelterbot.controllers.AdminCatUserController;
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
 * {@link CatUserController} и {@link AdminCatUserController}
 * и подготовки ответного сообщения пользователю</i>
 */

@Service
@RequiredArgsConstructor
public class ValidatorCatUserService {

    private final CatUserService catUserService;
    private final TelegramBot telegramBot;

    private final Pattern ADD_PATTERN = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");
    private final Pattern NUMBER_PATTERN = Pattern.compile("([\\d]+)");
    private final Pattern EDIT_PATTERN = Pattern.compile("([\\d]+)(\\s)([\\d]{11})(\\s)([\\W]+)");

    private final String CONGRATULATION_MESSAGE = "Поздравляем, вы прошли испытательный срок. Продолжайте и" +
            " впредь заботится о своем новом любимце и он ответит вам любовью ответ:)";

    private final String RETURN_MESSAGE = "К сожалению, вы не прошли испытательный срок. Вам требуется вернуть животное " +
            "в приют. Если вы не можете к нам приехать, мы можем направить к вам волонтера для возврата животного. Для " +
            "этого свяжитесь с нами.";

    private static final String catButtonText = "Вернуться";

    /**
     * <i> Метод для проверки и обработки входящего сообщения от усыновителя.
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

            if (catUserService.getCatUserByChatId(chatId).isEmpty() && catUserService.getCatUserByPhoneNumber(phone).isEmpty()) {
                CatUser catUser = catUserService.addCatUser(new CatUser(name, phone, chatId));
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
    public String validateCatUserIdChat(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }

            Long phone = Long.valueOf(matcher.group(1));
            Long chatId = message.from().id();

            if (catUserService.getCatUserByChatId(chatId).isPresent()) {
                return "Обновить запись не удалось, свяжитесь с волонтером для уточнения информации";
            }

            Optional<CatUser> catUser = catUserService.getCatUserByPhoneNumber(phone);
            if (catUser.isEmpty()) {
                return "Телефон не найден, свяжитесь с волонтером для уточнения информации";
            }

            CatUser newCatUser = catUser.get();
            newCatUser.setChatId(chatId);
            catUserService.editCatUser(newCatUser);
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
    public String validateCatUserFromAdmin(Message message) {
        Matcher matcher = ADD_PATTERN.matcher(message.text());
        if (matcher.find()) {
            String name = matcher.group(3);
            if (!matcher.group(1).startsWith("8")) {
                return "Некорректный номер телефона";
            }
            Long phone = Long.valueOf(matcher.group(1));
            if (catUserService.getCatUserByPhoneNumber(phone).isEmpty()) {
                CatUser catUser = catUserService.addCatUser(new CatUser(name, phone));
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
    public String validateGetCatUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
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
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
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

    /**
     * <i> Метод для проверки и обработки входящего сообщения на отправку поздравления усыновителю от администратора.
     * <br>
     * Запрос выполняется через метод {@link AdminCatUserController#handleCongratulationCatUser(Message)}. </i>
     *
     * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateCongratulationCatUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<CatUser> findCatUser = catUserService.getCatUser(id);
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
    public String validateReturnCatUserFromAdmin(Message message) {
        Matcher matcher = NUMBER_PATTERN.matcher(message.text());
        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            Optional<CatUser> findCatUser = catUserService.getCatUser(id);
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
