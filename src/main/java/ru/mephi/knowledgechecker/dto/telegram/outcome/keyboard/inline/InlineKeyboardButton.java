package ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class InlineKeyboardButton {
    private String text;
//    private String url;
    @JsonProperty("callback_data")
    private String callbackData;
}
