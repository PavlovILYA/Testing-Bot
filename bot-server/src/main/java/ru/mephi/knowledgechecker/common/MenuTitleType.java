package ru.mephi.knowledgechecker.common;

import static ru.mephi.knowledgechecker.common.Constants.HOME;
import static ru.mephi.knowledgechecker.common.Constants.MENU_LINE;

public enum MenuTitleType {
    MAIN_MENU("" + MENU_LINE + "\n🏠\n" + HOME),
    PUBLIC_TEST_LIST("" + MENU_LINE + "\n📗\n" + HOME + " ➤ ПУБЛИЧНЫЕ ТЕСТЫ"),
    COURSES_LIST("" + MENU_LINE + "\n📚\n" + HOME + " ➤ КУРСЫ"),
    ADMIN_MENU("" + MENU_LINE + "\n🔐\n" + HOME + " ➤ АДМИНИСТРАТОРСКОЕ МЕНЮ"),
    TEST_SEARCH_RESULT("" + MENU_LINE + "\n🕵🏻‍\nПУБЛИЧНЫЕ ТЕСТЫ ➤ РЕЗУЛЬТАТЫ ПОИСКА"),
    COURSE_SEARCH_RESULT("" + MENU_LINE + "\n🕵🏻‍\nКУРСЫ ➤ РЕЗУЛЬТАТЫ ПОИСКА"),
    OUTPUT_COURSE_QUERIES("" + MENU_LINE + "\n🕵🏻‍\nКУРСЫ ➤ ОТКРЫТЫЕ ЗАЯВКИ"),
    STUDENTS("" + MENU_LINE + "\n👥\nКУРСЫ ➤ СТУДЕНТЫ"),
    MANAGE_TEST("" + MENU_LINE + "\n📃\nТЕСТ: "),
    MANAGE_COURSE("" + MENU_LINE + "\n📃\nКУРС: "),
    QUERY_TO_COURSE("" + MENU_LINE + "\n🕐\nЗАЯВКА НА КУРС: "),
    STUDENT("" + MENU_LINE + "\n🕐\nСТУДЕНТ: "),
    TEST_CREATING_TYPE(MENU_LINE + "\n🖌️\nВЫБЕРИТЕ ТИП СОЗДАНИЯ ТЕСТА");

    private final String title;

    MenuTitleType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
