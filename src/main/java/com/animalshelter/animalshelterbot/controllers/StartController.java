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

/**
 * <i>Контроллер обработки стартового сообщения.</i> <br>
 * Отвечает на:
 * <ul>
 *  <li>Команду с именем {@link #START_COMMAND}, возвращая приветственное сообщение</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class StartController implements CommandController {
    private static final String startText = "Привет! Данный бот может помочь вам взять и содержать животное из приюта. Для продолжения выберете опцию:";
    private static final String shelterInfoText = "В данном разделе можно получить информацию о приюте. Выберите, какую информацию вы хотите получить:";
    private static final String adoptionInfoText = "В данном разделе можно получить информацию об усыновлении собаки. Выберите, какую информацию вы хотите получить:";

    private static final String shelterCommonInfoButtonText = "Узнать информацию о приюте";
    private static final String adoptionInfoButtonText = "Как взять собаку из приюта";
    private static final String keepingPetButtonText = "Ведение питомца";
    private static final String callVolunteerButtonText = "Позвать волонтёра";

    private static final String shelterInfoButtonText = "Узнать информацию о приюте";
    private static final String scheduleButtonText = "Контактные данные";
    private static final String safetyInfoButtonText = "Рекоминдации о технике безопасности на территории приюта";
    private static final String getContactsButtonText = "Записать контактные данные";

    private static final String firstMeetingRulesButtonText = "Правила знакомства с собакой";
    private static final String documentListButtonText = "Список необходимых документов";
    private static final String transportationRecommendationsButtonText = "Рекоммендации по транспортировке";
    private static final String arrangementRecommendationsButtonText = "Рекомендаций по обустройству дома";
    private static final String cynologistAdvicesButtonText = "Советы кинолога";
    private static final String cynologistRecommendationsButtonText = "Проверенные кинологи";
    private static final String rejectionCausesButtonText = "Список причин отказа";

    private static final String START_COMMAND = "/start";

    private static final String SHELTER_COMMON_INFO_CALLBACK = "get_shelter_info";
    private static final String ADOPTION_INFO_CALLBACK = "get_adoption_info";
    private static final String SEND_REPORT_CALLBACK = "send_report";
    private static final String CALL_VOLUNTEER_CALLBACK = "call_volunteer";

    public static final String ABOUT_DESCRIPTION_CALLBACK = "aboutDescriptionText";
    public static final String ABOUT_ADRESSANDHOUTS_CALLBACK = "addressAndOpenHours";

    public static final String GENERAL_SAFETY_CALLBACK = "/safetyInfo";

    private static final String DUMMY_CALLBACK = "0";

    @Command(name = START_COMMAND)
    public SendMessage handleStartMessage(Message message) {
        return new SendMessage(message.from().id(), startText)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton(shelterCommonInfoButtonText).callbackData(SHELTER_COMMON_INFO_CALLBACK),
                        new InlineKeyboardButton(adoptionInfoButtonText).callbackData(ADOPTION_INFO_CALLBACK),
                        new InlineKeyboardButton(keepingPetButtonText).callbackData(SEND_REPORT_CALLBACK),

                        new InlineKeyboardButton(callVolunteerButtonText).callbackData(CALL_VOLUNTEER_CALLBACK)
                ));
    }

    @Callback(name = SHELTER_COMMON_INFO_CALLBACK)
    public SendMessage handleShelterInfo(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), shelterInfoText)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton(shelterInfoButtonText).callbackData(ABOUT_DESCRIPTION_CALLBACK),
                        new InlineKeyboardButton(scheduleButtonText).callbackData(ABOUT_ADRESSANDHOUTS_CALLBACK),
                        new InlineKeyboardButton(safetyInfoButtonText).callbackData(GENERAL_SAFETY_CALLBACK),

                        new InlineKeyboardButton(getContactsButtonText).callbackData(DUMMY_CALLBACK),
                        new InlineKeyboardButton(callVolunteerButtonText).callbackData(CALL_VOLUNTEER_CALLBACK)
                ));
    }

    @Callback(name = ADOPTION_INFO_CALLBACK)
    public SendMessage handleAdoptionInfo(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), adoptionInfoText)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton(firstMeetingRulesButtonText).callbackData(DUMMY_CALLBACK),
                        new InlineKeyboardButton(documentListButtonText).callbackData(DUMMY_CALLBACK),
                        new InlineKeyboardButton(transportationRecommendationsButtonText).callbackData(DUMMY_CALLBACK),
                        new InlineKeyboardButton(arrangementRecommendationsButtonText).callbackData(DUMMY_CALLBACK),
                        new InlineKeyboardButton(cynologistAdvicesButtonText).callbackData(DUMMY_CALLBACK),
                        new InlineKeyboardButton(cynologistRecommendationsButtonText).callbackData(DUMMY_CALLBACK),
                        new InlineKeyboardButton(rejectionCausesButtonText).callbackData(DUMMY_CALLBACK),

                        new InlineKeyboardButton(getContactsButtonText).callbackData(DUMMY_CALLBACK),
                        new InlineKeyboardButton(callVolunteerButtonText).callbackData(CALL_VOLUNTEER_CALLBACK)
                ));
    }
}
