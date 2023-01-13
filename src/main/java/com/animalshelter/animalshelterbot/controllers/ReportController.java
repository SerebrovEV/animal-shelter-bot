package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.model.DogReport;
import com.animalshelter.animalshelterbot.repository.ReportRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ReportController implements CommandController {

    private final TelegramBot telegramBot;

    private static final String addReport = "([\\W]{5})(\\s)([\\W]+)";

    private final Pattern pattern = Pattern.compile("Отчет ([\\W]+)");
    private final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    ReportRepository reportRepository;

   // @Command(pattern = addReport)
    public SendMessage addReport(String text, PhotoSize[] photoSizes, long idUser) {

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
        //     }
    //    DogReport report = new DogReport();
     //   Matcher matcher = pattern.matcher(message.text());
      //  matcher.find();
        //report.setPhoto(photo);
     //   report.setText(matcher.group(3));
      //  reportRepository.save(report);
        return new SendMessage(idUser, "Test");
    }


}
