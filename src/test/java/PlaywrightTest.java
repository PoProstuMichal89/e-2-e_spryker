import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightTest {

    private static final String newsletterInputLocator = "#newsletterSubscriptionWidgetForm_subscribe";
    private static final String newsletterSentButtonLocator = "#subscription > div > div > div > div:nth-child(2) > button";
    private static final String newsletterFormAlertLocator =
            "body > footer > div > div.footer__top > div > div > div > section > div.text-alert.subscription-form__alert";
    private static final String myAccountLocator = "[data-qa='component navigation-top'] >ul >li:nth-child(2)";
    private static final String singUpLocator = "toggler-radio__box";
    private static final String singUpLocator2 = "[name='accountLoginSwitcher']";

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
        page = context.newPage();
        page.navigate("http://yves.de.spryker.local/");
    }

    @Test
    public void testSpryker1() {
        assertThat(page).hasTitle(Pattern.compile("Spryker Shop"));
    }

    @Test
    public void subscriptionToNewslleterFail() {
        ElementHandle newsletterInput = page.querySelector(newsletterInputLocator);
        ElementHandle newsletterSentButton = page.querySelector(newsletterSentButtonLocator);

        newsletterInput.scrollIntoViewIfNeeded();
        newsletterInput.fill("miyiv43550@dogemn.com");
        page.waitForTimeout(3000);
        newsletterSentButton.click();
        assertThat(page.locator(newsletterFormAlertLocator)).containsText("You are already subscribed to the newsletter");
        page.pause();
    }

    @Test
    public void signUp(){
        ElementHandle myAccount = page.querySelector(myAccountLocator);
        myAccount.click();
        ElementHandle singUpButton = page.waitForSelector(".toggler-radio__box");
        singUpButton.click();
        page.locator("#registerForm_first_name").fill("Michal");
        page.locator("#registerForm_last_name").fill("Mazur");
        page.locator("#registerForm_email").fill("michumazur89@gmail.com");
        page.locator("#registerForm_password_pass").fill("Wehisay126@cyclesat.com");
        page.locator("#registerForm_password_confirm").fill("Wehisay126@cyclesat.com");
        page.waitForSelector(".checkbox__box").check();
        page.locator("[data-qa='submit-button']").click();
        page.locator("body > div > main > div > div > div.js-customer-page-login-register > div > form > div > div.form__actions > button").click();

        page.pause();
    }


}
