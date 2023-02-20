package ru.mephi.knowledgechecker.common;

import static ru.mephi.knowledgechecker.common.Constants.HOME;
import static ru.mephi.knowledgechecker.common.Constants.MENU_LINE;

public enum MenuTitleType {
    MAIN_MENU(MENU_LINE + "\n🏠 " + HOME + "\n" + MENU_LINE),
    PUBLIC_TEST_LIST(MENU_LINE + "\n📗 " + HOME + " ➤ ПУБЛИЧНЫЕ ТЕСТЫ\n" + MENU_LINE),
    COURSES_LIST(MENU_LINE + "\n📚 " + HOME + " ➤ КУРСЫ\n" + MENU_LINE),
    ADMIN_MENU(MENU_LINE + "\n🔐 " + HOME + " ➤ АДМИНИСТРАТОРСКОЕ МЕНЮ\n" + MENU_LINE),
    SEARCH_RESULT(MENU_LINE + "\n🕵🏻‍ ПУБЛИЧНЫЕ ТЕСТЫ ➤ РЕЗУЛЬТАТЫ ПОИСКА\n" + MENU_LINE);

    private final String title;

    MenuTitleType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
