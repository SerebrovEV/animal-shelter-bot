package com.animalshelter.animalshelterbot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;

public interface ReportService {

    SendMessage addReport(CallbackQuery callback);
    SendMessage getAllReports(Message message);
    SendMessage getReportByDay(Message message);
    SendMessage deleteReport(Message message);
    SendMessage deletePetsFromReportByPetId(Message message);
    List<SendMessage> getMissingReports(Message message);
    List<SendMessage> sendWarning(Message message);
    SendMessage validateReport(Message message);
    SendMessage addInquiryReport(CallbackQuery callback);
    SendMessage closeInquiryReport(CallbackQuery callback);
}
