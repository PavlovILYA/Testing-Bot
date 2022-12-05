package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.repository.OpenAnswerRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAnswerService {
    private final OpenAnswerRepository openAnswerRepository;
}