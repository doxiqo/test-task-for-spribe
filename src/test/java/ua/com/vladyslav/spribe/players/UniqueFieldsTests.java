package ua.com.vladyslav.spribe.players;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.models.PlayerResponse;

import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class UniqueFieldsTests extends BasePlayerTest {

    @Test(groups = {"negative"})
    @Description("Login must be unique")
    public void rejectDuplicateLogin() {
        PlayerRequest base = createValidRandomUserBuilder().buildRequest();
        PlayerResponse created = playerApiService.createPlayer(base, SUPERVISOR_LOGIN);
        PlayerRequest dup = createValidRandomUserBuilder().login(created.login()).buildRequest();
        playerApiService.invalidCreation(dup, SUPERVISOR_LOGIN);
    }

    @Test(groups = {"negative"})
    @Description("screenName must be unique")
    public void rejectDuplicateScreenName() {
        PlayerRequest base = createValidRandomUserBuilder().buildRequest();
        PlayerResponse created = playerApiService.createPlayer(base, SUPERVISOR_LOGIN);
        PlayerRequest dup = createValidRandomUserBuilder().screenName(created.screenName()).buildRequest();
        playerApiService.invalidCreation(dup, SUPERVISOR_LOGIN);
    }
}
