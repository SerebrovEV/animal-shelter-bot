package com.animalshelter.animalshelterbot.controllers;

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
 * Запрос через {@link Update} на добавление контакта осуществляется по команде
 * {@link #ADD_CONTACT}
 * <br>
 */
@Component
@RequiredArgsConstructor
public class DogUserController implements CommandController {
    private final Logger logger = LoggerFactory.getLogger(DogUserController.class);
    private final ValidatorDogUserService validatorDogUserService;
    private final String ADD_CONTACT = "/addContactDog";
    //private final String GET_CONTACT = "/getContact";
    private static final String ADD_CONTACT_PATTERN = "Возьму собаку ([\\d]{11})(\\s)([\\W]+)";
    private static final String backButtonText = "Назад";

    private final String ADD_MESSAGE = "Для того, чтобы оставить контактные данные для обратной " +
            "связи отправьте сообщение в форме:\n Возьму собаку 89871234567 Иван \n и мы вам перезвоним.";

    @Command(name = ADD_CONTACT)
    public SendMessage handleAddMessage(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} запросил пример для записи контакта в БД", idUser);
        return new SendMessage(idUser, ADD_MESSAGE)
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(backButtonText).callbackData(Callbacks.DOG_MENU.name())
                ));
    }

    @Callback(name = Callbacks.DOG_CONTACT_INFO)
    public SendMessage handleAddMessageDog(CallbackQuery callbackQuery) {
        long idUser = callbackQuery.from().id();
        logger.info("Пользователь {} запросил пример для записи контакта в БД собачего приюта", idUser);
        return new SendMessage(idUser, ADD_MESSAGE)
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(backButtonText).callbackData(Callbacks.DOG_MENU.name())
                ));
    }

//    /**
//     * <i>Метод для получения пользователем контактных данных, которые он записал в базу данных
//     * <br>
//     * Используется метод {@link ValidatorUserService#validateGetUser(Message)}</i>
//     * @param message
//     * @return {@link SendMessage}
//     */
//    @Command(name = GET_CONTACT)
//    public SendMessage getContactMessage(Message message) {
//        long idUser = message.from().id();
//        logger.info("Пользователь {} запросил проверку своего контакта в БД", idUser);
//        return new SendMessage(idUser, validatorUserService.validateGetUser(message));
//    }
//    @Callback(name = GET_CONTACT)
//    public SendMessage getContactMessage(CallbackQuery callbackQuery) {
//        return new SendMessage(callbackQuery.from().id(), validatorUserService.validateGetUser(callbackQuery));
//    }


    /**
     * <i> Метод для записи контактных данных усыновителя в базу данных собачего приюта
     * <br>
     * Используется метод {@link ValidatorDogUserService#validateDogUser(Message)}</i>
     *
     * @param message
     * @return {@link SendMessage}
     */
    @Command(pattern = ADD_CONTACT_PATTERN)
    public SendMessage handleAddDogUser(Message message) {
        long idUser = message.from().id();
        logger.info("Пользователь {} производит запись контактных данных в БД собачего приюта", idUser);
        return new SendMessage(idUser, validatorDogUserService.validateDogUser(message))
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(backButtonText).callbackData(Callbacks.DOG_MENU.name())
                ));
    }
}
