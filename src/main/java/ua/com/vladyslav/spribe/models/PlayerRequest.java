package ua.com.vladyslav.spribe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import ua.com.vladyslav.spribe.enums.Genders;
import ua.com.vladyslav.spribe.enums.Roles;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlayerRequest(
        Integer playerId,
        String login,
        String email,
        Integer age,
        Genders gender,
        Roles role,
        String screenName,
        String password
) {

    public PlayerRequest(Integer playerId, String login, String email, Integer age, Genders gender, Roles role, String screenName, String password) {
        this.playerId = playerId;
        this.login = login;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.screenName = screenName;
        this.password = password;
    }
}
