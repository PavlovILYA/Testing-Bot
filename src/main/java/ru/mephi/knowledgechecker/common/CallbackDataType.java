package ru.mephi.knowledgechecker.common;

public enum CallbackDataType {
    TO_PUBLIC_TEST_LIST("📗 Список публичных тестов"),
    TO_MAIN_MENU("⬅️"),
    CREATE_PUBLIC_TEST("📝 Создать тест"),
    FIND_PUBLIC_TEST("🔎️ Найти тест"),
    TO_COURSES_LIST("📚 Список курсов"),
    ATTEND_COURSE("👩🏼‍🎓 Поступить на курс"),
    TO_ADMIN_MENU("🔐 Администраторское меню"),
    CREATE_COURSE("📝 Создать курс"),
    ADD_VARIABLE_QUESTION("☑️ С вариантами"),
    ADD_OPEN_QUESTION("💬 Открытый"),
    ADD_WRONG_VARIABLE_ANSWER("Добавить"),
    TO_QUESTION_ADDING("Добавить вопрос");

    private final String description;

    CallbackDataType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
