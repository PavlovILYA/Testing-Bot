package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.repository.TestRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
}
