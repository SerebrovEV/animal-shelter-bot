package com.animalshelter.animalshelterbot.handler;

import com.animalshelter.animalshelterbot.controllers.UserController;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final List<CommandController> controllers;
    private final TelegramBot bot;
    private final Pattern pattern = Pattern.compile("([\\d]{11})(\\s)([\\W]+)");
    private final UserController userController;

    public void handleMessage(Message message) throws InvocationTargetException, IllegalAccessException {
        if(message.text() == null) {
            return;
        }
        Matcher matcher = pattern.matcher(message.text());
        if (matcher.matches()){
            SendMessage sendMessage = userController.addBotUser(message);
            bot.execute(sendMessage);
            return;
        }
        for(CommandController commandController: controllers) {
            for(Method method: commandController.getClass().getDeclaredMethods()){
                if(!method.isAnnotationPresent(Command.class)) {
                    continue;
                }

                Command annotation = method.getAnnotation(Command.class);

                if(!annotation.name().equals(message.text())) {
                    continue;
                }

                SendMessage sendMessage = (SendMessage) method.invoke(commandController, message);

                SendResponse sendResponse = bot.execute(sendMessage);

                return;
            }
        }
    }

    public void handleCallback(CallbackQuery callbackQuery) throws InvocationTargetException, IllegalAccessException {
        if(callbackQuery.data() == null) {
            return;
        }

        for(CommandController commandController: controllers) {
            for(Method method: commandController.getClass().getDeclaredMethods()){
                if(!method.isAnnotationPresent(Callback.class)) {
                    continue;
                }

                Callback annotation = method.getAnnotation(Callback.class);

                if(!annotation.name().equals(callbackQuery.data())) {
                    continue;
                }

                SendMessage sendMessage = (SendMessage) method.invoke(commandController, callbackQuery);

                SendResponse sendResponse = bot.execute(sendMessage);

                return;
            }
        }
    }
}
