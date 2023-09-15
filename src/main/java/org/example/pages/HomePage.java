package org.example.pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

public class HomePage {
    private Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    public String getHomePageTitle() {
        page.waitForLoadState();
        return page.title();
    }

}
