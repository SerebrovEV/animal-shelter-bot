package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallVolunteerControllerTest {

    @InjectMocks
    CallVolunteerController callVolunteerController;

    @Mock
    Message message;

    @Mock
    MessageEntity entity;

    @Mock
    User user;

    @Mock
    Chat chat;

    @Mock
    CallbackQuery callbackQuery;

    @Mock
    TelegramBotSender telegramBotSender;

    Long chat_id = 1L;
    Integer thread_id = 101;
    String name = "Test";


    @Test
    void handleStopChat() {
        addVolunteerChat();
        when(message.messageThreadId()).thenReturn(thread_id);
        SendMessage actual = callVolunteerController.handleStopChat(message);
        assertThat(actual.getParameters().get("text")).isEqualTo("Вопрос закрыт");
        assertThat(actual.getParameters().get("reply_to_message_id")).isEqualTo(thread_id);
    }

    @Test
    void handleStopChatNotFound() {
        addVolunteerChat();
        when(message.messageThreadId()).thenReturn(0);
        SendMessage actual = callVolunteerController.handleStopChat(message);
        assertThat(actual.getParameters().get("text")).isEqualTo("");
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chat_id);
    }

    @Test
    void handleMessageWhenUpdateToGroup() {
        SendMessage actual = addVolunteerChat();// callVolunteerController.handleMessageWhenUpdateToGroup(message);
        String answer = "Для отправки сообщения пользователю " + name + " отвечайте на любые сообщения бота!!!";
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chat_id);
        assertThat(actual.getParameters().get("reply_to_message_id")).isEqualTo(thread_id);
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void handleAllMessagesInternalInGroup() {
        SendMessage expected = new SendMessage(chat_id, "");
        when(message.replyToMessage()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(chat_id);
        when(user.isBot()).thenReturn(false);
        SendMessage actual = callVolunteerController.handleAllMessages(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chat_id);
        assertThat(actual.getParameters().get("text")).isEqualTo(expected.getParameters().get("text"));
    }

    @Test
    void handleAllMessagesAnswerInGroup() {
        when(message.replyToMessage()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(chat_id);
        when(user.isBot()).thenReturn(true);
        when(message.messageThreadId()).thenReturn(thread_id);
        when(message.text()).thenReturn("test");
        addVolunteerChat();

        SendMessage actual = callVolunteerController.handleAllMessages(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chat_id);
        assertThat(actual.getParameters().get("text")).isEqualTo("test");
    }

    @Test
    void handleAllMessagesAnswerInGroupWithoutAddingChat() {
        when(message.replyToMessage()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(chat_id);
        when(user.isBot()).thenReturn(true);
        when(message.messageThreadId()).thenReturn(thread_id + 1);
       // when(message.text()).thenReturn("test");
        addVolunteerChat();

        SendMessage actual = callVolunteerController.handleAllMessages(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chat_id);
        assertThat(actual.getParameters().get("text")).isEqualTo("");
    }

    @Test
    void handleAllMessagesAnswerInGroupWithPhoto() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("send_photo_volunteer.json").toURI()));
        Message message = getMessage(json);
        doNothing().when(telegramBotSender).telegramSendPhoto(any(SendPhoto.class));

        addVolunteerChat();

        SendMessage actual = callVolunteerController.handleAllMessages(message);
        verify(telegramBotSender).telegramSendPhoto(any(SendPhoto.class));
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chat_id);
        assertThat(actual.getParameters().get("text")).isEqualTo("");
    }

    @Test
    void handleAllMessagesAnswerInGroupWithoutChat() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("send_photo_volunteer.json").toURI()));
        Message message = getMessage(json);

        SendMessage actual = callVolunteerController.handleAllMessages(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chat_id);
        assertThat(actual.getParameters().get("text")).isEqualTo("");
    }

    @Test
    void handleAllMessagesAnswerFromUserWithoutChat() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("send_text_volunteer_from_user.json").toURI()));
        Message message = getMessage(json);

        SendMessage actual = callVolunteerController.handleAllMessages(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chat_id);
        assertThat(actual.getParameters().get("text")).isEqualTo("");
    }

    @Test
    void handleAllMessagesFromUserToVolunteerWithPhoto() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("send_photo_volunteer_from_user.json").toURI()));
        Message message = getMessage(json);
        doNothing().when(telegramBotSender).telegramSendPhoto(any(SendPhoto.class));
        addVolunteerChat();

        SendMessage actual = callVolunteerController.handleAllMessages(message);
        verify(telegramBotSender).telegramSendPhoto(any(SendPhoto.class));
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chat_id);
        assertThat(actual.getParameters().get("text")).isEqualTo("");
    }

    @Test
    void handleAllMessagesFromUserToVolunteerWithText() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("send_text_volunteer_from_user.json").toURI()));
        Message message = getMessage(json);
        ReflectionTestUtils.setField(callVolunteerController, "VOLUNTEER_CHAT_ID", 2L);
        addVolunteerChat();

        SendMessage actual = callVolunteerController.handleAllMessages(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(2L);
        assertThat(actual.getParameters().get("text")).isEqualTo("test");
    }

    @Test
    void handleCallbackMessageNew() {
        SendMessage expected = new SendMessage(1L, "Ваш запрос волонтерам отправлен.");
        when(callbackQuery.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
        SendMessage actual = callVolunteerController.handleCallbackMessage(callbackQuery);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actual.getParameters().get("text")).isEqualTo("Ваш запрос волонтерам отправлен.");

        addVolunteerChat();
        actual = callVolunteerController.handleCallbackMessage(callbackQuery);
        assertThat(actual.getParameters().get("text")).isEqualTo("Ваш запрос уже отправлен, ожидайте ответа");
    }

    private SendMessage addVolunteerChat() {

        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.text_mention, 0, 5)
                .user(user);
        MessageEntity[] entities = new MessageEntity[1];
        entities[0] = messageEntity;
        when(message.entities()).thenReturn(entities);
        when(user.id()).thenReturn(chat_id);
        when(user.firstName()).thenReturn(name);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chat_id);
        //when(entities[0].user()).thenReturn(user);
        when(message.messageId()).thenReturn(thread_id);
        return callVolunteerController.handleMessageWhenUpdateToGroup(message);
    }

    private Message getMessage(String json) {
        return BotUtils.fromJson(json, Message.class);
    }
}