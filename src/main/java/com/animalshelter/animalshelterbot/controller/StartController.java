package com.animalshelter.animalshelterbot.controller;

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

/**
 * <i>Контроллер обработки навигации по боту. Отвечает за ответ на все {@link Callback}`и-меню</i> <br>
 */
@Component
@RequiredArgsConstructor
public class StartController implements CommandController {

    private static final String START_MENU_TEXT = "Привет! Данный бот может помочь вам взять и содержать животное из приюта. Для продолжения выберите животное:";
    private static final String ADMIN_MENU_TEXT = "Основные команды для администратора:\n" +
            "/infoAboutAdminCat - команда для вызова меню с информацией о работе с кошками из приюта для кошек;\n" +
            "/infoAboutAdminDog - команда для вызова меню с информацией о работе с  собаками из приюта для собак;\n" +
            "/infoAboutAdminCatUser - команда для вызова меню с информацией о работе с усыновителями из приюта для кошек;\n" +
            "/infoAboutAdminDogUser - команда для вызова меню с информацией о работе с усыновителями из приюта для собак;\n" +
            "/getCatReports - команда для вызова меню с информацией о работе с отчетами из приюта для кошек;\n" +
            "/getDogReports - команда для вызова меню с информацией о работе с отчетами из приюта для собак.";

    private static final String DOG_MENU_TEXT = "Вы выбрали приют для собак. Для продолжения выберите раздел:";
    private static final String DOG_SHELTER_INFO_TEXT = "В данном разделе можно получить информацию о приюте. Выберите, какую информацию вы хотите получить:";
    private static final String DOG_ADOPTION_INFO_TEXT = "В данном разделе можно получить информацию об усыновлении собаки. Выберите, какую информацию вы хотите получить:";

    private static final String CAT_MENU_TEXT = "Вы выбрали приют для кошек. Для продолжения выберите раздел:";
    private static final String CAT_SHELTER_INFO_TEXT = "В данном разделе можно получить информацию о приюте. Выберите, какую информацию вы хотите получить:";
    private static final String CAT_ADOPTION_INFO_TEXT = "В данном разделе можно получить информацию об усыновлении кошки. Выберите, какую информацию вы хотите получить:";

    private static final String BACK_BUTTON_TEXT = "Назад";

    private static final String DOG_BUTTON_TEXT = "Собака";
    private static final String CAT_BUTTON_TEXT = "Кошка";

    private static final String SHELTER_COMMON_INFO_BUTTON_TEXT = "Узнать информацию о приюте";
    private static final String ADOPTION_INFO_BUTTON_TEXT = "Как взять питомца из приюта";
    private static final String KEEPING_PET_BUTTON_TEXT = "Ведение питомца";
    private static final String CALL_VOLUNTEER_BUTTON_TEXT = "Позвать волонтёра";

    private static final String SHELTER_INFO_BUTTON_TEXT = "Узнать информацию о приюте";
    private static final String SCHEDULE_BUTTON_TEXT = "Контактные данные";
    private static final String CAR_BUTTON_TEXT = "Данные для оформления пропуска";
    private static final String SAFETY_INFO_BUTTON_TEXT = "Рекомендации о ТБ на территории приюта";
    private static final String GET_CONTACTS_BUTTON_TEXT = "Записать контактные данные";

    private static final String FIRST_MEETING_RULES_BUTTON_TEXT = "Правила знакомства с собакой";
    private static final String FIRST_MEETING_CAT_RULES_BUTTON_TEXT = "Правила знакомства с кошкой";
    private static final String DOCUMENT_LIST_BUTTON_TEXT = "Список необходимых документов";
    private static final String TRANSPORTATION_RECOMMENDATIONS_BUTTON_TEXT = "Рекомендации по транспортировке";
    private static final String YOUNG_HOUSING_RECOMMENDATIONS_BUTTON_TEXT = "Рекомендации по обустройству дома для детёныша";
    private static final String HOUSING_RECOMMENDATIONS_BUTTON_TEXT = "Рекомендации по обустройству дома";
    private static final String DISABLED_HOUSING_RECOMMENDATIONS_BUTTON_TEXT = "Рекомендации по обустройству дома для животного с огр. возможностями";
    private static final String DECLINE_CAUSES_BUTTON_TEXT = "Список причин отказа";
    private static final String CYNOLOGIST_ADVICES_BUTTON_TEXT = "Советы кинолога";
    private static final String CYNOLOGIST_RECOMMENDATIONS_BUTTON_TEXT = "Проверенные кинологи";

    private static final String START_COMMAND = "/start";

    private static final String HELP_ADMIN_COMMAND = "/adminhelp";

    /**
     * Обработка команды {@value this#START_COMMAND}. Выдаёт стартовое меню
     */
    @Command(name = START_COMMAND)
    public SendMessage startMenu(Message message) {
        return new SendMessage(message.from().id(), START_MENU_TEXT)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton(DOG_BUTTON_TEXT).callbackData(Callbacks.DOG_MENU.name()),
                        new InlineKeyboardButton(CAT_BUTTON_TEXT).callbackData(Callbacks.CAT_MENU.name())
                ));
    }

    /**
     * Обработка коллбека START_MENU. Выдаёт стартовое меню
     */
    @Callback(name = Callbacks.START_MENU)
    public SendMessage startMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), START_MENU_TEXT)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton(DOG_BUTTON_TEXT).callbackData(Callbacks.DOG_MENU.name()),
                        new InlineKeyboardButton(CAT_BUTTON_TEXT).callbackData(Callbacks.CAT_MENU.name())
                ));
    }

    /**
     * Обработка коллбека DOG_MENU. Выдаёт основное меню для собаки
     */
    @Callback(name = Callbacks.DOG_MENU)
    public SendMessage dogMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), DOG_MENU_TEXT)
                .replyMarkup(new InlineKeyboardMarkup()
                    .addRow(new InlineKeyboardButton(SHELTER_COMMON_INFO_BUTTON_TEXT).callbackData(Callbacks.DOG_INFO_MENU.name()))
                    .addRow(new InlineKeyboardButton(ADOPTION_INFO_BUTTON_TEXT).callbackData(Callbacks.DOG_ADOPTION_INFO_MENU.name()))
                    .addRow(new InlineKeyboardButton(KEEPING_PET_BUTTON_TEXT).callbackData(Callbacks.DOG_REPORT.name()))
                    .addRow(
                            new InlineKeyboardButton(CALL_VOLUNTEER_BUTTON_TEXT).callbackData(Callbacks.CALL_VOLUNTEER.name()),
                            new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.START_MENU.name())
                    )
                );
    }

    /**
     * Обработка коллбека CAT_MENU. Выдаёт основное меню для кошки
     */
    @Callback(name = Callbacks.CAT_MENU)
    public SendMessage catMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), CAT_MENU_TEXT)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(SHELTER_COMMON_INFO_BUTTON_TEXT).callbackData(Callbacks.CAT_INFO_MENU.name()))
                        .addRow(new InlineKeyboardButton(ADOPTION_INFO_BUTTON_TEXT).callbackData(Callbacks.CAT_ADOPTION_INFO_MENU.name()))
                        .addRow(new InlineKeyboardButton(KEEPING_PET_BUTTON_TEXT).callbackData(Callbacks.CAT_REPORT.name()))
                        .addRow(
                                new InlineKeyboardButton(CALL_VOLUNTEER_BUTTON_TEXT).callbackData(Callbacks.CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.START_MENU.name())
                        )
                );
    }

    /**
     * Обработка коллбека DOG_INFO_MENU. Выдаёт информационное меню для собаки
     */
    @Callback(name = Callbacks.DOG_INFO_MENU)
    public SendMessage dogInfoMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), DOG_SHELTER_INFO_TEXT)
                .replyMarkup(new InlineKeyboardMarkup()
                    .addRow(new InlineKeyboardButton(SHELTER_INFO_BUTTON_TEXT).callbackData(Callbacks.DOG_SHELTER_INFO.name()))
                    .addRow(new InlineKeyboardButton(SCHEDULE_BUTTON_TEXT).callbackData(Callbacks.DOG_SCHEDULE_INFO.name()))
                    .addRow(new InlineKeyboardButton(CAR_BUTTON_TEXT).callbackData(Callbacks.DOG_CAR_INFO.name()))
                    .addRow(new InlineKeyboardButton(SAFETY_INFO_BUTTON_TEXT).callbackData(Callbacks.DOG_SHELTER_SAFETY_INFO.name()))
                    .addRow(new InlineKeyboardButton(GET_CONTACTS_BUTTON_TEXT).callbackData(Callbacks.DOG_CONTACT_INFO.name()))
                    .addRow(
                            new InlineKeyboardButton(CALL_VOLUNTEER_BUTTON_TEXT).callbackData(Callbacks.CALL_VOLUNTEER.name()),
                            new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.DOG_MENU.name())
                    )
                );
    }

    /**
     * Обработка коллбека CAT_INFO_MENU. Выдаёт информационное меню для кошки
     */
    @Callback(name = Callbacks.CAT_INFO_MENU)
    public SendMessage catInfoMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), CAT_SHELTER_INFO_TEXT)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(SHELTER_INFO_BUTTON_TEXT).callbackData(Callbacks.CAT_SHELTER_INFO.name()))
                        .addRow(new InlineKeyboardButton(SCHEDULE_BUTTON_TEXT).callbackData(Callbacks.CAT_SCHEDULE_INFO.name()))
                        .addRow(new InlineKeyboardButton(CAR_BUTTON_TEXT).callbackData(Callbacks.CAT_CAR_INFO.name()))
                        .addRow(new InlineKeyboardButton(SAFETY_INFO_BUTTON_TEXT).callbackData(Callbacks.CAT_SHELTER_SAFETY_INFO.name()))
                        .addRow(new InlineKeyboardButton(GET_CONTACTS_BUTTON_TEXT).callbackData(Callbacks.CAT_CONTACT_INFO.name()))
                        .addRow(
                                new InlineKeyboardButton(CALL_VOLUNTEER_BUTTON_TEXT).callbackData(Callbacks.CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.CAT_MENU.name())
                        )
                );
    }

    /**
     * Обработка коллбека DOG_ADOPTION_INFO_MENU. Выдаёт меню усыновления для собаки
     */
    @Callback(name = Callbacks.DOG_ADOPTION_INFO_MENU)
    public SendMessage dogAdoptionInfoMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), DOG_ADOPTION_INFO_TEXT)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(FIRST_MEETING_RULES_BUTTON_TEXT).callbackData(Callbacks.DOG_MEETING_RULES_INFO.name()))
                        .addRow(new InlineKeyboardButton(DOCUMENT_LIST_BUTTON_TEXT).callbackData(Callbacks.DOG_DOCUMENT_LIST.name()))
                        .addRow(new InlineKeyboardButton(TRANSPORTATION_RECOMMENDATIONS_BUTTON_TEXT).callbackData(Callbacks.DOG_TRANSPORTATION_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(YOUNG_HOUSING_RECOMMENDATIONS_BUTTON_TEXT).callbackData(Callbacks.DOG_YOUNG_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(HOUSING_RECOMMENDATIONS_BUTTON_TEXT).callbackData(Callbacks.DOG_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(DISABLED_HOUSING_RECOMMENDATIONS_BUTTON_TEXT).callbackData(Callbacks.DOG_DISABLED_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(DECLINE_CAUSES_BUTTON_TEXT).callbackData(Callbacks.DOG_DECLINE_CAUSES.name()))
                        .addRow(new InlineKeyboardButton(CYNOLOGIST_ADVICES_BUTTON_TEXT).callbackData(Callbacks.DOG_CYNOLOGIST_ADVICES.name()))
                        .addRow(new InlineKeyboardButton(CYNOLOGIST_RECOMMENDATIONS_BUTTON_TEXT).callbackData(Callbacks.DOG_CYNOLOGIST_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(GET_CONTACTS_BUTTON_TEXT).callbackData(Callbacks.DOG_CONTACT_INFO.name()))
                        .addRow(
                                new InlineKeyboardButton(CALL_VOLUNTEER_BUTTON_TEXT).callbackData(Callbacks.CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.DOG_MENU.name())
                        )
                );
    }

    /**
     * Обработка коллбека CAT_ADOPTION_INFO_MENU. Выдаёт меню усыновления для кошки
     */
    @Callback(name = Callbacks.CAT_ADOPTION_INFO_MENU)
    public SendMessage catAdoptionInfoMenu(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), CAT_ADOPTION_INFO_TEXT)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(FIRST_MEETING_CAT_RULES_BUTTON_TEXT).callbackData(Callbacks.CAT_MEETING_RULES_INFO.name()))
                        .addRow(new InlineKeyboardButton(DOCUMENT_LIST_BUTTON_TEXT).callbackData(Callbacks.CAT_DOCUMENT_LIST.name()))
                        .addRow(new InlineKeyboardButton(TRANSPORTATION_RECOMMENDATIONS_BUTTON_TEXT).callbackData(Callbacks.CAT_TRANSPORTATION_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(YOUNG_HOUSING_RECOMMENDATIONS_BUTTON_TEXT).callbackData(Callbacks.CAT_YOUNG_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(HOUSING_RECOMMENDATIONS_BUTTON_TEXT).callbackData(Callbacks.CAT_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(DISABLED_HOUSING_RECOMMENDATIONS_BUTTON_TEXT).callbackData(Callbacks.CAT_DISABLED_HOUSING_RECOMMENDATION.name()))
                        .addRow(new InlineKeyboardButton(DECLINE_CAUSES_BUTTON_TEXT).callbackData(Callbacks.CAT_DECLINE_CAUSES.name()))
                        .addRow(new InlineKeyboardButton(GET_CONTACTS_BUTTON_TEXT).callbackData(Callbacks.CAT_CONTACT_INFO.name()))
                        .addRow(
                                new InlineKeyboardButton(CALL_VOLUNTEER_BUTTON_TEXT).callbackData(Callbacks.CALL_VOLUNTEER.name()),
                                new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callbacks.CAT_MENU.name())
                        )
                );
    }

    /**
     * Обработка команды HELP_ADMIN_COMMAND. Выдаёт меню c информацией, как управлять и пользоваться ботом
     */
    @Command(name = HELP_ADMIN_COMMAND)
    public SendMessage adminHelpMenu(Message message) {
         return new SendMessage(message.from().id(), ADMIN_MENU_TEXT);
    }

}
