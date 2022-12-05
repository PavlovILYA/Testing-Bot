package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.repository.OpenQuestionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenQuestionService {
    private final OpenQuestionRepository openQuestionRepository;
}
