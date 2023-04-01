package ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;

import java.util.List;

@Data
@Builder
public class InlineKeyboardMarkup implements KeyboardMarkup {
    @JsonProperty("inline_keyboard")
    private List<List<InlineKeyboardButton>> inlineKeyboard;
}
