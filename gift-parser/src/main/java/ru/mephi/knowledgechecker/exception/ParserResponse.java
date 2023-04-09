package ru.mephi.knowledgechecker.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParserResponse {
    private int status;
    private String message;
    private String timestamp;
    @JsonProperty(value = "questions_number")
    private Integer questionsNumber;
}
