package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.model.CatReport;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
import com.animalshelter.animalshelterbot.repository.CatReportRepository;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CatReportService {

    private final CatUserService catUserService;
    private final AdoptedCatRepository adoptedCatRepository;

    private final CatReportRepository catReportRepository;

    private final ValidatorCatReportService validatorCatReportService;
    private final TelegramBotSender telegramBotSender;

    public void addReport(Message message) {

        String name = message.caption().split("\\.")[0];
        AdoptedCat adoptedCat = adoptedCatRepository.findAdoptedCatByCatName(name).orElse(null);
        CatReport catReport = new CatReport();
        catReport.setDate(Date.valueOf(LocalDate.now()));
        catReport.setText(message.caption());
        catReport.setPhoto(Arrays.stream(message.photo()).findFirst().get().fileId());
        catReport.setAdoptedCat(adoptedCat);
        // Проверяем, был ли за сегодня отчет
        CatReport oldReport = catReportRepository
                .findCatReportByDateAndAdoptedCat_Id(Date.valueOf(LocalDate.now()), adoptedCat.getId())
                .orElse(null);
        if (oldReport != null) {
            catReport.setId(oldReport.getId());
        }
        catReportRepository.save(catReport);
    }

    /**
     * Выводит спиок всех отчетов
     * @param message
     * @return
     */
    public SendMessage getAllCatReports(Message message) {
        Collection<CatReport> catReports = catReportRepository.findAll();
        sendReports(catReports, message.from().id());
        return new SendMessage(message.from().id(), "");
    }

    /**
     * Выводит список отчетов за сегодняшний день
     * @param message
     * @return
     */
    public SendMessage getCatReportByDay(Message message) {
        Collection<CatReport> catReports = catReportRepository.findCatReportByDate(Date.valueOf(LocalDate.now()));
        sendReports(catReports, message.from().id());
        return new SendMessage(message.from().id(), "");
    }

    public SendMessage deleteCatReport(Message message) {
        Long id = validatorCatReportService.getCatReportId(message);
        if (id == null) {
            return new SendMessage(message.from().id(), "Неверный запрос");
        }
        catReportRepository.deleteById(id);
        return new SendMessage(message.from().id(), "Отчет с id = " + id + " был удален.");
    }

    private void sendReports(Collection<CatReport> reports, Long id) {
        for (CatReport catReport : reports
        ) {
            SendPhoto sendPhoto = new SendPhoto(id, catReport.getPhoto())
                    .caption("id=" + catReport.getId() + ". Date=" + catReport.getDate() + ". " + catReport.getText());
            telegramBotSender.telegramSendPhoto(sendPhoto);
        }
    }
}
