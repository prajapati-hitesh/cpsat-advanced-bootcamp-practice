package com.ata.cpsat.practice.pages.setA;

import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.practice.enums.MockExamSetAMenu;
import org.openqa.selenium.By;

public class MockSetAHomePage {
    private String hamburgetMenuElement = "#menu-primary-spacious a";
    private String mainNavigationSection = ".main-nav";

    public void openMenu() {
        ElementHelper.getInstance().click(By.cssSelector(hamburgetMenuElement));
    }

    public void navigateTo(MockExamSetAMenu menu) {
        openMenu();
        ElementHelper.getInstance().click(By.linkText(menu.getMenuText()));
    }


}