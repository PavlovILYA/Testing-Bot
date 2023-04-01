package ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;

import java.util.List;

@Data
@Builder
public class ReplyKeyboardMarkup implements KeyboardMarkup {
    private List<List<KeyboardButton>> keyboard;
    @JsonProperty("resize_keyboard")
    private Boolean resizeKeyboard;
    @JsonProperty("one_time_keyboard")
    private Boolean oneTimeKeyboard;
    @JsonProperty("input_field_placeholder")
    private String inputFieldPlaceholder;
}
