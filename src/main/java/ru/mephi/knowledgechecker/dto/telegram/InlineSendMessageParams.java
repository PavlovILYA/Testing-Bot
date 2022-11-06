package ru.mephi.knowledgechecker.dto.telegram;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InlineSendMessageParams {
    @JsonProperty("chat_id")
    private Long chatId;
    private String text;
    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;
}
