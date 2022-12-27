package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Контроллер для получения информации о приюте, его адресе и часах работы.
 * Дополнительно изображение схему проезда.
 * Запрос на информацию о приюте через  message по команде {@link #ABOUT_DESCRIPTION_COMMAND}
 * Запрос на информацию о приюте через кнопку (callbackQuery) по значению {@link #ABOUT_DESCRIPTION_CALLBACK}
 * Запрос на информацию об адресе и часах работы через message по команде {@link #ABOUT_ADRESSANDHOUTS_COMMAND}
 * Запрос на информацию об адресе и часах работы через кнопку (callbackQuery) по значению {@link #ABOUT_ADRESSANDHOUTS_CALLBACK}
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
    private final String addressAndOpenHours = "Мы работаем ежедневно с 11:00 до 18:00\n По адресу: улица Аккорган, 5в, Сарыарка район, Астана";

    public static final String ABOUT_DESCRIPTION_COMMAND = "/description";
    public static final String ABOUT_DESCRIPTION_CALLBACK = "aboutDescriptionText";
    public static final String ABOUT_ADRESSANDHOUTS_COMMAND = "/addressAndOpenHours";
    public static final String ABOUT_ADRESSANDHOUTS_CALLBACK = "addressAndOpenHours";
    
    private final String FILE_IMAGE_PATH = "image/schema.png";

    @Command(name = ABOUT_DESCRIPTION_COMMAND)
    public SendMessage handleDescriptionMessage(Message message) {

        return new SendMessage(message.from().id(), aboutDescriptionText);
    }

    @Callback(name = ABOUT_DESCRIPTION_CALLBACK)
    public SendMessage handleCallbackDescriptionMessage(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), aboutDescriptionText);
    }
    @Command(name = ABOUT_ADRESSANDHOUTS_COMMAND)
    public SendMessage handleAddressAndHoursMessage(Message message) {

        Path path = Paths.get(FILE_IMAGE_PATH);
        File fileSchema = path.toFile();
        SendPhoto schema = new SendPhoto(message.from().id(), fileSchema).caption(addressAndOpenHours);
//        telegramBotSender.telegramSendRequest(new SendMessage(message.from().id(), addressAndOpenHours));
        telegramBotSender.telegramSendRequest(schema);
        return new SendMessage(message.from().id(), "");
//        return new SendMessage(message.from().id(), addressAndOpenHours);

    }

    @Callback(name = ABOUT_ADRESSANDHOUTS_CALLBACK)
    public SendMessage handleCallbackAddressAndHoursMessage(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), addressAndOpenHours);
    }
}
