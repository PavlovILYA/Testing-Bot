package ru.mephi.knowledgechecker.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateContext {
    private final List<UpdateStrategy> updateStrategies;

    public void process(Update update) {
        for (UpdateStrategy strategy : updateStrategies) {
            if (strategy.apply(update)) {
                strategy.process(update);
                return;
            }
        }
    }
}
