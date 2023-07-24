package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.service.impl.CatReportService;
import com.animalshelter.animalshelterbot.service.impl.DogReportService;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <i> Сервис для обработки входящих сообщений с контроллеров {@link DogReportService} и {@link CatReportService}<i>
 */
@Service
@RequiredArgsConstructor
public class ValidatorReportService {

    private final Pattern ID_PATTERN = Pattern.compile("(\\d+)");
    private final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    public Long getIdFromMessage(Message message) {
        Matcher matcher = ID_PATTERN.matcher(message.text());
        if (matcher.find()) {
            return Long.valueOf(matcher.group(1));
        }
        return null;
    }

    public Date getDateFromMessage(Message message) {
        Matcher matcher = DATE_PATTERN.matcher(message.text());
        if (matcher.find()) {
            try {
                return Date.valueOf(matcher.group(0));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }
}
