package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.service.impl.DogReportService;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DogReportControllerTest {

    @InjectMocks
    private DogReportController dogReportController;

    @Mock
    private DogReportService dogReportService;


    @Test
    void getHelp() throws URISyntaxException, IOException {
        String HELP_MESSAGE = "*/getDogReports* - выводит все отчеты из БД\n" +
                "*/getDogDayReport 2023-01-12* - выводит отчеты из БД за указанную дату\n" +
                "*/deleteDogsFromReportById 1* - удаляет из БД всех собак по id = 1(adopted_dog)\n" +
                "*/deleteDogReportId 1* - удаляет из БД запись с id = 1\n" +
                "*/getDogBadUsers 2023-01-12* - выводит список собак, у которых нет записи в БД на дату\n" +
                "*/sendDogWarning 1* - отправляет предупреждение владельцу по id собаки(adopted_dog)\n";
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        SendMessage actual = dogReportController.getHelp(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(HELP_MESSAGE);
    }

    @Test
    void addReportDogCallback() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        when(dogReportService.addInquiryReport(callbackQuery)).thenReturn(createSendMessage(callbackQuery.from().id()));

        SendMessage actual = dogReportController.addReportDogCallback(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void deleteUserFromTempReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        when(dogReportService.closeInquiryReport(callbackQuery)).thenReturn(createSendMessage(callbackQuery.from().id()));

        SendMessage actual = dogReportController.deleteUserFromTempReport(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void createReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        when(dogReportService.addReport(callbackQuery)).thenReturn(createSendMessage(callbackQuery.from().id()));

        SendMessage actual = dogReportController.createReport(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void cacheReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(dogReportService.validateReport(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = dogReportController.cacheReport(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void getReportByToday() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(dogReportService.getAllReports(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = dogReportController.getReportByToday(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void getReportByDay() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(dogReportService.getReportByDay(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = dogReportController.getReportByDay(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void deleteDogsFromReportById() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(dogReportService.deletePetsFromReportByPetId(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = dogReportController.deleteDogsFromReportById(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void deleteDogReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(dogReportService.deleteReport(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = dogReportController.deleteDogReport(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void getDogsWithoutReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(dogReportService.getMissingReports(message)).thenReturn(createListOfSendMessage());
        List<SendMessage> actual = dogReportController.getDogsWithoutReport(message);

        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isNotNull();
        });
    }

    @Test
    void sendWarningToUser() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(dogReportService.sendWarning(message)).thenReturn(createListOfSendMessage());
        List<SendMessage> actual = dogReportController.sendWarningToUser(message);

        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isNotNull();
        });
    }

    private CallbackQuery getCallback(String json) {
        return BotUtils.fromJson(json, CallbackQuery.class);
    }

    private Message getMessage(String json) {
        return BotUtils.fromJson(json, Message.class);
    }

    private SendMessage createSendMessage(Long chatId) {
        return new SendMessage(chatId, "Test");
    }

    private List<SendMessage> createListOfSendMessage() {
        return List.of(
                new SendMessage(123L, "Test1"),
                new SendMessage(123L, "Test2"),
                new SendMessage(123L, "Test3")
        );
    }
}