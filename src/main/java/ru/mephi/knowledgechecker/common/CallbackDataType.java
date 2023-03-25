package ru.mephi.knowledgechecker.common;

public enum CallbackDataType {
    TO_PUBLIC_TEST_LIST("📗 Список публичных тестов"),
    TO_MAIN_MENU("⬅️"),
    CREATE_TEST("📝 Создать тест"),
    FIND_PUBLIC_TEST("🔎️ Найти тест"),
    TO_COURSES_LIST("📚 Список курсов"),
    FIND_COURSE("🔎️ Найти курс"),
    PARTICIPATE_IN_COURSE("👩🏼‍🎓 Поступить на курс"),
    CANCEL_OUTPUT_QUERY("❌ Отменить"),
    TO_OUTPUT_COURSE_QUERIES("Активные заявки"),
    TO_ADMIN_MENU("🔐 Администраторское меню"),
    CREATE_COURSE("📝 Создать курс"),
    ADD_VARIABLE_QUESTION("☑️ С вариантами"),
    ADD_OPEN_QUESTION("💬 Открытый"),
    ADD_WRONG_VARIABLE_ANSWER("Добавить"),
    TO_QUESTION_ADDING("Добавить вопрос"),
    TO_STUDENTS("Список студентов"),
    TO_INPUT_COURSE_QUERIES("Заявки на обучение"),
    TO_PRIVATE_TEST_LIST("Тесты курса"),
    ACADEMIC_PERFORMANCE("Успеваемость"),
    SOLVE_OWN_TEST("Попробовать пройти"),
    MANAGE_VISIBILITY("Настроить видимость"),
    TO_TRAIN_TESTS("Тренировочные тесты"),
    TO_ESTIMATED_TESTS("Тесты на оценку"),
    NEXT("⏩️"),
    PREVIOUS("⏪️"),
    DELETE_TEST("❌"),
    EDIT_TEST("✏️"),
    ACCEPT_INPUT_QUERY("✅"),
    REJECT_INPUT_QUERY("⛔️"),
    BLOCK_STUDENT("❌");

    private final String description;

    CallbackDataType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
