package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.service.impl.CatReportService;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ValidatorReportServiceTest {

    private final ValidatorReportService validatorCatReportService = new ValidatorReportService();


    @Test
    void getIdFromMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message_id.json").toURI()));
        Message message = getMessage(json);
        Long expected = 1L;
        assertThat(expected).isEqualTo(validatorCatReportService.getIdFromMessage(message));
    }

    @Test
    void getIdFromMessageBadPattern() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message_bad_id.json").toURI()));
        Message message = getMessage(json);
        Long expected = null;
        assertThat(validatorCatReportService.getIdFromMessage(message)).isNull();
    }

    @Test
    void getDateFromMessage() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        Date expected = Date.valueOf("2023-01-12");
        assertThat(expected).isEqualTo(validatorCatReportService.getDateFromMessage(message));
    }

    @Test
    void getDateFromMessageBadPattern() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message_id.json").toURI()));
        Message message = getMessage(json);
        assertThat(validatorCatReportService.getDateFromMessage(message)).isNull();
    }

    @Test
    void getDateFromMessageBadDate() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message_bad_date.json").toURI()));
        Message message = getMessage(json);
        assertThat(validatorCatReportService.getDateFromMessage(message)).isNull();
    }

    private Message getMessage(String json) {
        return BotUtils.fromJson(json, Message.class);
    }
}