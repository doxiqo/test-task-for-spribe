package ua.com.vladyslav.spribe.players.negative;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.players.BasePlayerTest;

import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class RequiredFieldsTests extends BasePlayerTest {

    @Test(groups = {"negative"})
    @Description("Required fields: missing login")
    public void rejectMissingLogin() {
        PlayerRequest req = createValidRandomUserBuilder().login(null).buildRequest();
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Required fields: missing password")
    public void rejectMissingPassword() {
        PlayerRequest req = createValidRandomUserBuilder().password(null).buildRequest();
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Required fields: missing role")
    public void rejectMissingRole() {
        PlayerRequest req = createValidRandomUserBuilder().role(null).buildRequest();
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Required fields: missing screenName")
    public void rejectMissingScreenName() {
        PlayerRequest req = createValidRandomUserBuilder().screenName(null).buildRequest();
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Required fields: missing age")
    public void rejectMissingAge() {
        PlayerRequest req = createValidRandomUserBuilder().age(null).buildRequest();
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("Required fields: missing gender")
    public void rejectMissingGender() {
        PlayerRequest req = createValidRandomUserBuilder().gender(null).buildRequest();
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }
}
