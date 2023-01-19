package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <i>Контроллер ля вызова волонтера.</i>
 * <br>
 * <ul>
 * Для работы контроллера необходимо: <br>
 * <li>Создать группу волонтеров в telegram и подключить бота (админом).</li>
 * <li>Создать канал волонтеров, добавить созданную группу в список обсуждений подключить бота (админом).</li>
 * <li>Получить значения id группы с помощтю бота Get My ID.</li>
 * <li>Опубликовать в канале любое сообщение. Тогда в группе появится информация от @getmyid_bot с id-ками.</li>
 * <li>Удалить @getmyid_bot </li>
 * </ul>
 * <p>
 * Методы:
 *  <ul>
 *   <li>Команда с именем {@link #STOP_CHAT}, отключает пользователя от волонтеров</li>
 *   <li>Команда с именем {@link #NEW_REQUEST}, Вызывается автоматически, когда канал добавляет обсуждение в группу</li>
 *   <li>Команда с паттерном {@link #PATTERN}, Принимает все сообщения, кроме тех, которые начинаются с "/"</li>
 *   <li>Коллбэк {@link Callbacks#CAT_CALL_VOLUNTEER}, подключает пользователя к чату волонтеров</li>
 *  </ul>
 */
@Component
@RequiredArgsConstructor
public class CallVolunteerController implements CommandController {

    @Value("${telegram.volunteer.chat.id}")
    private Long VOLUNTEER_CHAT_ID;

    @Value("${telegram.volunteer.chanel.id}")
    private Long VOLUNTEER_CHANEL_ID;

    public static final String STOP_CHAT = "/stopChat";

    public static final String NEW_REQUEST = "/*Новый вопрос от пользователя*/";

    /**
     * PATTER для всех сообщений, кроме тех, которые начинаются с "/".
     */
    private static final String PATTERN = "[^/].*"; // for all symbols

    private final TelegramBotSender telegramBotSender;

    /**
     * <i>Харнит структуру (id_user, id_thread_message)</i>
     * <br>
     * id_user - id чата, с котроым общаемся
     * <br>
     * id_thread_message - id сообщения, которое является основным в канале
     */
    private final Map<Long, Integer> volunteerChatEnable = new HashMap<>();


    /**
     * Команда на прекращение общения с сервисной службой. Может быть выполнена только на стороне сервиса.
     */
    @Command(name = STOP_CHAT)
    public SendMessage handleStopChat(Message message) {
        // Знаем threadId и можем отключить пользователя.
        long id = -1L;
        for (Map.Entry<Long, Integer> entry : volunteerChatEnable.entrySet()) {
            if (entry.getValue().equals(message.messageThreadId())) {
                id = entry.getKey();
                break;
            }
        }
        if (id != -1) {
            volunteerChatEnable.remove(id);
            return new SendMessage(VOLUNTEER_CHAT_ID, "Вопрос закрыт")
                    .replyToMessageId(message.messageThreadId());
        }

        return new SendMessage(message.chat().id(), "");
    }

    @Command(name = NEW_REQUEST)
    public SendMessage handleMessageWhenUpdateToGroup(Message message) {

        volunteerChatEnable.put(message.entities()[0].user().id(), message.messageId());
        String name = message.entities()[0].user().firstName();
        MessageEntity entity = new MessageEntity(MessageEntity.Type.text_mention, 36, name.length())
                .user(message.entities()[0].user());

        return new SendMessage(message.chat().id(), "Для отправки сообщения пользователю " + name + " отвечайте на любые сообщения бота!!!")
                .replyToMessageId(message.messageId())
                .entities(entity);
    }

    @Command(pattern = PATTERN)
    public SendMessage handleAllMessages(Message message) {

        if (message.replyToMessage() != null) {
            // Если сообщения внутри канала, то либо между собой, либо ответ пользователю
            if (!message.replyToMessage().from().isBot()) {
                // если внутри канала общаемся
                return new SendMessage(message.from().id(), "");
            } else {
                // По threadId ищем кому переслать
                int thread = message.replyToMessage().messageThreadId();
                for (Map.Entry<Long, Integer> entry : volunteerChatEnable.entrySet()) {
                    if (entry.getValue() == thread) {
                        if (message.photo() != null) {
                            telegramBotSender.telegramSendPhoto(new SendPhoto(entry.getKey(), message.photo()[0].fileId())
                                    .caption(message.caption()));
                            return new SendMessage(entry.getKey(), "");
                        }
                        return new SendMessage(entry.getKey(), message.text());
                    }
                }
            }
        } else {
            // Передаем сообщение от пользователя в канал
            // Проверяем, запрашивал ли пользователь запрос в чат
            if (volunteerChatEnable.containsKey(message.chat().id())) {
                if (message.photo() != null) {
                    telegramBotSender.telegramSendPhoto(new SendPhoto(VOLUNTEER_CHAT_ID, message.photo()[0].fileId())
                            .caption(message.caption()).replyToMessageId(volunteerChatEnable.get(message.chat().id())));
                    return new SendMessage(message.chat().id(), "");
                }
                return new SendMessage(VOLUNTEER_CHAT_ID, message.text())
                        .replyToMessageId(volunteerChatEnable.get(message.chat().id()));
            } else {
                return new SendMessage(message.chat().id(), "");
            }
        }
        return new SendMessage(message.chat().id(), "");
    }

    @Callback(name = Callbacks.CAT_CALL_VOLUNTEER)
    public SendMessage handleCallbackMessage(CallbackQuery callbackQuery) {
        // Проверяем, был ли запрос ранее, чтобы не создавать новую тему, пока старый не закрыт
        if (volunteerChatEnable.containsKey(callbackQuery.from().id())) {
            return new SendMessage(callbackQuery.from().id(), "Ваш запрос уже отправлен, ожидайте ответа");
        }
        String requestString = NEW_REQUEST;

        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.text_mention, 0, requestString.length())
                .user(callbackQuery.from());
        SendMessage sendMessage = new SendMessage(VOLUNTEER_CHANEL_ID, requestString)
                .entities(messageEntity);
        telegramBotSender.sendMessage(sendMessage);

        return new SendMessage(callbackQuery.from().id(), "Ваш запрос волонтерам отправлен.");
    }

}
