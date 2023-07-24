package com.animalshelter.animalshelterbot.controller;

import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.service.ReportService;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DogReportController implements CommandController {

    private final ReportService reportService;

    private final String photoPattern = "(?U)(\\w+)(\\.)(.+)";

    private final String getAllDogReports = "/getDogReports";

    private final String getDogDateReport = "/getDogDayReport (\\d{4}-\\d{2}-\\d{2})";
    private final String deleteDogFromReportByDogId = "/deleteDogsFromReportById ([\\d]+)";
    private final String deleteDogFromReportById = "/deleteDogReportId ([\\d]+)";
    private final String getDogsFromBadUsers = "/getDogBadUsers (\\d{4}-\\d{2}-\\d{2})";
    private final String sendWarningToUser = "/sendDogWarning (\\d+)";
    private final String getHelp = "/dogReportHelp";
    private final String helpMessage = "*/getDogReports* - выводит все отчеты из БД\n" +
            "*/getDogDayReport 2023-01-12* - выводит отчеты из БД за указанную дату\n" +
            "*/deleteDogsFromReportById 1* - удаляет из БД всех собак по id = 1(adopted_dog)\n" +
            "*/deleteDogReportId 1* - удаляет из БД запись с id = 1\n" +
            "*/getDogBadUsers 2023-01-12* - выводит список собак, у которых нет записи в БД на дату\n" +
            "*/sendDogWarning 1* - отправляет предупреждение владельцу по id собаки(adopted_dog)\n";

    public DogReportController(@Qualifier("dogReportService") ReportService reportService) {
        this.reportService = reportService;
    }


    @Command(name = getHelp)
    public SendMessage getHelp(Message message) {
        return new SendMessage(message.from().id(), helpMessage).parseMode(ParseMode.Markdown);
    }

    /**
     * Метод "запоминает" намерения пользователя добавить отчет.
     *
     * @return Пример отчета
     */
    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.DOG_REPORT)
    public SendMessage addReportDogCallback(CallbackQuery callback) {
        return reportService.addInquiryReport(callback);
    }

    /**
     * Метод для удаления пользователя из списков на отправку отчета.
     *
     * @return {@link SendMessage}
     */
    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.DOG_ADD_REPORT_NO)
    public SendMessage deleteUserFromTempReport(CallbackQuery callback) {
        return reportService.closeInquiryReport(callback);
    }

    /**
     * Метод на добавление отчета в БД
     *
     * @return {@link SendMessage}
     */
    @com.animalshelter.animalshelterbot.handler.Callback(name = Callbacks.DOG_ADD_REPORT_YES)
    public SendMessage createReport(CallbackQuery callback) {
        return reportService.addReport(callback);
    }

    /**
     * Метод принимает сообщения к фото
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = photoPattern)
    public SendMessage cacheReport(Message message) {
        return reportService.validateReport(message);
    }

    /**
     * Метод на получение всех записей из БД отчетов по кошкам
     *
     * @return {@link SendMessage}
     */
    @Command(name = getAllDogReports)
    public SendMessage getReportByToday(Message message) {
        return reportService.getAllReports(message);
    }

    /**
     * Метод на получение всех записей из БД отчетов по кошкам по указанной дате
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = getDogDateReport)
    public SendMessage getReportByDay(Message message) {
        return reportService.getReportByDay(message);
    }

    /**
     * Удаляет всех кошек из БД отчетов по идентификатору adopted_dog_id.
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = deleteDogFromReportByDogId)
    public SendMessage deleteDogsFromReportById(Message message) {
        return reportService.deletePetsFromReportByPetId(message);
    }

    /**
     * Удаляет запись из БД отчетов.
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = deleteDogFromReportById)
    public SendMessage deleteDogReport(Message message) {
        return reportService.deleteReport(message);
    }

    /**
     * Выводит список питомце, которые не имеют отчетов в указанную дату.
     *
     * @return Список питомцев, не имеющих отчет
     */
    @Command(pattern = getDogsFromBadUsers)
    public List<SendMessage> getDogsWithoutReport(Message message) {
        return reportService.getMissingReports(message);
    }

    /**
     * Отправляет предупреждение хозяину.
     *
     * @param message хранит id усыновленного питомца
     * @return Список {@link SendMessage}
     */
    @Command(pattern = sendWarningToUser)
    public List<SendMessage> sendWarningToUser(Message message) {
        return reportService.sendWarning(message);
    }
}
