package ru.mephi.knowledgechecker.dto.telegram.outcome.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardMarkup;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageTextParams {
    @JsonProperty("chat_id")
    private Long chatId;
    @JsonProperty("message_id")
    private Long messageId;
    private String text;
    private List<MessageEntity> entities;
    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    public EditMessageTextParams(Long messageId, SendMessageParams params) {
        this.chatId = params.getChatId();
        this.messageId = messageId;
        this.text = params.getText();
        this.entities = params.getEntities();
        this.replyMarkup = (InlineKeyboardMarkup) params.getReplyMarkup();
    }
}
