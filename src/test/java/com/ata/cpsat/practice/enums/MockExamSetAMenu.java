package com.ata.cpsat.practice.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public enum MockExamSetAMenu {
    Home("Home"),
    ABOUT_CTHACKATAHON("About #CTHackATAhon"),
    CHALLENGE_DOCUMENT("Challenge Document"),
    CHALLENGE_0("Challenge 0"),
    CHALLENGE_1("Challenge 1"),
    CHALLENGE_2("Challenge 2"),
    CHALLENGE_3("Challenge 3"),
    CHALLENGE_4("Challenge 4"),
    LIST_OF_PARTICIPANTS("List of Participants"),
    FORM("Form");

    private static final Map<String, MockExamSetAMenu> enumMAP;

    static {
        Map<String, MockExamSetAMenu> mainNavMap = Arrays
                .stream(values())
                .collect(toMap(cg -> cg.menu, e -> e));

        enumMAP = Collections.unmodifiableMap(mainNavMap);
    }

    private final String menu;

    MockExamSetAMenu(String menuText) {
        this.menu = menuText;
    }

    public static MockExamSetAMenu getEnum(String menuText) {
        return enumMAP.get(menuText);
    }

    public String getMenuText() {
        return menu;
    }
}
