package ru.mephi.knowledgechecker.dto.telegram.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Update {
    @JsonProperty(value = "update_id")
    private Long id;
    private Message message;
    @JsonProperty(value = "callback_query")
    private CallbackQuery callbackQuery;

    public Long getUserId() {
        if (message != null) {
            return message.getFrom().getId();
        } else if (callbackQuery != null) {
            return callbackQuery.getFrom().getId();
        } else {
            throw new InvalidUpdateException(id);
        }
    }
}
