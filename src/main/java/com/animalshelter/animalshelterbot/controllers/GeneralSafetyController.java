package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <i>Контроллер получения информации по общим рекомендациям по технике безопасности.</i>
 * <br>
 * Запрос через {@link Update#message()} осуществляется по команде {@link #GENERAL_SAFETY_COMMAND}
 * <br>
 * Запрос через {@link Update#callbackQuery()} осуществляется по значению  {@link #GENERAL_SAFETY_CALLBACK}
 */
@Component
@RequiredArgsConstructor
public class GeneralSafetyController implements CommandController {
    private final String generalSafetyInfoText =
            "Рекоменации по технике безопасности:\n"
                    + "    - удобная обувь с нескользящей подошвой;\n"
                    + "    - одежда закрытого типа;\n"
                    + "    - за ограждения не заходить;\n"
                    + "    - животных с рук не кормить;\n"
                    + "    - в вальеры c животными руки не сувать.";

    public static final String GENERAL_SAFETY_COMMAND = "/safetyInfo";
    public static final String GENERAL_SAFETY_CALLBACK = "/safetyInfo";

    @Command(name = GENERAL_SAFETY_COMMAND)
    public SendMessage handleSafetyMessage(Message message) {

        return new SendMessage(message.from().id(), generalSafetyInfoText);
    }

    @Callback(name = Callbacks.DOG_SHELTER_SAFETY_INFO)
    public SendMessage handleCallbackMessage(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), generalSafetyInfoText);
    }
}
