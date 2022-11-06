package ru.mephi.knowledgechecker.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class ReplyKeyboardMarkup {
    private List<List<KeyboardButton>> keyboard;
    @JsonProperty("resize_keyboard")
    private Boolean resizeKeyboard;
    @JsonProperty("one_time_keyboard")
    private Boolean oneTimeKeyboard;
    @JsonProperty("input_field_placeholder")
    private String inputFieldPlaceholder;
}
