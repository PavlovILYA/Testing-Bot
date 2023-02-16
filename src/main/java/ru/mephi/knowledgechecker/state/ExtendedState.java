package ru.mephi.knowledgechecker.state;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.mephi.knowledgechecker.common.DataType;

import java.util.Map;

@Data
@AllArgsConstructor
public class ExtendedState {
    private BotState state;
    private Map<DataType, Object> data;
}
