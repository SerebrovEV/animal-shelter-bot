package com.animalshelter.animalshelterbot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TelegramBotSender {

    private final TelegramBot telegramBot;

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text).parseMode(ParseMode.HTML);
        SendResponse response = telegramBot.execute(message);
    }

    public void sendForwardMessage(ForwardMessage forwardMessage) {
        SendResponse response = telegramBot.execute(forwardMessage);
    }

    public void setCommands(BotCommand... commands) {
        SetMyCommands commands1 = new SetMyCommands(commands);
            telegramBot.execute(commands1);
    }

    public void sendMsg(SendMessage message) {
        SendResponse response = telegramBot.execute(message);
    }


}
