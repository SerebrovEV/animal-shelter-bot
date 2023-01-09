package com.animalshelter.animalshelterbot.handler;

import com.animalshelter.animalshelterbot.listener.TelegramBotUpdateListener;
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

/**
 * Класс, распределяющий события, поступающие от {@link TelegramBotUpdateListener}, между классами, имплементирующими интерфейс {@link CommandController}
 * */
@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final List<CommandController> controllers;
    private final TelegramBot bot;

    public void handleMessage(Message message) {
        if(message.text() == null) {
            return;
        }

        for(CommandController commandController: controllers) {
            try {
                this.processController(commandController, message);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void processController(CommandController commandController, Message message) throws InvocationTargetException, IllegalAccessException {
        for(Method method: commandController.getClass().getDeclaredMethods()){
            if(!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            Command annotation = method.getAnnotation(Command.class);

            Pattern pattern = Pattern.compile(annotation.pattern());
            Matcher matcher = pattern.matcher(message.text());

            if(annotation.chatId() != 0 && annotation.chatId() != message.chat().id()) {
                continue;
            }

            if(!(annotation.name().equals(message.text()) || matcher.matches())) {
                continue;
            }

            SendMessage sendMessage = (SendMessage) method.invoke(commandController, message);
            SendResponse sendResponse = bot.execute(sendMessage);
        }
    }

    public void handleCallback(CallbackQuery callbackQuery) throws InvocationTargetException, IllegalAccessException {
        if(callbackQuery.data() == null) {
            return;
        }

        for(CommandController commandController: controllers) {
            this.processController(commandController, callbackQuery);
        }
    }

    public void processController(CommandController commandController, CallbackQuery callbackQuery) throws InvocationTargetException, IllegalAccessException {
        for(Method method: commandController.getClass().getDeclaredMethods()){
            if(!method.isAnnotationPresent(Callback.class)) {
                continue;
            }

            Callback annotation = method.getAnnotation(Callback.class);

            if(annotation.chatId() != 0 && annotation.chatId() != callbackQuery.from().id()) {
                continue;
            }

            if(!annotation.name().equals(callbackQuery.data())) {
                continue;
            }

            SendMessage sendMessage = (SendMessage) method.invoke(commandController, callbackQuery);

            SendResponse sendResponse = bot.execute(sendMessage);
        }
    }
}
