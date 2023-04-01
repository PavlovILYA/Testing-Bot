package ru.mephi.knowledgechecker.dto.telegram.outcome.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendPopupParams {
    @JsonProperty("callback_query_id")
    private String callbackQueryId;
    private String text;
    @JsonProperty("show_alert")
    private Boolean showAlert;
}
