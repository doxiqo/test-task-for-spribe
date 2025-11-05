package ua.com.vladyslav.spribe.players.negative;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.players.BasePlayerTest;

import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class PasswordTests extends BasePlayerTest {

    @Test(groups = {"negative"})
    @Description("Password must be between 7 and 15 and contain letters and digits: too short")
    public void rejectPasswordTooShort() {
        PlayerRequest req = createValidRandomUserBuilder().password("a1b2c3").buildRequest(); // 6 chars
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Password must contain digits")
    public void rejectPasswordNoDigits() {
        PlayerRequest req = createValidRandomUserBuilder().password("abcdefgh").buildRequest();
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Password must contain letters")
    public void rejectPasswordNoLetters() {
        PlayerRequest req = createValidRandomUserBuilder().password("12345678").buildRequest();
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Verify password max length enforced (>15)")
    public void rejectPasswordTooLong() {
        PlayerRequest req = createValidRandomUserBuilder().password("Abcdefghijklmnop123").buildRequest(); // >15
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }
}
