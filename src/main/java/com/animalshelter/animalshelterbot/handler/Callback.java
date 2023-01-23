package com.animalshelter.animalshelterbot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Интерфейс-аннотация, указвающая на функцию ответа на {@link CallbackQuery} <br>
 * Прменима к методам <br>
 * Метод должен принимать {@link CallbackQuery} и возвращать {@link SendMessage} <br>
 * В качестве аргумента в аннотацию необходимо передать коллбек команды {@link Callback#name()} и id чата {@link Callback#chatId()}
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Callback {
    /**
     * Значение {@link CallbackQuery#data()}, при котором будет вызван метод, помеченный аннотацией
     * */
    com.animalshelter.animalshelterbot.organisation.Callback name();
    /**
     * ID чата {@link Message#chat()#chatId()}, при котором будет вызван метод, помеченный аннотацией
     * */
    int chatId() default 0;
}
