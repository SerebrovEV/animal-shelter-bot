//package com.animalshelter.animalshelterbot.controllers;
//
//import com.animalshelter.animalshelterbot.model.BotUser;
//import com.animalshelter.animalshelterbot.service.BotUserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.ArraySchema;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import liquibase.pro.packaged.B;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * <i>Контроллер для добавления, редактирования, проверки наличия и получения
// * всех {@link BotUser} в/из базы данных администратором
// * </i>
// */
//@RestController
//@RequestMapping("user")
//@RequiredArgsConstructor
//public class AdminUserController {
//    private final BotUserService botUserService;
//
//    @Operation(
//            summary = "Добавление BotUser в базу данных",
//            description = "Контроллер createBotUser для добавления BotUser в базу данных",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "Пример BotUser для добавления в базу данных",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = BotUser.class)
//                    )
//            ),
//            responses = @ApiResponse(
//                    responseCode = "200",
//                    description = "BotUser добавлен",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = BotUser.class)
//                    )
//            ),
//            tags = "BotUser")
//    @PostMapping
//    public ResponseEntity<BotUser> createBotUser(@RequestBody BotUser botUser) {
//        return ResponseEntity.ok(botUserService.addBotUser(botUser));
//    }
//
//    @Operation(summary = "Поиск BotUser по id",
//            description = "Контроллер getBotUser для поиска BotUser по id",
//            tags = "BotUser")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "BotUser найден",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = BotUser.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Ошибка! BotUser отсутсвует в базе данных"
//            )}
//    )
//    @GetMapping("{id}")
//    public ResponseEntity<BotUser> getBotUser(
//            @Parameter(description = "Поиск BotUser по id",
//                    //allowEmptyValue = false,  пример аннотации для обязательных параметров, по умолчанию false
//                    example = "1")
//            @PathVariable Long id) {
//        return ResponseEntity.ok(botUserService.getBotUser(id));
//    }
//
//    @Operation(summary = "Удаление BotUser по id",
//            description = "Контроллер deleteBotUser для удаления BotUser из базы данных по id",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "BotUser удален"
//                    ),
//                    @ApiResponse(
//                            responseCode = "404",
//                            description = "BotUser не найден"
//                    )
//            },
//            tags = "BotUser"
//    )
//    @DeleteMapping("{id}")
//    public ResponseEntity deleteBotUser(@Parameter(description = "Удаление BotUser по id", example = "1")
//                                        @PathVariable Long id) {
//        botUserService.deleteBotUser(id);
//        return ResponseEntity.ok().build();
//    }
//
//
//    @Operation(
//            summary = "Редактирование BotUser в базе данных",
//            description = "Контроллер editBotUser для редактирования BotUser в базе данных",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "Пример BotUser для редактирования в базе данных",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = BotUser.class)
//                    )
//            ),
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "BotUser изменен",
//                            content = @Content(
//                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
//                                    schema = @Schema(implementation = BotUser.class)
//                            )
//                    ),
//                    @ApiResponse(
//                            responseCode = "404",
//                            description = "BotUser не найден"
//                    )
//            },
//            tags = "BotUser")
//    @PutMapping
//    public ResponseEntity<BotUser> editBotUser(@RequestBody BotUser botUser) {
//        return ResponseEntity.ok(botUserService.editBotUser(botUser));
//    }
//
//    @Operation(summary = "Получение всех BotUser из базы данных",
//            description = "Контроллер getAllBotUser для получения всех BotUser",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "Получен List всех BotUser",
//                            content = @Content(
//                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
//                                    array = @ArraySchema(
//                                            schema = @Schema(implementation = BotUser.class)
//                                    )
//                            )
//                    )
//            },
//            tags = "BotUser")
//    @GetMapping("/all")
//    public ResponseEntity<List<BotUser>> getAllBotUser() {
//        return ResponseEntity.ok(botUserService.getAll());
//    }
//
//}
