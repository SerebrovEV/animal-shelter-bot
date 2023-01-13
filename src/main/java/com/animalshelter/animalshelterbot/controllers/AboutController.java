package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;


/**
 * Контроллер для получения информации о приюте, его адресе и часах работы, контактов для оформления пропуска.
 * Дополнительно изображение схему проезда.
 * Запрос на информацию о приюте через  message по команде {@link #ABOUT_DESCRIPTION_COMMAND}
 * Запрос на информацию о приюте через кнопку (callbackQuery) по значению {@link #ABOUT_DESCRIPTION_CALLBACK}
 * Запрос на информацию об адресе и часах работы через message по команде {@link #ABOUT_ADDRESSANDHOURS_COMMAND}
 * Запрос на информацию об адресе и часах работы через кнопку (callbackQuery) по значению {@link #ABOUT_ADDRESSANDHOURS_CALLBACK}
 * Запрос на контактные данные для оформления пропуска приюта собак (callbackQuery) по значению {@link Callbacks#DOG_CAR_INFO}
 * Запрос на контактные данные для оформления пропуска приюта кошек (callbackQuery) по значению {@link Callbacks#CAT_CAR_INFO}
 */
@Component
@RequiredArgsConstructor
public class AboutController implements CommandController {
    private final TelegramBotSender telegramBotSender;
    private final String aboutDescriptionText = "Приют Лапки Добра - это муниципальный приют для бездомных собак и кошек в Астане. " +
            "В нем живет почти 2500 собак и 150 кошек. Большие и маленькие, пушистые и гладкие, веселые и задумчивые - и на всех одна" +
            " большая мечта - встретить своего Человека и найти Дом.\n" +
            "\n" +
            "Взять домой\n" +
            "\n" +
            "Если вы хотите взять собаку или кошку, не ищите питомник, " +
            "в котором можно купить щенка или котенка - просто свяжитесь с нами, " +
            "и вы обязательно найдете себе самого лучшего друга. Во всем мире это уже стало доброй традицией - человек, " +
            "который решил завести питомца, обращается в приют, чтобы подарить заботу и любовь тому, " +
            "кто уже появился на свет, но ему почему-то досталась нелегкая судьба. Мы поможем вам выбрать животное с учетом ваших пожеланий и предпочтений," +
            " с радостью познакомим со всеми нашими собаками и кошками. Все наши питомцы привиты и стерилизованы.";

    public static final String ABOUT_DESCRIPTION_COMMAND = "/description";
    public static final String ABOUT_DESCRIPTION_CALLBACK = "aboutDescriptionText";
    public static final String ABOUT_ADDRESSANDHOURS_COMMAND = "/addressAndOpenHours";
    public static final String ABOUT_ADDRESSANDHOURS_CALLBACK = "addressAndOpenHours";
    private static final String backButtonText = "Назад";


    @Command(name = ABOUT_DESCRIPTION_COMMAND)
    public SendMessage handleDescriptionMessage(Message message) {
        return new SendMessage(message.from().id(), aboutDescriptionText);
    }
    @Callback(name = Callbacks.DOG_SHELTER_INFO)
   public SendMessage handleCallbackDescriptionMessage(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), aboutDescriptionText);
    }
    @Command(name = ABOUT_ADDRESSANDHOURS_COMMAND)
    public SendMessage handleAddressAndHoursMessage(Message message) {
        File imageScheme = new File("src/main/resources/images/scheme.PNG");
        String addressAndOpenHours = "Мы работаем ежедневно с 11:00 до 18:00 по адресу: улица Аккорган, 5в, Сарыарка район, Астана";
        SendPhoto scheme = new SendPhoto(message.from().id(), imageScheme).caption(addressAndOpenHours);
        telegramBotSender.telegramSendPhoto(scheme);
        return new SendMessage(message.from().id(), "");
        //return new SendMessage(message.from().id(), addressAndOpenHours);
    }
   @Callback(name = Callbacks.DOG_SCHEDULE_INFO)
    public SendMessage handleCallbackAddressAndHoursMessage(CallbackQuery callbackQuery) {
        File imageScheme = new File("src/main/resources/images/scheme.PNG");
        String addressAndOpenHours = "Мы работаем ежедневно с 11:00 до 18:00 по адресу: улица Аккорган, 5в, Сарыарка район, Астана";
        SendPhoto scheme = new SendPhoto(callbackQuery.from().id(), imageScheme).caption(addressAndOpenHours);
        telegramBotSender.telegramSendPhoto(scheme);
        return new SendMessage(callbackQuery.from().id(), "");
        //return new SendMessage(callbackQuery.from().id(), addressAndOpenHours);
    }


    @Callback(name = Callbacks.DOG_CAR_INFO)
    public SendMessage handleDogCarInfoCallbackMessage(CallbackQuery callbackQuery) {
        String contactSecurity = "Для оформления пропуска свяжитесь с начальником отдела охраны Ивановым Иваном Ивановичем 89871234567 " +
                "или службой охраны 88125461234";
        return new SendMessage(callbackQuery.from().id(), contactSecurity)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(backButtonText).callbackData(Callbacks.DOG_INFO_MENU.name())));

    }

    @Callback(name = Callbacks.CAT_CAR_INFO)
    public SendMessage handleCatCarInfoCallbackMessage(CallbackQuery callbackQuery) {
        String contactSecurity = "Для оформления пропуска свяжитесь с начальником отдела охраны Семеновым Семеном Семеновичем 89861234567 " +
                "или службой охраны 88145461234";
        return new SendMessage(callbackQuery.from().id(), contactSecurity)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(backButtonText).callbackData(Callbacks.CAT_INFO_MENU.name())));

    }
}
