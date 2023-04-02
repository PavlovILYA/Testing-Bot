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
import ru.mephi.knowledgechecker.exception.ErrorResponse;
import ru.mephi.knowledgechecker.exception.FileParsingException;
import ru.mephi.knowledgechecker.exception.TestNotFoundException;
import ru.mephi.knowledgechecker.service.DocumentService;

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
public class DocumentController {
    private final DocumentService documentService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/users/{userId}/tests")
    public Long saveTest(@Positive @PathVariable(value = "userId") Long userId,
                        @RequestParam("file") MultipartFile file,
                        RedirectAttributes redirectAttributes) {
        log.info("GET /users/{}/tests", userId);
        try {
            byte[] b = file.getBytes();
            log.info("file:\n{}", new String(b));
            return documentService.saveFile(userId, file);
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
            File file = documentService.loadAsResource(userId, testId);
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
    public ErrorResponse handle404Exception(final Exception e) {
        log.error("{} {}", HttpStatus.NOT_FOUND, e.getMessage());
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ExceptionHandler({RuntimeException.class, FileParsingException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle500Exception(final Exception e) {
        log.error("{} {}", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }
}
