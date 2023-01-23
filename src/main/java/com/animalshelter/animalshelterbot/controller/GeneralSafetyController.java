package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callback;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <i>Контроллер получения информации по общим рекомендациям по технике безопасности.</i>
 * Запрос через {@link Update#callbackQuery()} осуществляется по значению {@link Callback#DOG_SHELTER_SAFETY_INFO}
 * и {@link Callback#CAT_SHELTER_SAFETY_INFO}
 */
@Component
@RequiredArgsConstructor
public class GeneralSafetyController implements CommandController {
    private final String generalSafetyInfoText =
            "Рекомендации по технике безопасности:\n"
                    + "    - удобная обувь с нескользящей подошвой;\n"
                    + "    - одежда закрытого типа;\n"
                    + "    - за ограждения не заходить;\n"
                    + "    - животных с рук не кормить;\n"
                    + "    - в вольеры c животными руки не совать.";


    private static final String BACK_BUTTON_TEXT = "Назад";


    @com.animalshelter.animalshelterbot.handler.Callback(name = Callback.DOG_SHELTER_SAFETY_INFO)
    public SendMessage handleCallbackSafetyDogRules(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), generalSafetyInfoText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callback.DOG_INFO_MENU.name()
                        )));
    }


    @com.animalshelter.animalshelterbot.handler.Callback(name = Callback.CAT_SHELTER_SAFETY_INFO)
    public SendMessage handleCallbackSafetyCatRules(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), generalSafetyInfoText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(BACK_BUTTON_TEXT).callbackData(Callback.CAT_INFO_MENU.name()
                        )));
    }
}
