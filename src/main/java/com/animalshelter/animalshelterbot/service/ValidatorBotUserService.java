package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений с контроллера
 * {@link com.animalshelter.animalshelterbot.controllers.BotUserController} из телеграма
 * и подготовки ответного сообщения пользователю</i>
 */

@Service
@RequiredArgsConstructor
public class ValidatorBotUserService {

    private final BotUserService botUserService;
    private final Pattern pattern = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");

    /**
     *<i> Метод для проверки и обработки входящего сообщения от пользователя.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.BotUserController#addBotUser(Message)}. </i>
      * @param message
     * @return String в зависимости от результата обработки
     */
    public String validateUser(Message message){
        Matcher matcher = pattern.matcher(message.text());
        matcher.find();
        String name = matcher.group(3);
        if(!matcher.group(1).startsWith("8")){
          return "Некорректный номер телефона";
        }
        long phone = Long.parseLong(matcher.group(1));
        long idUser = message.from().id();
        if(botUserService.getBotUserByChatId(idUser) != null){
            return "Данный пользователь уже есть";
        }
        BotUser botUser = botUserService.addBotUser(new BotUser(name, phone, idUser));
        return "Добавлена запись контакта: " + botUser;
    }

    /**
     * <i>Метод для проверки входящего сообщения от пользователя для проверки контакта.
     * <br>
     * Запрос выполняется через метод {@link com.animalshelter.animalshelterbot.controllers.BotUserController#getContactMessage(Message)}.</i>
     * @param message
     * @return String в зависимости от проверки сообщения
     */
    public String validateGetUser(Message message){
        BotUser botUser = botUserService.getBotUserByChatId(message.from().id());
        if (botUser != null) {
            return botUser.toString();
        }
        return "Клиент не найден! Пожалуйста добавьте контакты для обратной связи или" +
                " запросите вызов волонтера. Спасибо!";
    }
}
