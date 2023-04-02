package ru.mephi.knowledgechecker.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String status;
    private String message;
    private String timestamp;
}
