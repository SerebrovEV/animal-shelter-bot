package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений с контроллера
 * {@link com.animalshelter.animalshelterbot.controllers.BotUserController}
 * и подготовки ответного сообщения пользователю</i>
 */

@Service
@RequiredArgsConstructor
public class ValidatorBotUserService {

    private final BotUserService botUserService;
    private final Pattern pattern = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");

    public String validateUser(Message message){
        Matcher matcher = pattern.matcher(message.text());
        matcher.find();
        String name = matcher.group(3);
        if(!matcher.group(1).startsWith("8")){
          return "Некорректный номер телефона";
        }
        long phone = Long.parseLong(matcher.group(1));
        long idUser = message.from().id();
        if(botUserService.getBotUser(idUser) != null){
            return "Данный пользователь уже есть";
        }
        BotUser botUser = new BotUser(name, phone, idUser);
        botUserService.addBotUser(botUser);
        return "Добавлена запись контакта: " + botUser.toString();
    }

    public String validateGetUser(Message message){
        BotUser botUser = botUserService.getBotUser(message.from().id());
        if (botUser != null) {
            return botUser.toString();
        }
        return "Клиент не найден! Пожалуйста добавьте контакты для обратной связи или" +
                " запросите вызов волонтера. Спасибо!";
    }
}
