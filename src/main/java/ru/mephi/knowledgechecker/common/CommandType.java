package ru.mephi.knowledgechecker.common;

public enum CommandType {
    START("/start");

    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
