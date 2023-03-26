package ru.mephi.knowledgechecker.model.answer;

public enum MarkType {
    MARK_0("Неверно", 0),
    MARK_1("1 балл", 1),
    MARK_2("2 балла", 2),
    MARK_3("3 балла", 3),
    MARK_4("4 балла", 4),
    MARK_5("5 баллов", 5);

    private final String description;
    private final long mark;

    MarkType(String description, int mark) {
        this.description = description;
        this.mark = mark;
    }

    public String getDescription() {
        return description;
    }

    public long getMark() {
        return mark;
    }

    public static MarkType of(String description) {
        switch (description) {
            case "Неверно":
                return MARK_0;
            case "1 балл":
                return MARK_1;
            case "2 балла":
                return MARK_2;
            case "3 балла":
                return MARK_3;
            case "4 балла":
                return MARK_4;
            case "5 баллов":
                return MARK_5;
            default:
                return null;
        }
    }
}
