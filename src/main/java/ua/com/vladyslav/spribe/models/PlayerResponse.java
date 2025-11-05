package ua.com.vladyslav.spribe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import ua.com.vladyslav.spribe.enums.Genders;
import ua.com.vladyslav.spribe.enums.Roles;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlayerResponse(
        Integer id,
        String login,
        Integer age,
        Genders gender,
        Roles role,
        String screenName,
        String password
) {

    public PlayerResponse(Integer id, String login, Integer age, Genders gender, Roles role, String screenName, String password) {
        this.id = id;
        this.login = login;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.screenName = screenName;
        this.password = password;
    }
}

