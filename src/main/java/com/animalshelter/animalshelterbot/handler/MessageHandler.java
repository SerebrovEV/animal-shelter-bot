package com.animalshelter.animalshelterbot.handler;

import com.animalshelter.animalshelterbot.listener.TelegramBotUpdateListener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
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

    /**
     * Обработка сообщения от {@link TelegramBotUpdateListener}
     * */
    public void handleMessage(Message message) {
        if(message.text() == null && message.caption() == null) {
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

    /**
     * Обработка контроллера при получении сообщения от {@link TelegramBotUpdateListener}
     * */
    public void processController(CommandController commandController, Message message) throws InvocationTargetException, IllegalAccessException {
        for(Method method: commandController.getClass().getDeclaredMethods()){
            if(!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            Command annotation = method.getAnnotation(Command.class);

            String text;

            if(message.text() != null) {
                text = message.text();
            } else {
                text = message.caption();
            }

            Pattern pattern = Pattern.compile(annotation.pattern());
            Matcher matcher = pattern.matcher(text);

            if(annotation.chatId() != 0 && annotation.chatId() != message.chat().id()) {
                continue;
            }

            if(!(annotation.name().equals(text) || matcher.matches())) {
                continue;
            }

            ((List<SendMessage>) (
                    (method.getReturnType() == SendMessage.class) ?
                            List.of((SendMessage) method.invoke(commandController, message)):
                            method.invoke(commandController, message)
            )).forEach(bot::execute);
        }
    }

    /**
     * Обработка коллбека от {@link TelegramBotUpdateListener}
     * */
    public void handleCallback(CallbackQuery callbackQuery) throws InvocationTargetException, IllegalAccessException {
        if(callbackQuery.data() == null) {
            return;
        }

        for(CommandController commandController: controllers) {
            this.processController(commandController, callbackQuery);
        }
    }

    /**
     * Обработка контроллера при получении коллбека от {@link TelegramBotUpdateListener}
     * */
    public void processController(CommandController commandController, CallbackQuery callbackQuery) throws InvocationTargetException, IllegalAccessException {
        for(Method method: commandController.getClass().getDeclaredMethods()){
            if(!method.isAnnotationPresent(Callback.class)) {
                continue;
            }

            Callback annotation = method.getAnnotation(Callback.class);

            if(annotation.chatId() != 0 && annotation.chatId() != callbackQuery.from().id()) {
                continue;
            }

            if(!annotation.name().name().equals(callbackQuery.data())) {
                continue;
            }

            ((List<SendMessage>) (
                    (method.getReturnType() == SendMessage.class) ?
                            List.of((SendMessage) method.invoke(commandController, callbackQuery)):
                            method.invoke(commandController, callbackQuery)
            )).forEach(bot::execute);
        }
    }

    // если пришло фото
    public void handlePhoto(Message message) {
        for(CommandController commandController: controllers) {
            try {
                this.processControllerForPhoto(commandController, message);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    public void processControllerForPhoto(CommandController commandController, Message message) throws InvocationTargetException, IllegalAccessException {
        for(Method method: commandController.getClass().getDeclaredMethods()){
            if(!method.isAnnotationPresent(Photo.class)) {
                continue;
            }
            SendMessage sendMessage = (SendMessage) method.invoke(commandController, message);

            SendResponse sendResponse = bot.execute(sendMessage);
        }
    }
}
