package com.animalshelter.animalshelterbot.service;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidatorReportService {

    private final Pattern idPattern = Pattern.compile("(\\d+)");
    private final Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    public Long getIdFromMessage(Message message) {
        Matcher matcher = idPattern.matcher(message.text());
        if (matcher.find()) {
            return Long.valueOf(matcher.group(1));
        }
        return null;
    }

    public Date getDateFromMessage(Message message) {
        Matcher matcher = datePattern.matcher(message.text());
        if (matcher.find()) {
            try {
                return Date.valueOf(matcher.group(0));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
