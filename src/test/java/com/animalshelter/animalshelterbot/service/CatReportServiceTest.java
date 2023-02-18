package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedCat;
import com.animalshelter.animalshelterbot.model.CatReport;
import com.animalshelter.animalshelterbot.model.CatUser;
import com.animalshelter.animalshelterbot.repository.AdoptedCatRepository;
import com.animalshelter.animalshelterbot.repository.CatReportRepository;
import com.animalshelter.animalshelterbot.repository.CatUserRepository;
import com.animalshelter.animalshelterbot.sender.TelegramBotSender;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatReportServiceTest {

    @InjectMocks
    CatReportService catReportService;

    @Mock
    AdoptedCatRepository adoptedCatRepository;

    @Mock
    CatUserRepository catUserRepository;

    @Mock
    CatReportRepository catReportRepository;

    @Mock
    TelegramBotSender telegramBotSender;

    @Mock
    ValidatorReportService validatorCatReportService;

    @Test
    void addReport() throws URISyntaxException, IOException {
        String answer = "Ваш отчет отправлен.";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_callback.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        AdoptedCat adoptedCat = createAdoptedCat();
        CatReport catReport = createCatReport(1L, Date.valueOf("2023-01-17"), "photo", "text", adoptedCat);
        when(catReportRepository.findCatReportByDateAndAdoptedCat_Id(any(Date.class), any(Long.class)))
                .thenReturn(Optional.of(catReport));
        when(catReportRepository.save(any(CatReport.class))).thenReturn(catReport);

        addReportToInquiry();
        validateReportPositive();
        SendMessage actual = catReportService.addReport(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void getAllCatReports() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_photo_message.json").toURI()));
        Message message = getMessage(json);
        List<CatReport> catReports = getCatReports();
        when(catReportRepository.findAll()).thenReturn(catReports);

        SendMessage actual = catReportService.getAllCatReports(message);
        verify(telegramBotSender, times(catReports.size())).telegramSendPhoto(any(SendPhoto.class));
    }

    @Test
    void getCatReportByDay() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        List<CatReport> catReports = getCatReports();
        when(catReportRepository.findCatReportByDate(any(Date.class))).thenReturn(catReports);
        when(validatorCatReportService.getDateFromMessage(any(Message.class))).thenReturn(Date.valueOf("2021-01-12"));
        SendMessage actual = catReportService.getCatReportByDay(message);
        verify(telegramBotSender, times(catReports.size())).telegramSendPhoto(any(SendPhoto.class));
    }

    @Test
    void deleteCatReport() throws URISyntaxException, IOException {
        String answer = "Отчет с id = 1 был удален.";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        when(validatorCatReportService.getIdFromMessage(any(Message.class))).thenReturn(1L);
        doNothing().when(catReportRepository).deleteById(any(Long.class));
        SendMessage actual = catReportService.deleteCatReport(message);
        verify(catReportRepository).deleteById(1L);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void deleteCatReportWrongId() throws URISyntaxException, IOException {
        String answer = "Неверный запрос";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        when(validatorCatReportService.getIdFromMessage(any(Message.class))).thenReturn(null);
//        doNothing().when(catReportRepository).deleteById(any(Long.class));
        SendMessage actual = catReportService.deleteCatReport(message);
//        verify(catReportRepository).deleteById(1L);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void deleteCatsFromReportByCatId() throws URISyntaxException, IOException {
        String answer = "Все отчеты с кошкой с id = 1 были удалены.";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        List<CatReport> catReports = getCatReports();
        when(validatorCatReportService.getIdFromMessage(any(Message.class))).thenReturn(1L);
        when(catReportRepository.findCatReportsByAdoptedCat_Id(any(Long.class)))
                .thenReturn(catReports);
        doNothing().when(catReportRepository).deleteAll(anyCollection());

        SendMessage actual = catReportService.deleteCatsFromReportByCatId(message);
        verify(catReportRepository).deleteAll(anyCollection());
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void deleteCatsFromReportByCatWrongId() throws URISyntaxException, IOException {
        String answer = "Неверный запрос";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        when(validatorCatReportService.getIdFromMessage(any(Message.class))).thenReturn(null);

        SendMessage actual = catReportService.deleteCatsFromReportByCatId(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void getMissingReports() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        Collection<AdoptedCat> adoptedCats = List.of(
                new AdoptedCat("cat1"),
                new AdoptedCat("cat2"),
                new AdoptedCat("cat3")
        );
        when(validatorCatReportService.getDateFromMessage(any(Message.class))).thenReturn(Date.valueOf("2021-01-12"));
        when(adoptedCatRepository.findMissingReportAdoptedCat(any(Date.class))).thenReturn(adoptedCats);

        List<SendMessage> actual = catReportService.getMissingReports(message);
        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isNotNull();
        });
    }
    @Test
    void getMissingReportsBad() throws URISyntaxException, IOException {
        String answer = "Неверный запрос даты! Отправьте в формате yyyy-mm-dd";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);

        when(validatorCatReportService.getDateFromMessage(any(Message.class))).thenReturn(null);

        List<SendMessage> actual = catReportService.getMissingReports(message);
        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isEqualTo(answer);
        });
    }

    @Test
    void sendWarning() throws URISyntaxException, IOException {

        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        AdoptedCat adoptedCat = createAdoptedCat();
        when(validatorCatReportService.getIdFromMessage(any(Message.class))).thenReturn(1L);
        when(adoptedCatRepository.findById(any(Long.class))).thenReturn(Optional.of(adoptedCat));

        List<SendMessage> actual = catReportService.sendWarning(message);
        assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Предупреждение отправлено");
        assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(adoptedCat.getCatUser().getChatId());
        assertThat(actual.get(1).getParameters().get("text")).isNotNull();
    }


    @Test
    void sendWarningBadId() throws URISyntaxException, IOException {
        String answer = "Неверный запрос";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        when(validatorCatReportService.getIdFromMessage(any(Message.class))).thenReturn(null);

        List<SendMessage> actual = catReportService.sendWarning(message);
        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isEqualTo(answer);
        });

    }

    @Test
    void sendWarningCantFindCat() throws URISyntaxException, IOException {
        String answer = "Кошки с таким id не найдено";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        when(validatorCatReportService.getIdFromMessage(any(Message.class))).thenReturn(1L);
        when(adoptedCatRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        List<SendMessage> actual = catReportService.sendWarning(message);
        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isEqualTo(answer);
        });
    }

    @Test
    void validateReportPositive() throws URISyntaxException, IOException {
        String answer = "Вы хотите отправить отчет?";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_photo_message.json").toURI()));
        Message message = getMessage(json);
        AdoptedCat adoptedCat = createAdoptedCat();
        when(adoptedCatRepository.findAdoptedCatByCatName(any(String.class))).thenReturn(Optional.of(adoptedCat));
        when(catUserRepository.findCatUserByChatId(any(Long.class))).thenReturn(Optional.of(adoptedCat.getCatUser()));
        addReportToInquiry();
        SendMessage actual = catReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void validateReportWrongPattern() throws URISyntaxException, IOException {
        String answer = "Мы не нашли усыновленную Вами кошку с таким именем!";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_photo_message.json").toURI()));
        Message message = getMessage(json);
        addReportToInquiry();
        SendMessage actual = catReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void validateReportWrongName() throws URISyntaxException, IOException {
        String answer = "Мы не нашли усыновленную Вами кошку с таким именем!";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_photo_message_no_name.json").toURI()));
        Message message = getMessage(json);
        when(adoptedCatRepository.findAdoptedCatByCatName(any(String.class))).thenReturn(Optional.ofNullable(null));
        addReportToInquiry();
        SendMessage actual = catReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void validateReportFindNameNotBelongsToUser() throws URISyntaxException, IOException {
        String answer = "Мы не нашли усыновленную Вами кошку с таким именем!";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_photo_message_no_name.json").toURI()));
        Message message = getMessage(json);
        AdoptedCat adoptedCat = createAdoptedCat();
        adoptedCat.setCatName("testName");
        when(adoptedCatRepository.findAdoptedCatByCatName(any(String.class))).thenReturn(Optional.of(adoptedCat));
        addReportToInquiry();
        SendMessage actual = catReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void validateReportNoCaption() throws URISyntaxException, IOException {
        String answer = "Добавьте к фото пояснительное сообщение и повторите отправку фото!";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_photo_message_no_caption.json").toURI()));
        Message message = getMessage(json);
        addReportToInquiry();
        SendMessage actual = catReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void validateReportWithoutMenu() throws URISyntaxException, IOException {
        String answer = "";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_photo_message.json").toURI()));
        Message message = getMessage(json);
        AdoptedCat adoptedCat = createAdoptedCat();
        SendMessage actual = catReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void validateReportWithMenuAndNoPhoto() throws URISyntaxException, IOException {
        String answer = "";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        AdoptedCat adoptedCat = createAdoptedCat();
        addReportToInquiry();
        SendMessage actual = catReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void addInquiryCatReport() throws URISyntaxException, IOException {
        SendMessage actual = addReportToInquiry();
        Long expected = 123L;
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(expected);
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void closeInquiryCatReport() throws URISyntaxException, IOException {
        String answer = "Ваш отчет не был отправлен.";
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_callback.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        SendMessage actual = catReportService.closeInquiryCatReport(callbackQuery);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    private CallbackQuery getCallback(String json) {
        return BotUtils.fromJson(json, CallbackQuery.class);
    }

    private Message getMessage(String json) {
        return BotUtils.fromJson(json, Message.class);
    }

    private AdoptedCat createAdoptedCat() {
        CatUser catUser = new CatUser("Owner", 12345, 1);
        AdoptedCat adoptedCat = new AdoptedCat("Питомец", null, 30);
        adoptedCat.setCatUser(catUser);
        adoptedCat.setId(1L);
        return adoptedCat;
    }

    private SendMessage addReportToInquiry() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CatReportService.class.getResource("animal_report_callback.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);

        doNothing().when(telegramBotSender).telegramSendPhoto(any(SendPhoto.class));
        return catReportService.addInquiryCatReport(callbackQuery);
    }


    private CatReport createCatReport(Long id, Date date, String photo, String text, AdoptedCat adoptedCat) {
        CatReport catReport = new CatReport();
        catReport.setId(id);
        catReport.setDate(date);
        catReport.setPhoto(photo);
        catReport.setText(text);
        catReport.setAdoptedCat(adoptedCat);
        return catReport;
    }

    private List<CatReport> getCatReports() {
        AdoptedCat adoptedCat = createAdoptedCat();
        return List.of(
                createCatReport(1L, Date.valueOf("2023-01-12"), "photo1", "test1", adoptedCat),
                createCatReport(2L, Date.valueOf("2023-01-12"), "photo2", "test2", adoptedCat),
                createCatReport(3L, Date.valueOf("2023-01-12"), "photo3", "test3", adoptedCat));
    }

}