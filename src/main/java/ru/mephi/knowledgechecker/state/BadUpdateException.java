package ru.mephi.knowledgechecker.state;

public class BadUpdateException extends Exception {
    public BadUpdateException(Long updateId) {
        super("Update " + updateId + " doesn't have neither message nor callback query");
    }
}
