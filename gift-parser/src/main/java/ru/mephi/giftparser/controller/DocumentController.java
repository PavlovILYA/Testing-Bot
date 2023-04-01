package ru.mephi.giftparser.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.giftparser.service.DocumentService;

import javax.validation.constraints.Positive;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

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
                          @Positive @PathVariable("testId") Long testId) { // @PathVariable - fileType
        log.info("GET /users/{}/tests/{}", userId, testId);
        File file = null;
        try {
            file = documentService.loadAsResource(userId, testId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        HttpHeaders responseHeader = (HttpHeaders) RequestContextHolder.getRequestAttributes().getAttribute("responseHeader", RequestAttributes.SCOPE_REQUEST);
//        return new ResponseEntity<>(new ByteArrayResource(data), responseHeader, HttpStatus.OK);

//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + file.getFilename() + "\"").body(file);

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok()
//                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
