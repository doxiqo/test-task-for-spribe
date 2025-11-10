package ua.com.vladyslav.spribe.players;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.data.factories.PlayerDataProviders;
import ua.com.vladyslav.spribe.models.PlayerRequest;

import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class RequiredFieldsTests extends BasePlayerTest {

    @Test(dataProvider = "requiredFields", dataProviderClass = PlayerDataProviders.class, groups = {"negative"})
    @Description("Verify player creation fails when required fields are missing")
    public void rejectMissingRequiredField(String missingField) {
        LOG.info("Testing missing required field: {}", missingField);

        var builder = createValidRandomUserBuilder();

        switch (missingField) {
            case "login" -> builder.login(null);
            case "password" -> builder.password(null);
            case "role" -> builder.role(null);
            case "screenName" -> builder.screenName(null);
            case "age" -> builder.age(null);
            case "gender" -> builder.gender(null);
            default -> throw new IllegalArgumentException("Unknown field: " + missingField);
        }

        PlayerRequest req = builder.buildRequest();

        playerApiService.invalidCreation(req, SUPERVISOR_LOGIN);
    }
}
