package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callback;
import com.animalshelter.animalshelterbot.service.ValidatorCatUserService;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
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
 * Запрос через {@link Update#callbackQuery()} осуществляется по значению  {@link Callback#CAT_CONTACT_INFO}
 * <br>
 */
@Component
@RequiredArgsConstructor
public class CatUserController implements CommandController {
    private final Logger logger = LoggerFactory.getLogger(CatUserController.class);
    private final ValidatorCatUserService validatorcatUserService;
    private static final String ADD_CONTACT_PATTERN = "Возьму кота ([\\d]{11})(\\s)([\\W]+)";
    private static final String ADD_ID_CHAT_PATTERN = "Взял кота ([\\d]{11})(\\s)([\\W]+)";
    private static final String BACK_BUTTON_TEXT = "Назад";

    private final String addMessage = "Для того, чтобы оставить контактные данные для обратной " +
            "связи отправьте сообщение в форме:\n Возьму кота 89871234567 Иван \n и мы вам перезвоним.";



    @com.animalshelter.animalshelterbot.handler.Callback(name = Callback.CAT_CONTACT_INFO)
    public SendMessage handleAddMessageCat(CallbackQuery callbackQuery) {
        long idUser = callbackQuery.from().id();
        logger.info("Пользователь {} запросил пример для записи контакта в базу данных приюта для кошек", idUser);
        return new SendMessage(idUser, addMessage)
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callback.CAT_MENU.name())
                ));
    }

    /**
     * <i> Метод для записи контактных данных усыновителя в базу данных приюта для кошек
     * <br>
     * Используется метод {@link ValidatorCatUserService#validateCatUser(Message)}</i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = ADD_CONTACT_PATTERN)
    public SendMessage handleAddCatUser(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} производит запись контактных данных в базу данных приюта для кошек", idUser);
        return new SendMessage(idUser, validatorcatUserService.validateCatUser(message))
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callback.CAT_MENU.name())
                ));
    }
    /**
     * <i> Метод для записи id chat усыновителя в базу данных приюта для кошек
     * <br>
     * Используется метод {@link ValidatorCatUserService#validateCatUserIdChat(Message)} </i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = ADD_ID_CHAT_PATTERN)
    public SendMessage handleAddCatUserIDChat(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} производит запись контактных данных в базу данных приюта для кошек", idUser);
        return new SendMessage(idUser, validatorcatUserService.validateCatUserIdChat(message))
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callback.CAT_MENU.name())
                ));
    }
}
