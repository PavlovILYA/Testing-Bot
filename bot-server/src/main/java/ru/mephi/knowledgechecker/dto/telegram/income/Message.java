package ru.mephi.knowledgechecker.dto.telegram.income;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    @JsonProperty(value = "message_id")
    private Long id;
    private UserDto from;
    private Long date; // Instant.ofEpochMilli(date).toLocalDate();
    private String text;
    private Chat chat;
    private List<MessageEntity> entities;
    private Document document;
}
