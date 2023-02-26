package ru.mephi.knowledgechecker.common;

import static ru.mephi.knowledgechecker.common.Constants.HOME;
import static ru.mephi.knowledgechecker.common.Constants.MENU_LINE;

public enum MenuTitleType {
    MAIN_MENU("" + MENU_LINE + "\n🏠\n" + HOME),
    PUBLIC_TEST_LIST("" + MENU_LINE + "\n📗\n" + HOME + " ➤ ПУБЛИЧНЫЕ ТЕСТЫ"),
    COURSES_LIST("" + MENU_LINE + "\n📚\n" + HOME + " ➤ КУРСЫ"),
    ADMIN_MENU("" + MENU_LINE + "\n🔐\n" + HOME + " ➤ АДМИНИСТРАТОРСКОЕ МЕНЮ"),
    SEARCH_RESULT("" + MENU_LINE + "\n🕵🏻‍\nПУБЛИЧНЫЕ ТЕСТЫ ➤ РЕЗУЛЬТАТЫ ПОИСКА"),
    MANAGE_TEST("" + MENU_LINE + "\n📃\nТЕСТ: ");

    private final String title;

    MenuTitleType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
