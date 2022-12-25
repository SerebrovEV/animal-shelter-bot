package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.service.BotUserService;
import liquibase.pro.packaged.B;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final BotUserService botUserService;

    @PostMapping
    public ResponseEntity<BotUser> createBotUser(@RequestBody BotUser botUser) {
        return ResponseEntity.ok(botUserService.addBotUser(botUser));
    }

    @GetMapping("{id}")
    public ResponseEntity<BotUser> getBotUser(@PathVariable Long id){
        return ResponseEntity.ok(botUserService.getBotUser(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteBotUser(@PathVariable Long id){
        botUserService.deleteBotUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<BotUser> editBotUser(@RequestBody BotUser botUser){
        return ResponseEntity.ok(botUserService.editBotUser(botUser));
    }



}
