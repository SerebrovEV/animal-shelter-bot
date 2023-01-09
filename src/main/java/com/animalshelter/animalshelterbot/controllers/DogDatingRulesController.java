package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <i>Контроллер получения информации по правилам знакомства с собакой.</i>
 * <br>
 * Запрос через {@link Update#callbackQuery()} осуществляется по значению  {@link #DOG_DATING_RULES_CALLBACK}
 */
@Component
@RequiredArgsConstructor
public class DogDatingRulesController implements CommandController {

    private final String pathToFileRecommendation = "src/main/resources/textinfo/dog_dating_rules_recommendation.txt";

    public static final String DOG_DATING_RULES_CALLBACK = "dogDatingRules";

    /**
     *Для проверки
     */
    @Command(name = "/rules")
    public SendMessage handleDescriptionMessage(Message message) throws IOException {
        String text = Files.readString(Paths.get(pathToFileRecommendation));
        return new SendMessage(message.from().id(), text)
                .parseMode(ParseMode.Markdown);
    }

    @Callback(name = DOG_DATING_RULES_CALLBACK)
    public SendMessage handleCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        String text = Files.readString(Paths.get(pathToFileRecommendation));
        return new SendMessage(callbackQuery.from().id(), text)
                .parseMode(ParseMode.Markdown);
    }
}
