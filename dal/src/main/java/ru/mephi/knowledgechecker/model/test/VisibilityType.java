package ru.mephi.knowledgechecker.model.test;

public enum VisibilityType {
    TRAIN("Сделать тренировочным"),
    INVISIBLE("Перестать отображать"),
    ESTIMATED("Дать доступ на оценку");

    private final String description;

    VisibilityType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
