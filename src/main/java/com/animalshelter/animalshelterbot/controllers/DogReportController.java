package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.service.DogReportService;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DogReportController implements CommandController {

    private final DogReportService dogReportService;

    private final String PHOTO_PATTERN = "(?U)(\\w+)(\\.)(.+)";

    private final String GET_ALL_DOG_REPORTS = "/getDogReports";

    private final String GET_DOG_DATE_REPORT = "/getDogDayReport (\\d{4}-\\d{2}-\\d{2})";
    private final String DELETE_DOG_FROM_REPORT_BY_DOG_ID = "/deleteDogsFromReportById ([\\d]+)";
    private final String DELETE_DOG_FROM_REPORT_BY_ID = "/deleteDogReportId ([\\d]+)";
    private final String GET_DOGS_FROM_BAD_USERS = "/getDogBadUsers (\\d{4}-\\d{2}-\\d{2})";
    private final String SEND_WARNING_TO_USER = "/sendDogWarning (\\d+)";
    private final String GET_HELP = "/dogReportHelp";
    private final String HELP_MESSAGE = "*/getDogReports* - выводит все отчеты из БД\n" +
            "*/getDogDayReport 2023-01-12* - выводит отчеты из БД за указанную дату\n" +
            "*/deleteDogsFromReportById 1* - удаляет из БД всех собак по id = 1(adopted_dog)\n" +
            "*/deleteDogReportId 1* - удаляет из БД запись с id = 1\n" +
            "*/getDogBadUsers 2023-01-12* - выводит список собак, у которых нет записи в БД на дату\n" +
            "*/sendDogWarning 1* - отправляет предупреждение владельцу по id собаки(adopted_dog)\n";


    @Command(name = GET_HELP)
    public SendMessage getHelp(Message message) {
        return new SendMessage(message.from().id(), HELP_MESSAGE).parseMode(ParseMode.Markdown);
    }

    /**
     * Метод "запоминает" намерения пользователя добавить отчет.
     *
     * @return Пример отчета
     */
    @Callback(name = Callbacks.DOG_REPORT)
    public SendMessage addReportDogCallback(CallbackQuery callback) {
        return dogReportService.addInquiryDogReport(callback);
    }

    /**
     * Метод для удаления пользователя из списков на отправку отчета.
     *
     * @return {@link SendMessage}
     */
    @Callback(name = Callbacks.DOG_ADD_REPORT_NO)
    public SendMessage deleteUserFromTempReport(CallbackQuery callback) {
        return dogReportService.closeInquiryDogReport(callback);
    }

    /**
     * Метод на добавление отчета в БД
     *
     * @return {@link SendMessage}
     */
    @Callback(name = Callbacks.DOG_ADD_REPORT_YES)
    public SendMessage createReport(CallbackQuery callback) {
        return dogReportService.addReport(callback);
    }

    /**
     * Метод принимает сообщения к фото
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = PHOTO_PATTERN)
    public SendMessage cacheReport(Message message) {
        return dogReportService.validateReport(message);
    }

    /**
     * Метод на получение всех записей из БД отчетов по кошкам
     *
     * @return {@link SendMessage}
     */
    @Command(name = GET_ALL_DOG_REPORTS)
    public SendMessage getReportByToday(Message message) {
        return dogReportService.getAllDogReports(message);
    }

    /**
     * Метод на получение всех записей из БД отчетов по кошкам по указанной дате
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = GET_DOG_DATE_REPORT)
    public SendMessage getReportByDay(Message message) {
        return dogReportService.getDogReportByDay(message);
    }

    /**
     * Удаляет всех кошек из БД отчетов по идентификатору adopted_dog_id.
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_DOG_FROM_REPORT_BY_DOG_ID)
    public SendMessage deleteDogsFromReportById(Message message) {
        return dogReportService.deleteDogsFromReportByDogId(message);
    }

    /**
     * Удаляет запись из БД отчетов.
     *
     * @return {@link SendMessage}
     */
    @Command(pattern = DELETE_DOG_FROM_REPORT_BY_ID)
    public SendMessage deleteDogReport(Message message) {
        return dogReportService.deleteDogReport(message);
    }

    /**
     * Выводит список питомце, которые не имеют отчетов в указанную дату.
     *
     * @return Список питомцев, не имеющих отчет
     */
    @Command(pattern = GET_DOGS_FROM_BAD_USERS)
    public List<SendMessage> getDogsWithoutReport(Message message) {
        return dogReportService.getMissingReports(message);
    }

    /**
     * Отправляет предупреждение хозяину.
     *
     * @param message хранит id усыновленного питомца
     * @return Список {@link SendMessage}
     */
    @Command(pattern = SEND_WARNING_TO_USER)
    public List<SendMessage> sendWarningToUser(Message message) {
        return dogReportService.sendWarning(message);
    }
}
