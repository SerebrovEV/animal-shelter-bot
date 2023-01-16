package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
import com.animalshelter.animalshelterbot.repository.CatUserRepository;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidatorCatReportService {

    private final AdoptedCatRepository adoptedCatRepository;
    private final CatUserRepository catUserRepository;


    private final Pattern DIGITAL_PATTERN = Pattern.compile("(\\d+)");


    /**
     * Проверяет, есть ли кошка с именем из сообщения у человека, который является усыновителем.
     * @param message
     * @return
     */
    public boolean isCatExist(Message message) {
        String name;
        try {
            name = message.caption().split("\\.")[0];
        } catch (RuntimeException e) {
            return false;
        }
        AdoptedCat adoptedCat = adoptedCatRepository.findAdoptedCatByCatName(name).orElse(null);
        if (adoptedCat == null) {
            return false;
        }
        if (adoptedCat.getCatUser().equals(catUserRepository.findCatUserByChatId(message.from().id()))) {
            return true;
        }
        return false;
    }

    public Long getCatReportId(Message message) {
        Matcher matcher = DIGITAL_PATTERN.matcher(message.text());
        if (matcher.find()) {
            return Long.valueOf(matcher.group(1));
        }
        return null;
    }
}
