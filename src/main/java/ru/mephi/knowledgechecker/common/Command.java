package ru.mephi.knowledgechecker.common;

public enum Command {
    START("/start");

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
