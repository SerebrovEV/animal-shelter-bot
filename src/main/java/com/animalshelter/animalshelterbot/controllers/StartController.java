package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <i>Контроллер обработки навигации по боту. Отвечает за ответ на все {@link Callback}`и-меню</i> <br>
 */
@Component
@RequiredArgsConstructor
public class StartController implements CommandController {
    //    @Value("${telegram.volunteer.chat.id}")
    //    private Long VOLUNTEER_CHAT_ID;

    private static final String startMenuText = "Привет! Данный бот может помочь вам взять и содержать животное из приюта. Для продолжения выберите животное:";
    private static final String AdminMenuText = "Основные команды для администратора:\n" +
            "/infoAboutAdminCat - команда для вызова меню с информацией о работе с кошками из приюта для кошек;\n" +
            "/infoAboutAdminDog - команда для вызова меню с информацией о работе с  собаками из приюта для собак;\n" +
            "/infoAboutAdminCatUser - команда для вызова меню с информацией о работе с усыновителями из приюта для кошек;\n" +
            "/infoAboutAdminDogUser - команда для вызова меню с информацией о работе с усыновителями из приюта для собак";

    private static final String dogMenuText = "Вы выбрали приют для собак. Для продолжения выберите раздел:";
    private static final String dogShelterInfoText = "В данном разделе можно получить информацию о приюте. Выберите, какую информацию вы хотите получить:";
    private static final String dogAdoptionInfoText = "В данном разделе можно получить информацию об усыновлении собаки. Выберите, какую информацию вы хотите получить:";

    private static final String catMenuText = "Вы выбрали приют для кошек. Для продолжения выберите раздел:";
    private static final String catShelterInfoText = "В данном разделе можно получить информацию о приюте. Выберите, какую информацию вы хотите получить:";
    private static final String catAdoptionInfoText = "В данном разделе можно получить информацию об усыновлении кошки. Выберите, какую информацию вы хотите получить:";

    private static final String backButtonText = "Назад";

    private static final String dogButtonText = "Собака";
    private static final String catButtonText = "Кошка";

    private static final String shelterCommonInfoButtonText = "Узнать информацию о приюте";
    private static final String adoptionInfoButtonText = "Как взять питомца из приюта";
    private static final String keepingPetButtonText = "Ведение питомца";
    private static final String callVolunteerButtonText = "Позвать волонтёра";

    private static final String shelterInfoButtonText = "Узнать информацию о приюте";
    private static final String scheduleButtonText = "Контактные данные";
    private static final String carButtonText = "Данные для оформления пропуска";
    private static final String safetyInfoButtonText = "Рекомендации о ТБ на территории приюта";
    private static final String getContactsButtonText = "Записать контактные данные";

    private static final String firstMeetingRulesButtonText = "Правила знакомства с собакой";
    private static final String firstMeetingCatRulesButtonText = "Правила знакомства с кошкой";
    private static final String documentListButtonText = "Список необходимых документов";
    private static final String transportationRecommendationsButtonText = "Рекомендации по транспортировке";
    private static final String youngHousingRecommendationsButtonText = "Рекомендации по обустройству дома для детёныша";
    private static final String housingRecommendationsButtonText = "Рекомендации по обустройству дома";
    private static final String disabledHousingRecommendationsButtonText = "Рекомендации по обустройству дома для животного с огр. возможностями";
    private static final String declineCausesButtonText = "Список причин отказа";
    private static final String cynologistAdvicesButtonText = "Советы кинолога";
    private static final String cynologistRecommendationsButtonText = "Проверенные кинологи";

    private static final String START_COMMAND = "/start";

    private static final String HELP_ADMIN_COMMAND = "/adminhelp";

    /**
     * Обработка команды {@value this#START_COMMAND}. Выдаёт стартовое меню
     */
    @Command(name = START_COMMAND)
    public SendMessage startMenu(Message message) {
        return new SendMessage(message.from().id(), startMenuText)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton(dogButtonText).callbackData(Callbacks.DOG_MENU.name()),
                        new InlineKeyboardButton(catButtonText).callbackData(Callbacks.CAT_MENU.name())
                ));
    }

    /**
     * Обработка коллбека START_MENU. Выдаёт стартовое меню
     */
    @Callback(name = Callbacks.START_MENU)
    public SendMessage startMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), startMenuText)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton(dogButtonText).callbackData(Callbacks.DOG_MENU.name()),
                        new InlineKeyboardButton(catButtonText).callbackData(Callbacks.CAT_MENU.name())
                ));
    }

    /**
     * Обработка коллбека DOG_MENU. Выдаёт основное меню для собаки
     */
    @Callback(name = Callbacks.DOG_MENU)
    public SendMessage dogMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), dogMenuText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(shelterCommonInfoButtonText).callbackData(Callbacks.DOG_INFO_MENU.name()))
                        .addRow(new InlineKeyboardButton(adoptionInfoButtonText).callbackData(Callbacks.DOG_ADOPTION_INFO_MENU.name()))
                        .addRow(new InlineKeyboardButton(keepingPetButtonText).callbackData(Callbacks.DOG_REPORT.name()))
                        .addRow(
                                new InlineKeyboardButton(callVolunteerButtonText).callbackData(Callbacks.DOG_CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(backButtonText).callbackData(Callbacks.START_MENU.name())
                        )
                );
    }

    /**
     * Обработка коллбека CAT_MENU. Выдаёт основное меню для кошки
     */
    @Callback(name = Callbacks.CAT_MENU)
    public SendMessage catMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), catMenuText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(shelterCommonInfoButtonText).callbackData(Callbacks.CAT_INFO_MENU.name()))
                        .addRow(new InlineKeyboardButton(adoptionInfoButtonText).callbackData(Callbacks.CAT_ADOPTION_INFO_MENU.name()))
                        .addRow(new InlineKeyboardButton(keepingPetButtonText).callbackData(Callbacks.CAT_REPORT.name()))
                        .addRow(
                                new InlineKeyboardButton(callVolunteerButtonText).callbackData(Callbacks.CAT_CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(backButtonText).callbackData(Callbacks.START_MENU.name())
                        )
                );
    }

    /**
     * Обработка коллбека DOG_INFO_MENU. Выдаёт информационное меню для собаки
     */
    @Callback(name = Callbacks.DOG_INFO_MENU)
    public SendMessage dogInfoMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), dogShelterInfoText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(shelterInfoButtonText).callbackData(Callbacks.DOG_SHELTER_INFO.name()))
                        .addRow(new InlineKeyboardButton(scheduleButtonText).callbackData(Callbacks.DOG_SCHEDULE_INFO.name()))
                        .addRow(new InlineKeyboardButton(carButtonText).callbackData(Callbacks.DOG_CAR_INFO.name()))
                        .addRow(new InlineKeyboardButton(safetyInfoButtonText).callbackData(Callbacks.DOG_SHELTER_SAFETY_INFO.name()))
                        .addRow(new InlineKeyboardButton(getContactsButtonText).callbackData(Callbacks.DOG_CONTACT_INFO.name()))
                        .addRow(
                                new InlineKeyboardButton(callVolunteerButtonText).callbackData(Callbacks.DOG_CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(backButtonText).callbackData(Callbacks.DOG_MENU.name())
                        )
                );
    }

    /**
     * Обработка коллбека CAT_INFO_MENU. Выдаёт информационное меню для кошки
     */
    @Callback(name = Callbacks.CAT_INFO_MENU)
    public SendMessage catInfoMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), catShelterInfoText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(shelterInfoButtonText).callbackData(Callbacks.CAT_SHELTER_INFO.name()))
                        .addRow(new InlineKeyboardButton(scheduleButtonText).callbackData(Callbacks.CAT_SCHEDULE_INFO.name()))
                        .addRow(new InlineKeyboardButton(carButtonText).callbackData(Callbacks.CAT_CAR_INFO.name()))
                        .addRow(new InlineKeyboardButton(safetyInfoButtonText).callbackData(Callbacks.CAT_SHELTER_SAFETY_INFO.name()))
                        .addRow(new InlineKeyboardButton(getContactsButtonText).callbackData(Callbacks.CAT_CONTACT_INFO.name()))
                        .addRow(
                                new InlineKeyboardButton(callVolunteerButtonText).callbackData(Callbacks.CAT_CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(backButtonText).callbackData(Callbacks.CAT_MENU.name())
                        )
                );
    }

    /**
     * Обработка коллбека DOG_ADOPTION_INFO_MENU. Выдаёт меню усыновления для собаки
     */
    @Callback(name = Callbacks.DOG_ADOPTION_INFO_MENU)
    public SendMessage dogAdoptionInfoMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), dogAdoptionInfoText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(firstMeetingRulesButtonText).callbackData(Callbacks.DOG_MEETING_RULES_INFO.name()))
                        .addRow(new InlineKeyboardButton(documentListButtonText).callbackData(Callbacks.DOG_DOCUMENT_LIST.name()))
                        .addRow(new InlineKeyboardButton(transportationRecommendationsButtonText).callbackData(Callbacks.DOG_TRANSPORTATION_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(youngHousingRecommendationsButtonText).callbackData(Callbacks.DOG_YOUNG_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(housingRecommendationsButtonText).callbackData(Callbacks.DOG_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(disabledHousingRecommendationsButtonText).callbackData(Callbacks.DOG_DISABLED_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(declineCausesButtonText).callbackData(Callbacks.DOG_DECLINE_CAUSES.name()))
                        .addRow(new InlineKeyboardButton(cynologistAdvicesButtonText).callbackData(Callbacks.DOG_CYNOLOGIST_ADVICES.name()))
                        .addRow(new InlineKeyboardButton(cynologistRecommendationsButtonText).callbackData(Callbacks.DOG_CYNOLOGIST_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(getContactsButtonText).callbackData(Callbacks.DOG_CONTACT_INFO.name()))
                        .addRow(
                                new InlineKeyboardButton(callVolunteerButtonText).callbackData(Callbacks.DOG_CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(backButtonText).callbackData(Callbacks.DOG_MENU.name())
                        )
                );
    }

    /**
     * Обработка коллбека CAT_ADOPTION_INFO_MENU. Выдаёт меню усыновления для кошки
     */
    @Callback(name = Callbacks.CAT_ADOPTION_INFO_MENU)
    public SendMessage catAdoptionInfoMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), catAdoptionInfoText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(firstMeetingCatRulesButtonText).callbackData(Callbacks.CAT_MEETING_RULES_INFO.name()))
                        .addRow(new InlineKeyboardButton(documentListButtonText).callbackData(Callbacks.CAT_DOCUMENT_LIST.name()))
                        .addRow(new InlineKeyboardButton(transportationRecommendationsButtonText).callbackData(Callbacks.CAT_TRANSPORTATION_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(youngHousingRecommendationsButtonText).callbackData(Callbacks.CAT_YOUNG_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(housingRecommendationsButtonText).callbackData(Callbacks.CAT_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(disabledHousingRecommendationsButtonText).callbackData(Callbacks.CAT_DISABLED_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(declineCausesButtonText).callbackData(Callbacks.CAT_DECLINE_CAUSES.name()))
                        .addRow(new InlineKeyboardButton(getContactsButtonText).callbackData(Callbacks.CAT_CONTACT_INFO.name()))
                        .addRow(
                                new InlineKeyboardButton(callVolunteerButtonText).callbackData(Callbacks.CAT_CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(backButtonText).callbackData(Callbacks.CAT_MENU.name())
                        )
                );
    }

    /**
     * Обработка команды HELP_ADMIN_COMMAND. Выдаёт меню c информацией, как управлять и пользоваться ботом
     */
    @Command(name = HELP_ADMIN_COMMAND)
    public SendMessage adminHelpMenu(Message message) {
        //   if (VOLUNTEER_CHAT_ID == message.from().id())
        return new SendMessage(message.from().id(), AdminMenuText);
    }

}
