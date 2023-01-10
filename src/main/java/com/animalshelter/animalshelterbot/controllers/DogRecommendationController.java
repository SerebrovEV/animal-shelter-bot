package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <i>Контроллер получения информации и рекомендаций для работы с собакой.</i>
 * <br>
 */
@Component
@RequiredArgsConstructor
public class DogRecommendationController implements CommandController {

    private final String pathToFileRecommendation = "src/main/resources/textinfo/dog_dating_rules_recommendation.txt";

    private final String pathToFileCynologistTeam = "src/main/resources/textinfo/dog_cynologists_team.txt";

    /**
     * Для проверки. TODO удалить
     */
    @Command(name = "/rules")
    public SendMessage handleDescriptionMessage(Message message) throws IOException {
        String text = Files.readString(Paths.get(pathToFileRecommendation));
        return new SendMessage(message.from().id(), text)
                .parseMode(ParseMode.Markdown);
    }

    /**
     * Получение информации о знакомстве с собакой <br>
     * Запрос осуществляется по значению  {@link Callbacks#DOG_MEETING_RULES_INFO}
     *
     * @return Рекомендации для знакомства с собакой
     * @throws IOException
     */
    @Callback(name = Callbacks.DOG_MEETING_RULES_INFO)
    public SendMessage handleMeetingRulesCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        String text = Files.readString(Paths.get(pathToFileRecommendation));
        return new SendMessage(callbackQuery.from().id(), text)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton(("Назад"))
                        .callbackData(Callbacks.DOG_ADOPTION_INFO_MENU.name())));
    }

    /**
     * Получение информации о проверенных кинологах <br>
     * Запрос осуществляется по значению  {@link Callbacks#DOG_CYNOLOGIST_RECOMMENDATION}
     *
     * @return Рекомендации для знакомства с собакой
     * @throws IOException
     */
    @Callback(name = Callbacks.DOG_CYNOLOGIST_RECOMMENDATION)
    public SendMessage handleCynologistsRecommendationCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        String text = Files.readString(Paths.get(pathToFileCynologistTeam));
        return new SendMessage(callbackQuery.from().id(), text)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton(("Назад"))
                        .callbackData(Callbacks.DOG_ADOPTION_INFO_MENU.name())));
    }
}
