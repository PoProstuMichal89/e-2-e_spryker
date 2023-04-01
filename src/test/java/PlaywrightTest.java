import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaywrightTest {

    private static final String newsletterInputLocator = "#newsletterSubscriptionWidgetForm_subscribe";
    private static final String newsletterSentButtonLocator = "#subscription > div > div > div > div:nth-child(2) > button";
    private static final String newsletterFormAlertLocator =
            "body > footer > div > div.footer__top > div > div > div > section > div.text-alert.subscription-form__alert";
    private static final String myAccountLocator = "[data-qa='component navigation-top'] >ul >li:nth-child(2)";
    private static final String singUpLocator = "toggler-radio__box";
    private static final String singUpLocator2 = "[name='accountLoginSwitcher']";
    private static String testEmail = Helpers.generateFakeEmailAddress();
    private static String validEmail = "toster.oprogramowania2@gmail.com";
    private static String passToValidEmail = "auvkGi3MShbRpwcdMX3!@";

    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setDevtools(true));
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
        page = context.newPage();
        page.navigate("http://yves.de.spryker.local/");
    }

    @Test
    public void pageVerification() {
        assertThat(page).hasTitle(Pattern.compile("Spryker Shop"));
    }

    @Test
    public void signUp() {
        /*nawigacja do strony rejestracji*/
        ElementHandle myAccount = page.querySelector(myAccountLocator);
        myAccount.click();
        ElementHandle singUpButton = page.waitForSelector(".toggler-radio__box");
        singUpButton.click();

        /*wypełnianie danych*/
        page.locator("#registerForm_first_name").fill("Michal");
        page.locator("#registerForm_last_name").fill("Mazur");
        page.locator("#registerForm_email").fill(testEmail);
        page.locator("#registerForm_password_pass").fill("Wehisay126@cyclesat.com");
        page.locator("#registerForm_password_confirm").fill("Wehisay126@cyclesat.com");
        page.waitForSelector(".checkbox__box").check();
//        page.locator("[data-qa='submit-button']").click();
        /*zatwierdzanie danych (przycisk singUP) */
        page.locator("body > div > main > div > div > div.js-customer-page-login-register > div > form > div > div.form__actions > button").click();
//        assertThat(page.locator("[data-qa='component flash-message']")).isVisible();

        /*weryfikacja czy zapisano poprawnie e-mail)*/
        ElementHandle succesNotification = page.waitForSelector("[data-qa='component flash-message']");
        String succesMessage = succesNotification.innerText();
        String expectedMessage = "Almost there! We send you an email to validate your email address. Please confirm it to be able to log in.\n" +
                "Ok!";
        assertEquals(expectedMessage, succesMessage);
    }

    @Test
    public void singIn() {
        page.navigate("http://yves.de.spryker.local/en/login");
        page.querySelector("#loginForm_email").fill(validEmail);
        page.querySelector("#loginForm_password").fill(passToValidEmail);
//        page.locator("div:contains(\"Login\")").isVisible();
        page.waitForSelector("body > div > main > div > div > div.js-customer-page-login > div > form > div > div.form__actions > button")
                .click(); //do porawy selektor
        ElementHandle userEmailAddress = page.querySelector(".menu--customer-account > li:nth-child(2) > a ");
        String userEmail = userEmailAddress.innerText();
        assertEquals(validEmail, userEmail);
        page.pause();
    }

    @Test
    public void singOut(){
        page.querySelector(myAccountLocator).hover();
        page.pause();
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
}
