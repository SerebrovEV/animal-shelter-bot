package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.CatUser;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatReportService {

    private final CatUserService catUserService;

    public void addReport(Message message) {
        // ищем по chat_id CatUser
        CatUser catUser = catUserService.getCatUserByChatId(message.from().id());


    }
}
