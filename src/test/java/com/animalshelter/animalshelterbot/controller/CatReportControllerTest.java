package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.service.impl.CatReportService;
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
class CatReportControllerTest {

    @InjectMocks
    private CatReportController catReportController;

    @Mock
    private CatReportService catReportService;


    @Test
    void getHelp() throws URISyntaxException, IOException {
        String HELP_MESSAGE = "*/getCatReports* - выводит все отчеты из БД\n" +
                "*/getCatDayReport 2023-01-12* - выводит отчеты из БД за указанную дату\n" +
                "*/deleteCatsFromReportById 1* - удаляет из БД всех кошек по id = 1(adopted_cat)\n" +
                "*/deleteCatReportId 1* - удаляет из БД запись с id = 1\n" +
                "*/getCatBadUsers 2023-01-12* - выводит список кошек, у которых нет записи в БД на дату\n" +
                "*/sendCatWarning 1* - отправляет предупреждение владельцу по id кошки(adopted_cat)\n";
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        SendMessage actual = catReportController.getHelp(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(HELP_MESSAGE);
    }

    @Test
    void addReportCatCallback() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        when(catReportService.addInquiryReport(callbackQuery)).thenReturn(createSendMessage(callbackQuery.from().id()));

        SendMessage actual = catReportController.addReportCatCallback(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void deleteUserFromTempReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        when(catReportService.closeInquiryReport(callbackQuery)).thenReturn(createSendMessage(callbackQuery.from().id()));

        SendMessage actual = catReportController.deleteUserFromTempReport(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void createReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        when(catReportService.addReport(callbackQuery)).thenReturn(createSendMessage(callbackQuery.from().id()));

        SendMessage actual = catReportController.createReport(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void cacheReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(catReportService.validateReport(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = catReportController.cacheReport(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void getReportByToday() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(catReportService.getAllReports(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = catReportController.getReportByToday(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void getReportByDay() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(catReportService.getReportByDay(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = catReportController.getReportByDay(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void deleteCatsFromReportById() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(catReportService.deletePetsFromReportByPetId(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = catReportController.deleteCatsFromReportById(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void deleteCatReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(catReportService.deleteReport(message)).thenReturn(createSendMessage(message.from().id()));
        SendMessage actual = catReportController.deleteCatReport(message);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void getCatsWithoutReport() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(catReportService.getMissingReports(message)).thenReturn(createListOfSendMessage());
        List<SendMessage> actual = catReportController.getCatsWithoutReport(message);

        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isNotNull();
        });
    }

    @Test
    void sendWarningToUser() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportController.class.getResource("dog_dating_rules.json").toURI()));
        Message message = getMessage(json);
        when(catReportService.sendWarning(message)).thenReturn(createListOfSendMessage());
        List<SendMessage> actual = catReportController.sendWarningToUser(message);

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