package com.animalshelter.animalshelterbot.controllers;

import com.animalshelter.animalshelterbot.model.BotUser;
import com.animalshelter.animalshelterbot.service.BotUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import liquibase.pro.packaged.B;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class AdminUserController {
    private final BotUserService botUserService;

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавление BotUser в базу данных",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BotUser.class)
                    )
            ),
            tags = "BotUser")
    @PostMapping
    public ResponseEntity<BotUser> createBotUser(@RequestBody BotUser botUser) {
        return ResponseEntity.ok(botUserService.addBotUser(botUser));
    }

    @Operation(summary = "Поиск усыновителя по id",
            description = "Поиск усыновителя по id",
            tags = "BotUser")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Поиск усыновителя по id",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BotUser.class)
//                            extensions = {
//                                    @Extension(
//                                            properties = ,
//                                    )
//                            }
                    )
            )
    }
    )
    @GetMapping("{id}")
    public ResponseEntity<BotUser> getBotUser(
            @Parameter(description = "Поиск усыновителя по id",
                    //allowEmptyValue = false,
                    example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(botUserService.getBotUser(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteBotUser(@PathVariable Long id) {
        botUserService.deleteBotUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<BotUser> editBotUser(@RequestBody BotUser botUser) {
        return ResponseEntity.ok(botUserService.editBotUser(botUser));
    }

    @Operation(summary = "Получение всех будущих, нынешних усыновителей из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получение списка всех BotUser",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BotUser.class)
                                    )
                            )
                    )
            },
            tags = "BotUser")
    @GetMapping("/all")
    public ResponseEntity<List<BotUser>> getAllBotUser() {
        return ResponseEntity.ok(botUserService.getAll());
    }

}
