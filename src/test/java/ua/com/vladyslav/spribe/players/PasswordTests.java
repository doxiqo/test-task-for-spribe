package ua.com.vladyslav.spribe.players;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.data.factories.PlayerDataProviders;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.models.PlayerResponse;

import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.PlayerBuilder.getBuilderFromPlayerResponse;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class PasswordTests extends BasePlayerTest {

    @Test(dataProvider = "invalidPasswords", dataProviderClass = PlayerDataProviders.class, groups = {"negative"})
    @Description("Verify password validation rejects invalid formats and lengths")
    public void rejectInvalidPasswordsOnCreate(String password, String reason) {
        PlayerRequest req = createValidRandomUserBuilder()
                .password(password)
                .buildRequest();

        LOG.info("Testing invalid password case: {} -> {}", reason, password);
        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }

    @Test(dataProvider = "validPasswords", dataProviderClass = PlayerDataProviders.class, groups = {"positive"})
    @Description("Verify password validation accepts valid formats and boundary lengths")
    public void acceptValidPasswordsOnCreate(String password, String reason) {
        PlayerRequest req = createValidRandomUserBuilder()
                .password(password)
                .buildRequest();

        LOG.info("Testing valid password case: {} -> {}", reason, password);
        var created = playerApiService.createPlayer(req, SUPERVISOR_LOGIN);

        PlayerRequest fetched = getBuilderFromPlayerResponse(playerApiService.getPlayer(created.id())).buildRequest();
        org.testng.Assert.assertNotNull(fetched.password(), "Password should be accepted for valid case: " + reason);
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = PlayerDataProviders.class, groups = {"negative"})
    @Description("Verify invalid passwords are rejected during player update")
    public void rejectInvalidPasswordOnUpdate(String password, String reason) {
        LOG.info("Testing invalid password on UPDATE: '{}', reason: {}", password, reason);

        PlayerResponse created = playerApiService.createPlayer(
                createValidRandomUserBuilder().buildRequest(),
                SUPERVISOR_LOGIN);

        PlayerRequest invalidUpdate = getBuilderFromPlayerResponse(created)
                .password(password)
                .buildRequest();

        playerApiService.invalidUpdate(invalidUpdate, SUPERVISOR_LOGIN, created.id());
    }
}
