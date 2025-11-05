package ua.com.vladyslav.spribe.api.facades;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ua.com.vladyslav.spribe.api.ApiClientFactory;
import ua.com.vladyslav.spribe.api.services.PlayerApiService;
import ua.com.vladyslav.spribe.data.factories.PlayerBuilder;
import ua.com.vladyslav.spribe.models.PlayerRequest;
import ua.com.vladyslav.spribe.models.PlayerResponse;
import ua.com.vladyslav.spribe.models.PlayersResponse;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.testng.Assert.assertNotNull;
import static ua.com.vladyslav.spribe.api.services.PlayerApiService.SUPERVISOR_LOGIN;

public class PlayerApiFacade {

    private final PlayerApiService apiService;
    private final List<PlayerResponse> createdPlayers;

    public PlayerApiFacade() {
        createdPlayers = new ArrayList<>();
        this.apiService = ApiClientFactory.playerApi();
    }

    public void cleanUp() {
        createdPlayers.forEach(e -> deletePlayer(e.id(), SUPERVISOR_LOGIN));
    }

    @Step("Create player")
    public PlayerResponse createPlayer(PlayerRequest request, String editor) {
        Response response = apiService.createPlayer(request, editor)
                .then()
                .statusCode(200)
                .extract()
                .response();
        PlayerResponse createdPlayer = deserializeResponse(response);
        createdPlayers.add(createdPlayer);
        return createdPlayer;
    }

    @Step("Invalid player creation")
    public void invalidCreation(PlayerRequest invalidPlayerRequest, String editor) {
        apiService.createPlayer(invalidPlayerRequest, editor)
                .then()
                .statusCode(400)
                .body(emptyOrNullString());
    }

    @Step("Invalid role player creation")
    public void invalidRoleCreation(PlayerRequest invalidPlayerRequest, String editor) {
        apiService.createPlayer(invalidPlayerRequest, editor)
                .then()
                .statusCode(403)
                .body(emptyOrNullString());
    }

    public void deletePlayerAndValidate(Integer playerId, String editor) {
        deletePlayer(playerId, editor)
                .statusCode(204)
                .body(emptyOrNullString());
    }

    @Step("Delete player by ID")
    private ValidatableResponse deletePlayer(Integer playerId, String editor) {
        return apiService.deletePlayer(PlayerBuilder.build().id(playerId).buildRequest(), editor)
                .then();
    }

    @Step("Get player by ID")
    public PlayerResponse getPlayer(Integer playerId) {
        Response response = apiService.getPlayer(PlayerBuilder.build().id(playerId).buildRequest())
                .then()
                .statusCode(200)
                .extract()
                .response();
        return deserializeResponse(response);
    }

    @Step("Get all players")
    public PlayersResponse getAllPlayers() {
        Response response = apiService.getALLPlayers()
                .then()
                .statusCode(200)
                .extract()
                .response();
        return response.as(PlayersResponse.class);
    }

    @Step("Update player by ID")
    public PlayerResponse updatePlayer(PlayerRequest updatedRequest, String editor, Integer userId) {
        Response response = apiService.updatePlayer(updatedRequest, editor, userId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        return deserializeResponse(response);
    }

    @Step("Invalid update player attempt")
    public void invalidUpdate(PlayerRequest updatedRequest, String editor, Integer userId) {
        apiService.updatePlayer(updatedRequest, editor, userId)
                .then()
                .statusCode(400)
                .body(emptyOrNullString());
    }

    @Step("Invalid delete player attempt")
    public void invalidDelete(Integer playerId, String editor) {
        apiService.deletePlayer(PlayerBuilder.build().id(playerId).buildRequest(), editor)
                .then()
                .statusCode(403)
                .body(emptyOrNullString());
    }

    private PlayerResponse deserializeResponse(Response response) {
        String body = response.getBody().asString();
        if (body == null || body.isBlank()) {
            throw new IllegalStateException("Response body is empty, cannot map to PlayersResponse");
        }
        PlayerResponse playerResponse = response.as(PlayerResponse.class);
        assertNotNull(playerResponse, "Response deserialized to null");
        assertNotNull(playerResponse.login(), "PlayerResponse.login is null");
        assertNotNull(playerResponse.id(), "PlayerResponse.id is null");
        return playerResponse;
    }
}
