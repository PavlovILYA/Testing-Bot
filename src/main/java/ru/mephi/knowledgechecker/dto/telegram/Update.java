package ru.mephi.knowledgechecker.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Update {
    @JsonProperty(value = "update_id")
    private Long id;
    private Message message;

}
