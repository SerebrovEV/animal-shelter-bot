package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <i>Контроллер ля вызова волонтера.</i>
 */
@Component
@RequiredArgsConstructor
public class CallVolunteerController implements CommandController {

    @Value("${telegram.volunteer.chat.id}")
    private Long VOLUNTEER_CHAT_ID;
    public static final String CALL_VOLUNTEER_COMMAND = "/callVolunteer";
    public static final String CALL_VOLUNTEER_CALLBACK = "/callVolunteer";

    public static final String START_CHAT = "/startChat";

    public static final String STOP_CHAT = "/stopChat";

    /**
     * PATTER for all symbols
     */
    private static final String PATTERN = ".+"; // for all symbols

    private final TelegramBotSender telegramBotSender;

    // Для хранения чатов, которые могут персылать сообщения волонтерам
    private Map<Long, Boolean> volunteerChatEnable = new HashMap<>();

    public boolean isChatWithVolunteerEnable(Long id) {
        return volunteerChatEnable.getOrDefault(id, false);
    }

    public void removeFromChatVolunteer(Long id) {
        volunteerChatEnable.remove(id);
    }

    @Command(name = CALL_VOLUNTEER_COMMAND)
    public SendMessage handleCallVolunteer(Message message) {
        volunteerChatEnable.put(message.from().id(), true);

        // send request to volunteer
        telegramBotSender.sendMessage(VOLUNTEER_CHAT_ID, "У пользователя есть вопрос. Нажмите ответить на сообщении.");
        SendMessage msg = new SendMessage(VOLUNTEER_CHAT_ID, "Вопрос есть")
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton("Ответить").callbackData("answer")));
        telegramBotSender.sendMsg(msg);
        ForwardMessage forwardMessage = new ForwardMessage(VOLUNTEER_CHAT_ID, message.from().id(), message.messageId());
        telegramBotSender.sendForwardMessage(forwardMessage);

        return new SendMessage(message.from().id(), "Запрос к волонтеру уже отправлен. Ждем, когда он подключится к чату");
    }

    @Command(name = START_CHAT)
    public SendMessage handleStartChat(Message message) {
        volunteerChatEnable.put(message.chat().id(), true);
        return new SendMessage(message.chat().id(), "Вы начали чат с волонтером. Отправьте Ваш вопрос. Чтобы закончить чат, отправьте /stopChat");
    }

    @Command(name = STOP_CHAT)
    public SendMessage handleStopChat(Message message) {
        volunteerChatEnable.remove(message.chat().id());
        return new SendMessage(message.chat().id(), "Ваши сообщения больше не отправляются волонтерам");
    }

//    @Command(pattern = PATTERN)
//    public SendMessage handleSafetyMessage(Message message) {
//        // Сообщение приходит от user и он в списке на чат
//        if (volunteerChatEnable.getOrDefault(message.chat().id(), false)) {
//            ForwardMessage forwardMessage = new ForwardMessage(VOLUNTEER_CHAT_ID, message.from().id(), message.messageId());
//            telegramBotSender.sendForwardMessage(forwardMessage);
//        }
//        //Если сообщение от чата волонтеров
//        if (message.chat().id().equals(VOLUNTEER_CHAT_ID)) {
//            if (message.text().equals("/stop")) {
//                volunteerChatEnable.remove(message.replyToMessage().forwardFrom().id());
//                return new SendMessage(message.replyToMessage().forwardFrom().id(), "Ваш сенанс связи с волонтерами закончился.");
//            } else {
//                return new SendMessage(message.replyToMessage().forwardFrom().id(), message.text());
//            }
//        }
//        return new SendMessage(message.from().id(), "");
//    }

    @Callback(name = CALL_VOLUNTEER_CALLBACK)
    public SendMessage handleCallbackMessage(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.from().id(), "");
    }
}
