package com.animalshelter.animalshelterbot.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Интерфейс-аннотация, указвающая на функцию ответа на {@link Message} <br>
 * Прменима к методам <br>
 * Метод должен принимать {@link Message} и возвращать {@link SendMessage} <br>
 * В качестве аргумента в аннотацию необходимо передать либо название команды {@link Command#name()}, либо паттрен команды {@link Command#pattern()}
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    /**
     * Значение {@link Message#text()}, при котором будет вызван метод, помеченный аннотацией
     * */
    String name() default "";
    /**
     * Паттерн {@link Message#text()}, при котором будет вызван метод, помеченный аннотацией
     * */
    String pattern() default "";
}
