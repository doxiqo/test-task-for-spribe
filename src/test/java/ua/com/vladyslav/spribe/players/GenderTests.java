package ua.com.vladyslav.spribe.players;

import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import ua.com.vladyslav.spribe.enums.Genders;
import ua.com.vladyslav.spribe.models.PlayersResponse;

import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;
import static ua.com.vladyslav.spribe.data.factories.TestDataFactory.createValidRandomUserBuilder;

public class GenderTests extends BasePlayerTest {

    @Test(groups = {"negative"})
    @Description("Verify exclusive duality of gender field")
    public void genderDualityTest() {
        playerApiService.invalidCreation(createValidRandomUserBuilder().gender(Genders.UNKNOWN).buildRequest(), SUPERVISOR_LOGIN);
        PlayersResponse players = playerApiService.getAllPlayers();
        Assert.assertListNotContains(players.players(), e -> e.gender().equals(Genders.UNKNOWN), "There is at least one player with UNKNOWN gender");
    }
}
