package ru.mephi.knowledgechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.knowledgechecker.exception.FileParsingException;
import ru.mephi.knowledgechecker.exception.ParserResponse;
import ru.mephi.knowledgechecker.exception.QuestionDataException;
import ru.mephi.knowledgechecker.exception.TestNotFoundException;
import ru.mephi.knowledgechecker.service.FileService;

import javax.validation.constraints.Positive;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MainController {
    private final FileService fileService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/users/{userId}/tests/{testId}")
    public ParserResponse saveTest(@Positive @PathVariable(value = "userId") Long userId,
                         @Positive @PathVariable("testId") Long testId,
                         @RequestParam("file") MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        log.info("GET /users/{}/tests", userId);
        try {
            int questionsNumber = fileService.importFile(userId, testId, new String(file.getBytes()));
            return ParserResponse.builder()
                    .status(201)
                    .message("Questions were saved!")
                    .questionsNumber(questionsNumber)
                    .timestamp(LocalDateTime.now().format(formatter))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/users/{userId}/tests/{testId}")
    @ResponseBody
    public Object getTest(@Positive @PathVariable(value = "userId") Long userId,
                          @Positive @PathVariable("testId") Long testId) {
        log.info("GET /users/{}/tests/{}", userId, testId);
        try {
            File file = fileService.exportFile(userId, testId);
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity.ok()
//                .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ExceptionHandler({TestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ParserResponse handle404Exception(final Exception e) {
        log.error("{} {}", HttpStatus.NOT_FOUND, e.getMessage());
        return ParserResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ExceptionHandler({Throwable.class, RuntimeException.class, FileParsingException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ParserResponse handle500Exception(final Exception e) {
        log.error("{} {}", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ParserResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ExceptionHandler(QuestionDataException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ParserResponse handle406Exception(final Exception e) {
        log.error("{} {}", HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        return ParserResponse.builder()
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }
}
