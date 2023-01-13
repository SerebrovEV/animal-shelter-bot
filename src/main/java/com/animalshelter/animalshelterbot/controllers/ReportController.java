package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.repository.ReportRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ReportController implements CommandController {

    private final TelegramBot telegramBot;

    private static final String addReport = "([\\W]{5})(\\s)([\\W]+)";

    private  final Pattern pattern = Pattern.compile("([\\W]{5})(\\s)([\\W]+)");
    private final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    ReportRepository reportRepository;

    @Command(pattern = addReport)
    public SendMessage addReport(Message message) {
        long idUser = message.from().id();
        LOG.info("Пользователь {} направил отчет для записи в БД", idUser);
//        byte[] photo = new byte[0];
//        PhotoSize[] photoSizes = message.photo();
//        for (PhotoSize photoSize : photoSizes) {
//            GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));
//            try {
//                photo = telegramBot.getFileContent(getFileResponse.file());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Report report = new Report();
//        Matcher matcher = pattern.matcher(message.text());
//        matcher.find();
//        report.setPhoto(photo);
//        report.setText(matcher.group(3));
//        reportRepository.save(report);
        return new SendMessage(idUser, "Test");
    }



}
