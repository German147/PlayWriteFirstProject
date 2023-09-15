package org.example.base;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utils.TestProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightFactory {
    private static Logger log = LogManager.getLogger();
    private Playwright playwright;
    private TestProperties testProperties;

    public PlaywrightFactory(TestProperties testProperties) {
        this.testProperties = testProperties;
        playwright = Playwright.create();
    }

    public Page createPage(){
        Page page = null;
        try{
            page = (getBrowserContext().newPage());
        }catch (Exception e){
            log.error("Unable to create Page: " + e);
        }
        return page;
    }
    /**
     * Method to get the playwright {@link BrowserContext} with the video recording,
     * tracing. storage context and view port
     * These properties are set based on values on config properties
     *
     * @return BrowserContext - Returns playwright {@link BrowserContext} instance
     */
    private BrowserContext getBrowserContext() {
        BrowserContext browserContext;
        Browser browser = getBrowser();
        Browser.NewContextOptions newContextOptions = new Browser.NewContextOptions();

        if (Boolean.parseBoolean(testProperties.getProperty("enableRecordVideo"))) {
            Path path = Paths.get(testProperties.getProperty("recordVideoDirectory"));
            newContextOptions.setRecordVideoDir(path);
            log.info("Browser Context - Video Recording is enabled at location '{}'", path.toAbsolutePath());
        }

        int viewPortHeight = Integer.parseInt(testProperties.getProperty("viewPortHeight"));
        int viewPortWidth = Integer.parseInt(testProperties.getProperty("viewPortWidth"));
        newContextOptions.setViewportSize(viewPortWidth, viewPortHeight);
        log.info("Browser Context - Viewport Width '{}' and Height '{}'", viewPortWidth, viewPortHeight);

        if (Boolean.parseBoolean(testProperties.getProperty("useSessionState"))) {
            Path path = Paths.get(testProperties.getProperty("sessionState"));
            newContextOptions.setStorageStatePath(path);
            log.info("Browser Context - Used the Session Storage State at location '{}'", path.toAbsolutePath());
        }

        browserContext = (browser.newContext(newContextOptions));

        if (Boolean.parseBoolean(testProperties.getProperty("enableTracing"))) {
            browserContext.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
            log.info("Browser Context - Tracing is enabled with Screenshots and Snapshots");
        }
        return browserContext;
    }
    /**
     * Method is to get playwright {@link Browser} instance of browser property in
     * config file with headless mode property
     *
     * @return Browser - Returns playwright {@link String} instance
     * @throws IllegalArgumentException - Throws Exception when no matching browser
     *                                  is available for property
     */
    private Browser getBrowser() throws IllegalArgumentException {
        String browserName = testProperties.getProperty("browser");
        boolean headless = Boolean.parseBoolean(testProperties.getProperty("headless"));
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);
        BrowserType browserType;
        switch (browserName.toLowerCase()) {
            case "chromium":
                browserType = playwright.chromium();
                break;
            case "firefox":
                browserType = playwright.firefox();
                break;
            case "safari":
                browserType = playwright.webkit();
                break;
            case "chrome":
                browserType = playwright.chromium();
                launchOptions.setChannel("chrome");
                break;
            case "edge":
                browserType = playwright.chromium();
                launchOptions.setChannel("msedge");
                break;
            default:
                String message = "Browser Name '" + browserName + "' specified in Invalid.";
                message += " Please specify one of the supported browsers [chromium, firefox, safari, chrome, edge].";
                log.debug(message);
                throw new IllegalArgumentException(message);
        }
        log.info("Browser Selected for Test Execution '{}' with headless mode as '{}'", browserName, headless);
        return browserType.launch(launchOptions);
    }

}
