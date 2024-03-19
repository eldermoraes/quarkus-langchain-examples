import java.util.concurrent.TimeUnit;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static org.awaitility.Awaitility.*;

public class BookPage {
    
    private final Page page;
    private final Locator chat;
    private final Locator send;
    private final Locator input;

    public BookPage(Page page) {
        this.page = page;
        this.chat = page.getByTestId("chatwin");
        this.input = page.getByPlaceholder("enter your message");
        this.send = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("send"));
    }

    public void navigate() {
        page.navigate("http://localhost:8080/");
    }

    public void sendChat(String text) {
        this.input.click();
        this.input.fill(text);
        this.send.click();
    }

    public void waitUntilAgentResponse() {
         await().atMost(35, TimeUnit.SECONDS).until(() ->
            {
                return isAgentLastLine(this.chat);
            });
    }

    public String lastLineOfChat() {
        return removeAgent(getLastLine(this.chat));
    }

    private boolean isAgentLastLine(Locator chatwin) {
        String[] lines = chatwin.allTextContents().getLast().split(System.lineSeparator());
        return lines.length-1 > 0 && lines[lines.length-1].startsWith("[Agent]");
    }

    private String getLastLine(Locator chatwin) {
        String[] lines = chatwin.allTextContents().getLast().split(System.lineSeparator());
        return lines[lines.length-1];
    }

    private String removeAgent(String line) {
        return line.substring(line.indexOf(":") + 1).trim();
    }
}
