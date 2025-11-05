package ua.com.vladyslav.spribe.validators;

import io.qameta.allure.Step;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.models.PlayerResponse;

import static org.testng.Assert.*;

public final class PlayerValidator {

    private PlayerValidator() {}

    @Step("Validate that PlayerResponse matches PlayerRequest")
    public static void validatePlayerResponseMatchesRequest(PlayerResponse response, PlayerRequest request) {
        assertNotNull(response, "PlayerResponse must not be null");
        if (response.id() != null && request.playerId() != null) {
            assertEquals(response.id(), request.playerId(),
                    "Player ID does not match request.playerId");
        }

        assertEquals(response.login(), request.login(), "Login does not match");
        assertEquals(response.age(), request.age(), "Age does not match");
        assertEquals(response.gender(), request.gender(), "Gender does not match");
        assertEquals(response.role(), request.role(), "Role does not match");
        assertEquals(response.screenName(), request.screenName(), "Screen name does not match");

        if (response.password() != null && request.password() != null) {
            assertEquals(response.password(), request.password(), "Password does not match");
        }
    }
}