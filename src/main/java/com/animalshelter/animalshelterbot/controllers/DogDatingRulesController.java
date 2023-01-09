package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <i>Контроллер получения информации по правилам знакомства с собакой.</i>
 * <br>
 * Запрос через {@link Update#callbackQuery()} осуществляется по значению  {@link #DOG_DATING_RULES_CALLBACK}
 */
@Component
@RequiredArgsConstructor
public class DogDatingRulesController implements CommandController {


    private final String dogDatingRulesText =
            "*Правила знакомства с собакой:*\n\n"
                    + "  *Что нельзя делать:*\n"
                    + "    - Не нависайте над собакой (не наклоняйтесь над ней);\n"
                    + "    - Не нужно смотреть пристально собаке в глаза;\n"
                    + "    - Не нужно обнимать и прижимать к себе собаку;\n"
                    + "    - Не нужно целовать ее;\n"
                    + "    - Не трогайте за голову, и не гладьте по голове;\n"
                    + "    - Не надо кричать, визжать...;\n"
                    + "    - Не нужно приближаться быстро и лоб в лоб;\n\n"
                    + "  *Как правильно:*\n"
                    + "    - Дайте собаке возможность самой подойти к вам. Если вам нужно подойти к ней, делайте это немного как бы по дуге и не торопясь.\n"
                    + "    - Дайте собаке возможность обнюхать вас. Если она сильно боится, можно присесть на корточки, но, не наклоняясь над ней (если собачка маленькая).\n"
                    + "    - Избегайте взгляда глаза в глаза.\n"
                    + "    - Можете погладить по бокам, щечкам, груди, если она не сопротивляется — по спине.\n"
                    + "    - Если волонтер не против, можете угостить лакомством на открытой ладони.\n";

    public static final String DOG_DATING_RULES_CALLBACK = "dogDatingRules";

    @Callback(name = DOG_DATING_RULES_CALLBACK)
    public SendMessage handleCallbackMessage(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), dogDatingRulesText)
                .parseMode(ParseMode.Markdown);
    }
}
