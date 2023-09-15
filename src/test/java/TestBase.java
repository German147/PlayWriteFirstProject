import com.microsoft.playwright.Page;
import org.example.base.PlaywrightFactory;
import org.example.utils.TestProperties;
import org.testng.annotations.BeforeMethod;

public class TestBase {
    protected Page page;
    protected static TestProperties testProperties;

//   @BeforeMethod
//    public void startPlaywrightServer() {
//        PlaywrightFactory pf = new PlaywrightFactory(testProperties);
//        page = pf.createPage();
//        page.navigate(testProperties.getProperty("url"));
//    }
}
