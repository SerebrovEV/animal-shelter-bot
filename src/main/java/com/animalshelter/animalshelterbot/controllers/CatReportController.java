package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.handler.Callback;
import com.animalshelter.animalshelterbot.handler.Command;
import com.animalshelter.animalshelterbot.handler.CommandController;
import com.animalshelter.animalshelterbot.handler.Photo;
import com.animalshelter.animalshelterbot.organisation.Callbacks;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.animalshelter.animalshelterbot.service.CatReportService;
import com.animalshelter.animalshelterbot.service.ValidatorCatReportService;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CatReportController implements CommandController {

    private final TelegramBotSender telegramBotSender;

    private final CatReportService catReportService;

    private final ValidatorCatReportService validatorCatReportService;
    private final Map<Long, Message> reportTemp = new HashMap<>();

    private final String exampleText = "Следуя примеру выше, отправьте фото питомца с подписью к фото, где укажите имя кошки, а после точки рацион питания, " +
            "общее самочувствие и привыкание к новому месту, а также изменение в поведении: отказ от старых привычек, приобретение новых.";
    private final String photoFile = "src/main/resources/images/reportExamplePhoto.jpg";


    @Callback(name = Callbacks.DOG_REPORT)
    public SendMessage addReportDogCallback(CallbackQuery callback) {
        reportTemp.put(callback.from().id(), null);
        telegramBotSender.telegramSendPhoto(examplePhoto(photoFile, callback.from().id()));
        return new SendMessage(callback.from(), exampleText);
    }

    @Callback(name = Callbacks.CAT_REPORT)
    public SendMessage addReportCatCallback(CallbackQuery callback) {
        reportTemp.put(callback.from().id(), null);
        telegramBotSender.telegramSendPhoto(examplePhoto(photoFile, callback.from().id()));
        return new SendMessage(callback.from().id(), exampleText);
    }

    @Callback(name = Callbacks.CAT_ADD_REPORT_NO)
    public SendMessage deleteUserFromTempReport(CallbackQuery callback) {
        reportTemp.remove(callback.from().id());
        return new SendMessage(callback.from().id(), "Ваш отчет небыл отправлен.")
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton("Повторить").callbackData(Callbacks.CAT_REPORT.name()),
                        new InlineKeyboardButton("Назад").callbackData(Callbacks.CAT_MENU.name())
                ));
    }

    @Callback(name = Callbacks.CAT_ADD_REPORT_YES)
    public SendMessage createReport(CallbackQuery callback) {
        // create report for user in repository, then delete from tempReport
        catReportService.addReport(reportTemp.get(callback.from().id()));
        return new SendMessage(callback.from().id(), "Ваш отчет отправлен");
    }

    @Photo
    public SendMessage addReport(Message message) {
        if (reportTemp.containsKey(message.from().id())) {

            if (message.caption() == null) {
                return new SendMessage(message.from().id(), "Добавьте к фото пояснительное сообщение и повторите отправку фото!");
            }
            if (!validatorCatReportService.isCatExist(message)) {
                return new SendMessage(message.from().id(), "Мы не нашли усыновленную Вами кошку с таким именем!");
            }
            //должны проверить для какой кошки (если их несколько) записать отчет
            reportTemp.put(message.from().id(), message);
            return new SendMessage(message.from().id(), "Вы хотите отправить отчет?")
                    .replyMarkup(new InlineKeyboardMarkup(
                            new InlineKeyboardButton("Yes").callbackData(Callbacks.CAT_ADD_REPORT_YES.name()),
                            new InlineKeyboardButton("No").callbackData(Callbacks.CAT_ADD_REPORT_NO.name())
                    ));
        } else {
            return new SendMessage(message.from().id(), "");
        }
    }

    @Command(name = "/getCatReports")
    public SendMessage getReportByToday(Message message) {
        return catReportService.getAllCatReports(message);
    }

    @Command(name = "/getCatDayReport")
    public SendMessage getReportByDay(Message message) {
        return catReportService.getCatReportByDay(message);
    }

    @Command(pattern = "/deleteCatReportId ([\\d]+)")
    public Object deleteCatReport(Message message) {
        return catReportService.deleteCatReport(message);
    }


    private SendPhoto examplePhoto(String photoPathFile, Long id) {
        return new SendPhoto(id, new File(photoPathFile))
                .caption("Маруся. В рацион кошки входит сухое питание Hills Premium. Чувствует себя более уверенно, стала играть игрушками и выходить в другие комнаты.");
    }


}
