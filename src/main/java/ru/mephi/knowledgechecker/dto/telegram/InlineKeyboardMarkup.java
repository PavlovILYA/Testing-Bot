package ru.mephi.knowledgechecker.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class InlineKeyboardMarkup {
    @JsonProperty("inline_keyboard")
    private List<List<InlineKeyboardButton>> inlineKeyboard;
}
