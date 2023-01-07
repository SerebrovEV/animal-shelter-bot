package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i>Контроллер обработки стартового сообщения.</i> <br>
 * Отвечает на:
 * <ul>
 *  <li>Команду с именем {@link #START_COMMAND}, возвращая приветственное сообщение</li>
 *  <li>Команду с паттерном {@link #ADD_PATTERN}, возвращая сообщение c суммой чисел</li>
 *  <li>Коллбэк {@link #OPTION1_CALLBACK}, возвращая сообщение c суммой чисел</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class StartController implements CommandController {
    private static final String startText = "Привет! Данный бот может помочь вам взять и содержать животное из приюта. Для продолжения выберете команду:";

    private static final String button1Text = "Опция 1";
    private static final String button2Text = "Опция 2";
    private static final String button3Text = "Опция 3";
    private static final String buttonCallVolunteer = "Позвать волонтера";

    private static final String callback1Text = "Вы выбрали опцию 1";
    private static final String callback2Text = "Вы выбрали опцию 2";
    private static final String callback3Text = "Вы выбрали опцию 3";

    private static final String START_COMMAND = "/start";
    private static final String ADD_PATTERN = "(\\d+) \\+ (\\d+)";

    private static final String OPTION1_CALLBACK = "start_option_1";
    private static final String OPTION2_CALLBACK = "start_option_2";
    private static final String OPTION3_CALLBACK = "start_option_3";

    @Command(name = START_COMMAND)
    public SendMessage handleStartMessage(Message message) {

        return new SendMessage(message.from().id(), startText)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton(button1Text).callbackData(OPTION1_CALLBACK),
                        new InlineKeyboardButton(button2Text).callbackData(OPTION2_CALLBACK),
                        new InlineKeyboardButton(button3Text).callbackData(OPTION3_CALLBACK)
                ).addRow(new InlineKeyboardButton(buttonCallVolunteer)
                        .callbackData(CallVolunteerController.CALL_VOLUNTEER_CALLBACK))
                );
    }

    @Command(pattern = ADD_PATTERN)
    public SendMessage handlePatternMessage(Message message) {

        Pattern pattern = Pattern.compile(ADD_PATTERN);
        Matcher matcher = pattern.matcher(message.text());

        matcher.matches();

        return new SendMessage(message.from().id(),
                Integer.parseInt(matcher.group(1)) + Integer.parseInt(matcher.group(2)) + ""
        );
    }

    @Callback(name = OPTION1_CALLBACK)
    public SendMessage handleStartOption1(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), callback1Text);
    }

    @Callback(name = OPTION2_CALLBACK)
    public SendMessage handleStartOption2(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), callback2Text);
    }

    @Callback(name = OPTION3_CALLBACK)
    public SendMessage handleStartOption3(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), callback3Text);
    }
}
