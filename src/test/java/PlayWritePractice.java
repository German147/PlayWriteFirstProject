import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlayWritePractice extends TestBase {

    private static Logger log = LogManager.getLogger();

    public void firstTest() {

        try (Playwright pw = Playwright.create();) {
            BrowserType browserType = pw.chromium();
            Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome"));
            Page page = browser.newPage();
            page.navigate("https://playwright.dev");
            System.out.println(page.title());
        }
    }

    public void firstTestRefactored() {

        try (Playwright pw = Playwright.create();) {
            Page page = pw.chromium().launch().newPage();
            page.navigate("https://playwright.dev");
            System.out.println(page.title());
        }
    }

    public void browserSupport() {
        try (Playwright pw = Playwright.create()) {
            List<BrowserType> browserTypes = List.of(pw.chromium(), pw.firefox(), pw.webkit());

            for (BrowserType type : browserTypes) {
                Page page = type.launch().newPage();
                page.navigate("https://www.whatsmybrowser.org/");
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(type.name() + ".png")));
            }
        }
    }

    @Test
    public void testherokuapp() {

        try (Playwright pw = Playwright.create()) {
            BrowserType browserType = pw.chromium();
            Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome"));
            Page page = browser.newPage();
            log.info("Launched Page");
            page.navigate("https://the-internet.herokuapp.com/");
            page.locator("//a[contains(text(),'Form Authentication')]").click();
            page.getByRole(AriaRole.TEXTBOX,
                    new Page.GetByRoleOptions().setName("username")).fill("tomsmith");
            page.getByRole(AriaRole.TEXTBOX,
                    new Page.GetByRoleOptions().setName("password")).type("SuperSecretPassword!");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" Login"))
                    .click();
            log.info("Credentials were retrieved");
            boolean pageOpened = (page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Welcome to the Secure Area. When you are done click logout below."))).isVisible();
            Assert.assertTrue(pageOpened, "The login was unsuccessful");
        }
    }

    @Test
    public void testNavigate() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome"));
            Page page = browser.newPage();
            page.navigate("http://playwright.dev");

            // Expect a title "to contain" a substring.
            assertThat(page).hasTitle(Pattern.compile("Playwright"));

            // create a locator
            Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));

            // Expect an attribute "to be strictly equal" to the value.
            assertThat(getStarted).hasAttribute("href", "/docs/intro");

            // Click the get started link.
            getStarted.click();

            // Expects the URL to contain intro.
            assertThat(page).hasURL(Pattern.compile(".*intro"));
        }
    }

    @Test
    public void testInputCredentials() {
        HomePage homepage = new HomePage(page);
        String title = homepage.getHomePageTitle();
        System.out.println(title);
    }
}
