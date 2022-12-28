package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.repository.BotUserRepository;
import com.animalshelter.animalshelterbot.service.BotUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BotUserRepository botUserRepository;
    @SpyBean
    private BotUserService botUserService;
    @InjectMocks
    private AdminUserController adminUserController;

    private BotUser BOT_USER1;
    private BotUser BOT_USER2;
    private Long ID;
    private Long CHAT_ID;
    private Long PHONE_NUMBER;
    private String NAME_USER;

    @BeforeEach
    public void setOut() {
        ID = 1L;
        CHAT_ID = 150123L;
        PHONE_NUMBER = 89871234567L;
        NAME_USER = "Test";
        BOT_USER1 = new BotUser();
        BOT_USER1.setId(ID);
        BOT_USER1.setChatId(CHAT_ID);
        BOT_USER1.setUserName(NAME_USER);
        BOT_USER1.setPhoneNumber(PHONE_NUMBER);

    }


    @Test
    void createBotUser() throws Exception {

        JSONObject jsBotUser = new JSONObject();
        jsBotUser.put("id", ID);
        jsBotUser.put("chatId", CHAT_ID);
        jsBotUser.put("userName", NAME_USER);
        jsBotUser.put("phoneNumber", PHONE_NUMBER);


        when(botUserRepository.save(any(BotUser.class))).thenReturn(BOT_USER1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/")
                        .content(jsBotUser.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.chatId").value(CHAT_ID))
                .andExpect(jsonPath("$.userName").value(NAME_USER))
                .andExpect(jsonPath("$.phoneNumber").value(PHONE_NUMBER));


    }

    @Test
    void getBotUser() throws Exception {

        when(botUserRepository.findById(anyLong())).thenReturn(Optional.of(BOT_USER1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}", ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.chatId").value(CHAT_ID))
                .andExpect(jsonPath("$.userName").value(NAME_USER))
                .andExpect(jsonPath("$.phoneNumber").value(PHONE_NUMBER));

    }

    @Test
    void deleteBotUser() throws Exception {

        when(botUserRepository.findById(anyLong())).thenReturn(Optional.of(BOT_USER1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/{id}", ID))
                .andExpect(status().isOk());
        verify(botUserRepository, atLeastOnce()).deleteById(1L);
    }

    @Test
    void editBotUser() throws Exception {

        JSONObject jsBotUser = new JSONObject();
        jsBotUser.put("id", ID);
        jsBotUser.put("chatId", 2L);
        jsBotUser.put("userName", "Test2");
        jsBotUser.put("phoneNumber", PHONE_NUMBER);

        BOT_USER2 = new BotUser();
        BOT_USER2.setId(ID);
        BOT_USER2.setChatId(2L);
        BOT_USER2.setUserName("Test2");
        BOT_USER2.setPhoneNumber(PHONE_NUMBER);

        when(botUserRepository.save(any(BotUser.class))).thenReturn(BOT_USER2);
        when(botUserRepository.findById(anyLong())).thenReturn(Optional.of(BOT_USER1));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/")
                        .content(jsBotUser.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.chatId").value(2L))
                .andExpect(jsonPath("$.userName").value("Test2"))
                .andExpect(jsonPath("$.phoneNumber").value(PHONE_NUMBER));


    }

    @Test
    void getAllBotUser() throws Exception {

        BotUser botUser2 = new BotUser();
        BotUser botUser3 = new BotUser();
        BotUser botUser4 = new BotUser();
        BotUser botUser5 = new BotUser();
        BotUser botUser6 = new BotUser();
        BotUser botUser7 = new BotUser();
        BotUser botUser8 = new BotUser();
        List<BotUser> botUsers = List.of(
                BOT_USER1,
                botUser2,
                botUser3,
                botUser4,
                botUser5,
                botUser6,
                botUser7,
                botUser8);

        when(botUserRepository.findAll()).thenReturn(botUsers);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(botUsers)));

    }
}