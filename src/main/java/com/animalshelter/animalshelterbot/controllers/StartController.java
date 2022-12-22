package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class StartController implements CommandController {
    private static final String startText = "Привет! Данный бот может помочь вам взять и содержать животное из приюта. Для продолжения выберете команду:";
    private static final String addPattern = "(\\d+) \\+ (\\d+)";

    @Command(name = "/start")
    public SendMessage handleStartMessage(Message message) {

        return new SendMessage(message.from().id(), startText)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton("Опция 1").callbackData("start_option_1"),
                        new InlineKeyboardButton("Опция 2").callbackData("start_option_2"),
                        new InlineKeyboardButton("Опция 3").callbackData("start_option_3")
                ));
    }

    @Command(pattern = addPattern)
    public SendMessage handlePatternMessage(Message message) {

        Pattern pattern = Pattern.compile(addPattern);
        Matcher matcher = pattern.matcher(message.text());

        matcher.matches();

        return new SendMessage(message.from().id(),
                Integer.parseInt(matcher.group(1)) + Integer.parseInt(matcher.group(2)) + ""
        );
    }

    @Callback(name = "start_option_1")
    public SendMessage handleStartOption1(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), "Вы выбрали опцию 1");
    }

    @Callback(name = "start_option_2")
    public SendMessage handleStartOption2(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), "Вы выбрали опцию 2");
    }

    @Callback(name = "start_option_3")
    public SendMessage handleStartOption3(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), "Вы выбрали опцию 3");
    }
}
