package ru.mephi.knowledgechecker.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Message {
    @JsonProperty(value = "message_id")
    private Long id;
    private User from;
    private Long date; // Instant.ofEpochMilli(date).toLocalDate();
    private String text;
    private Chat chat;
    private List<MessageEntity> entities;
}
