package ru.mephi.knowledgechecker.dto.telegram.outcome.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyboardButton {
    private String text;
    @JsonProperty("request_contact")
    private Boolean requestContact;
    @JsonProperty("request_location")
    private Boolean requestLocation;
}
