package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Контроллер для получения информации о приюте, его адресе и часах работы, контактов для оформления пропуска.
 * Дополнительно изображения - схемы проезда в приюты для собак и кошек.
 * Запрос на информацию о приюте для собак через кнопку (callbackQuery) по значению {@link Callbacks#DOG_SHELTER_INFO}
 * Запрос на информацию о приюте для кошек через кнопку (callbackQuery) по значению {@link Callbacks#CAT_SHELTER_INFO}
 * Запрос на информацию об адресе и часах работы через кнопку (callbackQuery) по значению {@link Callbacks#DOG_SCHEDULE_INFO}
 * Запрос на информацию об адресе и часах работы через кнопку (callbackQuery) по значению {@link Callbacks#CAT_SCHEDULE_INFO}
 * Запрос на контактные данные для оформления пропуска приюта собак (callbackQuery) по значению {@link Callbacks#DOG_CAR_INFO}
 * Запрос на контактные данные для оформления пропуска приюта кошек (callbackQuery) по значению {@link Callbacks#CAT_CAR_INFO}
 */
@Component
@RequiredArgsConstructor
public class AboutController implements CommandController {
    private final TelegramBotSender telegramBotSender;
    private final String aboutDogDescriptionText = "Приют Лапки Добра для собак - это муниципальный приют для бездомных собак в Астане. " +
            "В нем живет почти 2500 собак. Большие и маленькие, пушистые и гладкие, веселые и задумчивые - и на всех одна" +
            " большая мечта - встретить своего Человека и найти Дом.\n" +
            "\n" +
            "Взять домой\n" +
            "\n" +
            "Если вы хотите взять собаку, не ищите питомник, " +
            "в котором можно купить щенка - просто свяжитесь с нами, " +
            "и вы обязательно найдете себе самого лучшего друга. Во всем мире это уже стало доброй традицией - человек, " +
            "который решил завести питомца, обращается в приют, чтобы подарить заботу и любовь тому, " +
            "кто уже появился на свет, но ему почему-то досталась нелегкая судьба. Мы поможем вам выбрать животное с учетом ваших пожеланий и предпочтений," +
            " с радостью познакомим со всеми нашими собаками. Все наши питомцы привиты и стерилизованы.";

    private final String aboutCatDescriptionText = "Приют Лапки Добра для кошек - это муниципальный приют для кошек в Астане. " +
            "В нем живет почти 150 кошек. Большие и маленькие, пушистые и гладкие, веселые и задумчивые - и на всех одна" +
            " большая мечта - встретить своего Человека и найти Дом.\n" +
            "\n" +
            "Взять домой\n" +
            "\n" +
            "Если вы хотите взять кошку, не ищите питомник, " +
            "в котором можно купить котенка - просто свяжитесь с нами, " +
            "и вы обязательно найдете себе самого лучшего друга. Во всем мире это уже стало доброй традицией - человек, " +
            "который решил завести питомца, обращается в приют, чтобы подарить заботу и любовь тому, " +
            "кто уже появился на свет, но ему почему-то досталась нелегкая судьба. Мы поможем вам выбрать животное с учетом ваших пожеланий и предпочтений," +
            " с радостью познакомим со всеми нашими кошками. Все наши питомцы привиты и стерилизованы.";

    private static final String BACK_BUTTON_TEXT = "Назад";

    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.DOG_SHELTER_INFO)
    public SendMessage handleCallbackDogDescriptionMessage(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), aboutDogDescriptionText)
         .replyMarkup(new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.DOG_INFO_MENU.name())));

    }
    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.CAT_SHELTER_INFO)
    public SendMessage handleCallbackCatDescriptionMessage(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), aboutCatDescriptionText)
         .replyMarkup(new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.CAT_INFO_MENU.name())));

    }

    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.DOG_SCHEDULE_INFO)
    public SendMessage handleCallbackDogAddressAndHoursMessage(CallbackQuery callbackQuery) {
        File imageScheme = new File("src/main/resources/images/scheme.PNG");
        String addressAndOpenHours = "Мы работаем ежедневно с 11:00 до 18:00 по адресу: улица Аккорган, 5в, Сарыарка район, Нур-Султан (Астана)";
        SendPhoto scheme = new SendPhoto(callbackQuery.from().id(), imageScheme).caption(addressAndOpenHours);
        telegramBotSender.telegramSendPhoto(scheme);
        return new SendMessage(callbackQuery.from().id(), "")
        .replyMarkup(new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.DOG_INFO_MENU.name())));
    }
    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.CAT_SCHEDULE_INFO)
    public SendMessage handleCallbackCatAddressAndHoursMessage(CallbackQuery callbackQuery) {
        File imageScheme = new File("src/main/resources/images/scheme_cat.PNG");
        String addressAndOpenHours = "Мы работаем со вторника по субботу с 11:00 до 16:00 часов. По адресу: ул. Кенесары, 52, Нур-Султан (Астана)";
        SendPhoto scheme = new SendPhoto(callbackQuery.from().id(), imageScheme).caption(addressAndOpenHours);
        telegramBotSender.telegramSendPhoto(scheme);
        return new SendMessage(callbackQuery.from().id(), "")
        .replyMarkup(new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.CAT_INFO_MENU.name())));
    }

    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.DOG_CAR_INFO)
    public SendMessage handleDogCarInfoCallbackMessage(CallbackQuery callbackQuery) {
        String contactSecurity = "Для оформления пропуска свяжитесь с начальником отдела охраны Ивановым Иваном Ивановичем 89871234567 " +
                "или службой охраны 88125461234";
        return new SendMessage(callbackQuery.from().id(), contactSecurity)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.DOG_INFO_MENU.name())));

    }

    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.CAT_CAR_INFO)
    public SendMessage handleCatCarInfoCallbackMessage(CallbackQuery callbackQuery) {
        String contactSecurity = "Для оформления пропуска свяжитесь с начальником отдела охраны Семеновым Семеном Семеновичем 89861234567 " +
                "или службой охраны 88145461234";
        return new SendMessage(callbackQuery.from().id(), contactSecurity)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.CAT_INFO_MENU.name())));

    }
}
