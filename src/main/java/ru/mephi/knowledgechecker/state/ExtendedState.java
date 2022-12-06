package ru.mephi.knowledgechecker.state;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ExtendedState {
    private BotState state;
    private Map<String, Object> data;
}
