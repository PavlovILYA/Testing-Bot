package ru.mephi.knowledgechecker.dto.telegram.outcome;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageParams {
    @JsonProperty("chat_id")
    private Long chatId;
    private String text;
    @JsonProperty("reply_markup")
    private KeyboardMarkup replyMarkup;
}
