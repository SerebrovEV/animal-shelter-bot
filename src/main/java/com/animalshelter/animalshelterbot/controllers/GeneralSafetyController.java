package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <i>Контроллер получения информации по общим рекомендациям по технике безопасности.</i>
 * Запрос через {@link Update#callbackQuery()} осуществляется по значению {@link Callbacks#DOG_SHELTER_SAFETY_INFO}
 * и {@link Callbacks#CAT_SHELTER_SAFETY_INFO}
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


    private static final String backButtonText = "Назад";


    @Callback(name = Callbacks.DOG_SHELTER_SAFETY_INFO)
    public SendMessage handleCallbackSafetyDogRules(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), generalSafetyInfoText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(backButtonText).callbackData(Callbacks.DOG_INFO_MENU.name()
                        )));
    }


    @Callback(name = Callbacks.CAT_SHELTER_SAFETY_INFO)
    public SendMessage handleCallbackSafetyCatRules(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), generalSafetyInfoText)
                .replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(backButtonText).callbackData(Callbacks.CAT_INFO_MENU.name()
                        )));
    }
}
