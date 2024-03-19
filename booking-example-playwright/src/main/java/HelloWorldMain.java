import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

import static org.awaitility.Awaitility.*;

import java.util.concurrent.TimeUnit;

@QuarkusMain
@ActivateRequestContext
public class HelloWorldMain implements QuarkusApplication {

    @Inject
    ValidateResponse validateResponse;

    private boolean isAgentLastLine(Locator chatwin) {
        String[] lines = chatwin.allTextContents().getLast().split(System.lineSeparator());
        return lines.length-1 > 0 && lines[lines.length-1].startsWith("[Agent]");
    }

    private String getLastLine(Locator chatwin) {
        String[] lines = chatwin.allTextContents().getLast().split(System.lineSeparator());
        return lines[lines.length-1];
    }

    private String removeAgent(String line) {
        return line.substring(line.indexOf(":") + 1 ).trim();
    }

    @Override
    public int run(String... args) throws Exception {

        try (Playwright playwright = Playwright.create()) {
            
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false));
            BrowserContext context = browser.newContext();
            
            Page page = context.newPage();
            page.navigate("http://localhost:8080/");
            page.getByPlaceholder("enter your message").click();
            page.getByPlaceholder("enter your message").fill("Hi I want to get info on my booking");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("send")).click();
            
            Locator chatwin = page.getByTestId("chatwin");

            await().atMost(35, TimeUnit.SECONDS).until(() ->
            {
                return isAgentLastLine(chatwin);
            });

            System.out.println(chatwin.allTextContents().getLast());

        }

        return 0;
    }
}