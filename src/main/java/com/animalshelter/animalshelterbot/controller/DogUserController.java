package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.pengrad.telegrambot.model.Update;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.service.ValidatorDogUserService;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <i> Контроллер для получения или сохранения контактных данных пользователя в базу данных.</i>
 * <br>
 * Запрос через {@link Update#callbackQuery()} осуществляется по значению  {@link Callbacks#DOG_CONTACT_INFO}
 * <br>
 */
@Component
@RequiredArgsConstructor
public class DogUserController implements CommandController {

    private final Logger logger = LoggerFactory.getLogger(DogUserController.class);
    private final ValidatorDogUserService validatorDogUserService;
    private static final String ADD_CONTACT_PATTERN = "Возьму собаку ([\\d]{11})(\\s)([\\W]+)";
    private static final String ADD_ID_CHAT_PATTERN = "Взял кота ([\\d]{11})(\\s)([\\W]+)";
    private static final String BACK_BUTTON_TEXT = "Назад";

    private final String addMessage = "Для того, чтобы оставить контактные данные для обратной " +
            "связи отправьте сообщение в форме:\n Возьму собаку 89871234567 Иван \n и мы вам перезвоним.";

    @Callback(name = Callbacks.DOG_CONTACT_INFO)
    public SendMessage handleAddMessageDog(CallbackQuery callbackQuery) {
        long idUser = callbackQuery.from().id();
        logger.info("Пользователь {} запросил пример для записи контакта в базу данных приюта для собак", idUser);
        return new SendMessage(idUser, addMessage)
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.DOG_MENU.name())
                ));
    }
    /**
     * <i> Метод для записи контактных данных усыновителя в базу данных приюта для собак
     * <br>
     * Используется метод {@link ValidatorDogUserService#validateDogUser(Message)}</i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = ADD_CONTACT_PATTERN)
    public SendMessage handleAddDogUser(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} производит запись контактных данных в базу данных приюта для собак", idUser);
        return new SendMessage(idUser, validatorDogUserService.validateDogUser(message))
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.DOG_MENU.name())
                ));
    }
    /**
     * <i> Метод для записи chatId усыновителя в базу данных приюта для собак
     * <br>
     * Используется метод {@link ValidatorDogUserService#validateDogUserIdChat(Message)}</i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = ADD_ID_CHAT_PATTERN)
    public SendMessage handleAddDogUserIDChat(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} производит запись контактных данных в базу данных приюта для собак", idUser);
        return new SendMessage(idUser, validatorDogUserService.validateDogUserIdChat(message))
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.CAT_MENU.name())
                ));
    }
}
