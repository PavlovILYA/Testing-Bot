package ru.mephi.knowledgechecker.dto.telegram;

public class InvalidUpdateException extends Exception {
    public InvalidUpdateException(Long id) {
        super("Update " + id + " doesn't have neither message nor callback query");
    }
}
