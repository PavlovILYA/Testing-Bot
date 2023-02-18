package ru.mephi.knowledgechecker.dto.telegram.income;

public class InvalidUpdateException extends RuntimeException {
    public InvalidUpdateException(Long id) {
        super("Update " + id + " doesn't have neither message nor callback query");
    }
}
