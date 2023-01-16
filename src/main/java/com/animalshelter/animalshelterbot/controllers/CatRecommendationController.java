package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <i>Контроллер получения информации и рекомендаций для работы с кошкой.</i>
 * <br>
 */
@Component
@RequiredArgsConstructor
public class CatRecommendationController implements CommandController {

    private final TelegramBotSender telegramBotSender;

    private static final String backButtonText = "Назад";

    private final String pathToFileCatRecommendation = "src/main/resources/textinfo/cat_dating_rules_recommendation.txt";
    private final String pathToFileKittyHousingRecommendation = "src/main/resources/textinfo/kitty_housing_recommendations.txt";
    private final String pathToFileHousingRecommendation = "src/main/resources/textinfo/cat_housing_recommendations.txt";
    private final String getPathToFileRecommendationDisabilitiesCat = "src/main/resources/textinfo/recommendations_for_a_cat_with_disabilities.txt";
    private final String pathToFileRejectionsReason = "src/main/resources/textinfo/rejection_reason.txt";
    private final String pathToFileDocList = "src/main/resources/textinfo/doc_list.txt";
    private final String pathToFileTransportationRecommendation = "src/main/resources/textinfo/cat_transportation_recommendations.txt";

    /**
     * Получение информации о знакомстве с кошкой <br>
     * Запрос осуществляется по значению  {@link Callbacks#CAT_MEETING_RULES_INFO}
     *
     * @return Рекомендации для знакомства с кошкой
     * @throws IOException
     */
    @Callback(name = Callbacks.CAT_MEETING_RULES_INFO)
    public SendMessage handleMeetingRulesCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        return getInfo(pathToFileCatRecommendation, callbackQuery.from().id());
    }

    /**
     * Получение информации о причинах отказа <br>
     * Запрос осуществляется по значению  {@link Callbacks#CAT_DECLINE_CAUSES}
     *
     * @return Список причин для отказа.
     */
    @Callback(name = Callbacks.CAT_DECLINE_CAUSES)
    public SendMessage handleCatDeclineCausesCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        return getInfo(pathToFileRejectionsReason, callbackQuery.from().id());
    }

    /**
     * Получение информации о необходимых документах для усыновления животного<br>
     * Запрос осуществляется по значению  {@link Callbacks#CAT_DOCUMENT_LIST}
     *
     * @return Спсок документов.
     * @throws IOException
     */
    @Callback(name = Callbacks.CAT_DOCUMENT_LIST)
    public SendMessage handleCatDocListCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        File petContract = new File("src/main/resources/documents/Договор.doc");
        SendDocument document = new SendDocument(callbackQuery.from().id(), petContract)
                .caption("Образец договора для ознакомления");
        telegramBotSender.sendDocument(document);
        return getInfo(pathToFileDocList, callbackQuery.from().id());
    }

    /**
     * Получение рекомендаций по обустройству дома для взрослой кошки <br>
     * Запрос осуществляется по значению  {@link Callbacks#CAT_HOUSING_RECOMMENDATION}
     *
     * @return рекомендации по обустройству дома.
     * @throws IOException
     */
    @Callback(name = Callbacks.CAT_HOUSING_RECOMMENDATION)
    public SendMessage handleCatHousingRecommendationCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        return getInfo(pathToFileHousingRecommendation, callbackQuery.from().id());
    }

    /**
     * Получение рекомендаций по обустройству дома для котенка <br>
     * Запрос осуществляется по значению  {@link Callbacks#CAT_YOUNG_HOUSING_RECOMMENDATION}
     *
     * @return рекомендации по обустройству дома для котенка.
     * @throws IOException
     */
    @Callback(name = Callbacks.CAT_YOUNG_HOUSING_RECOMMENDATION)
    public SendMessage handleKittyHousingRecommendationCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        return getInfo(pathToFileKittyHousingRecommendation, callbackQuery.from().id());
    }

    /**
     * Получение рекомендаций по транспортировке кошки <br>
     * Запрос осуществляется по значению  {@link Callbacks#CAT_TRANSPORTATION_RECOMMENDATION}
     *
     * @return рекомендации по транспортировке питомца.
     * @throws IOException
     */
    @Callback(name = Callbacks.CAT_TRANSPORTATION_RECOMMENDATION)
    public SendMessage handleCatTransportationRecommendationCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        return getInfo(pathToFileTransportationRecommendation, callbackQuery.from().id());
    }

    /**
     * Получение советов по обустройству дома для кошки с ограниченными возможностями.  <br>
     * Запрос осуществляется по значению  {@link Callbacks#CAT_DISABLED_HOUSING_RECOMMENDATION}
     *
     * @return Рекомендации по обустройству дома
     * @throws IOException
     */
    @Callback(name = Callbacks.CAT_DISABLED_HOUSING_RECOMMENDATION)
    public SendMessage handleCatDisabledHousingCallbackMessage(CallbackQuery callbackQuery) throws IOException {
        return getInfo(getPathToFileRecommendationDisabilitiesCat, callbackQuery.from().id());
    }

    private SendMessage getInfo(String filePath, Long id) throws IOException {
        String text = Files.readString(Paths.get(filePath));
        return new SendMessage(id, text)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(new InlineKeyboardMarkup().addRow(
                        new InlineKeyboardButton(backButtonText)
                                .callbackData(Callbacks.CAT_ADOPTION_INFO_MENU.name())
                ));
    }

}


