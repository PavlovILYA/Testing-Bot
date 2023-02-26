package ru.mephi.knowledgechecker.model.solving;

public enum SolvingType {
    INSTANT_DEMONSTRATION_ANSWER("🔔 Показывать правильный ответ"),
    REPORT_GENERATING_AT_THE_END("🔕 Результат в конце теста");

    private final String description;

    SolvingType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
