package com.animalshelter.animalshelterbot.service;

import com.animalshelter.animalshelterbot.model.AdoptedDog;
import com.animalshelter.animalshelterbot.model.DogReport;
import com.animalshelter.animalshelterbot.model.DogUser;
import com.animalshelter.animalshelterbot.repository.AdoptedDogRepository;
import com.animalshelter.animalshelterbot.repository.DogReportRepository;
import com.animalshelter.animalshelterbot.repository.DogUserRepository;
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
class DogReportServiceTest {

    @InjectMocks
    DogReportService dogReportService;

    @Mock
    AdoptedDogRepository adoptedDogRepository;

    @Mock
    DogUserRepository dogUserRepository;

    @Mock
    DogReportRepository dogReportRepository;

    @Mock
    TelegramBotSender telegramBotSender;

    @Mock
    ValidatorReportService validatorDogReportService;

    @Test
    void addReport() throws URISyntaxException, IOException {
        String answer = "Ваш отчет отправлен.";
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_callback.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);
        AdoptedDog adoptedDog = createAdoptedDog();
        DogReport dogReport = createDogReport(1L, Date.valueOf("2023-01-17"), "photo", "text", adoptedDog);
        when(dogReportRepository.findDogReportByDateAndAdoptedDog_Id(any(Date.class), any(Long.class)))
                .thenReturn(Optional.of(dogReport));
        when(dogReportRepository.save(any(DogReport.class))).thenReturn(dogReport);

        addReportToInquiry();
        validateReportPositive();
        SendMessage actual = dogReportService.addReport(callbackQuery);

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(callbackQuery.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void getAllDogReports() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_photo_message.json").toURI()));
        Message message = getMessage(json);
        List<DogReport> dogReports = getDogReports();
        when(dogReportRepository.findAll()).thenReturn(dogReports);

        SendMessage actual = dogReportService.getAllDogReports(message);
        verify(telegramBotSender, times(dogReports.size())).telegramSendPhoto(any(SendPhoto.class));
    }

    @Test
    void getDogReportByDay() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        List<DogReport> dogReports = getDogReports();
        when(dogReportRepository.findDogReportByDate(any(Date.class))).thenReturn(dogReports);
        when(validatorDogReportService.getDateFromMessage(any(Message.class))).thenReturn(Date.valueOf("2021-01-12"));
        SendMessage actual = dogReportService.getDogReportByDay(message);
        verify(telegramBotSender, times(dogReports.size())).telegramSendPhoto(any(SendPhoto.class));
    }

    @Test
    void deleteDogReport() throws URISyntaxException, IOException {
        String answer = "Отчет с id = 1 был удален.";
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        when(validatorDogReportService.getIdFromMessage(any(Message.class))).thenReturn(1L);
        doNothing().when(dogReportRepository).deleteById(any(Long.class));
        SendMessage actual = dogReportService.deleteDogReport(message);
        verify(dogReportRepository).deleteById(1L);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void deleteDogsFromReportByDogId() throws URISyntaxException, IOException {
        String answer = "Все отчеты с собакой с id = 1 были удалены.";
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        List<DogReport> dogReports = getDogReports();
        when(validatorDogReportService.getIdFromMessage(any(Message.class))).thenReturn(1L);
        when(dogReportRepository.findDogReportsByAdoptedDog_Id(any(Long.class)))
                .thenReturn(dogReports);
        doNothing().when(dogReportRepository).deleteAll(anyCollection());

        SendMessage actual = dogReportService.deleteDogsFromReportByDogId(message);
        verify(dogReportRepository).deleteAll(anyCollection());
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void getMissingReports() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        Collection<AdoptedDog> adoptedDogs = List.of(
                new AdoptedDog("dog1", Date.valueOf("2023-01-12"), 30),
                new AdoptedDog("dog2", Date.valueOf("2023-01-12"), 30),
                new AdoptedDog("dog3", Date.valueOf("2023-01-12"), 30)
        );
        when(validatorDogReportService.getDateFromMessage(any(Message.class))).thenReturn(Date.valueOf("2021-01-12"));
        when(adoptedDogRepository.findMissingReportAdoptedDog(any(Date.class))).thenReturn(adoptedDogs);

        List<SendMessage> actual = dogReportService.getMissingReports(message);
        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isNotNull();
        });
    }

    @Test
    void getMissingReportsBad() throws URISyntaxException, IOException {
        String answer = "Неверный запрос даты! Отправьте в формате yyyy-mm-dd";
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);

        when(validatorDogReportService.getDateFromMessage(any(Message.class))).thenReturn(null);

        List<SendMessage> actual = dogReportService.getMissingReports(message);
        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isEqualTo(answer);
        });
    }

    @Test
    void sendWarning() throws URISyntaxException, IOException {

        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        AdoptedDog adoptedDog = createAdoptedDog();
        when(validatorDogReportService.getIdFromMessage(any(Message.class))).thenReturn(1L);
        when(adoptedDogRepository.findById(any(Long.class))).thenReturn(Optional.of(adoptedDog));

        List<SendMessage> actual = dogReportService.sendWarning(message);
        assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Предупреждение отправлено");
        assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(adoptedDog.getDogUser().getChatId());
        assertThat(actual.get(1).getParameters().get("text")).isNotNull();
    }


    @Test
    void sendWarningBadId() throws URISyntaxException, IOException {
        String answer = "Неверный запрос";
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_message.json").toURI()));
        Message message = getMessage(json);
        when(validatorDogReportService.getIdFromMessage(any(Message.class))).thenReturn(null);

        List<SendMessage> actual = dogReportService.sendWarning(message);
        actual.forEach(s -> {
            assertThat(s.getParameters().get("chat_id")).isEqualTo(message.from().id());
            assertThat(s.getParameters().get("text")).isEqualTo(answer);
        });

    }

    @Test
    void validateReportPositive() throws URISyntaxException, IOException {
        String answer = "Вы хотите отправить отчет?";
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_photo_message.json").toURI()));
        Message message = getMessage(json);
        AdoptedDog adoptedDog = createAdoptedDog();
        when(adoptedDogRepository.findAdoptedDogByDogName(any(String.class))).thenReturn(Optional.of(adoptedDog));
        when(dogUserRepository.findDogUserByChatId(any(Long.class))).thenReturn(adoptedDog.getDogUser());
        addReportToInquiry();
        SendMessage actual = dogReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void validateReportWrongPattern() throws URISyntaxException, IOException {
        String answer = "Мы не нашли усыновленную Вами собаку с таким именем!";
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_photo_message.json").toURI()));
        Message message = getMessage(json);
        addReportToInquiry();
        SendMessage actual = dogReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void validateReportNoCaption() throws URISyntaxException, IOException {
        String answer = "Добавьте к фото пояснительное сообщение и повторите отправку фото!";
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_photo_message_no_caption.json").toURI()));
        Message message = getMessage(json);
        addReportToInquiry();
        SendMessage actual = dogReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void validateReportWithoutMenu() throws URISyntaxException, IOException {
        String answer = "Если Вы хотите отправить отчет, то сделайте это через меню.";
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_photo_message.json").toURI()));
        Message message = getMessage(json);
        AdoptedDog adoptedDog = createAdoptedDog();
        SendMessage actual = dogReportService.validateReport(message);
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(message.from().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(answer);
    }

    @Test
    void addInquiryDogReport() throws URISyntaxException, IOException {
        SendMessage actual = addReportToInquiry();
        Long expected = 123L;
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(expected);
        assertThat(actual.getParameters().get("text")).isNotNull();
    }

    @Test
    void closeInquiryDogReport() {
    }

    private CallbackQuery getCallback(String json) {
        return BotUtils.fromJson(json, CallbackQuery.class);
    }

    private Message getMessage(String json) {
        return BotUtils.fromJson(json, Message.class);
    }

    private AdoptedDog createAdoptedDog() {
        DogUser dogUser = new DogUser("Owner", 12345, 1);
        AdoptedDog adoptedDog = new AdoptedDog("Питомец", null, 30);
        adoptedDog.setDogUser(dogUser);
        adoptedDog.setId(1L);
        return adoptedDog;
    }

    private SendMessage addReportToInquiry() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(DogReportService.class.getResource("animal_report_callback.json").toURI()));
        CallbackQuery callbackQuery = getCallback(json);

        doNothing().when(telegramBotSender).telegramSendPhoto(any(SendPhoto.class));
        return dogReportService.addInquiryDogReport(callbackQuery);
    }


    private DogReport createDogReport(Long id, Date date, String photo, String text, AdoptedDog adoptedDog) {
        DogReport dogReport = new DogReport();
        dogReport.setId(id);
        dogReport.setDate(date);
        dogReport.setPhoto(photo);
        dogReport.setText(text);
        dogReport.setAdoptedDog(adoptedDog);
        return dogReport;
    }

    private List<DogReport> getDogReports() {
        AdoptedDog adoptedDog = createAdoptedDog();
        return List.of(
                createDogReport(1L, Date.valueOf("2023-01-12"), "photo1", "test1", adoptedDog),
                createDogReport(2L, Date.valueOf("2023-01-12"), "photo2", "test2", adoptedDog),
                createDogReport(3L, Date.valueOf("2023-01-12"), "photo3", "test3", adoptedDog));
    }

}