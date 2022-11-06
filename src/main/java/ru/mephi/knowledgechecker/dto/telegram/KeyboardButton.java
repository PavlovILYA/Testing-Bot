package ru.mephi.knowledgechecker.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class KeyboardButton {
    private String text;
    @JsonProperty("request_contact")
    private Boolean requestContact;
}
