package ua.com.vladyslav.spribe.data.factories;

import com.github.javafaker.Faker;
import ua.com.vladyslav.spribe.enums.Genders;
import ua.com.vladyslav.spribe.enums.Roles;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataFactory {
    public static Integer getRandomValidAge() {
        return ThreadLocalRandom.current().nextInt(17, 60);
    }

    public static Integer getRandomAgeBelowLimit() {
        return ThreadLocalRandom.current().nextInt(1, 18);
    }

    public static Integer getRandomAgeAboveLimit() {
        return ThreadLocalRandom.current().nextInt(60, 100);
    }

    public static PlayerBuilder createValidRandomUserBuilder() {
        Faker faker = Faker.instance();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return PlayerBuilder.build()
                .login(faker.name().username() + "_" + uniqueId)
                .email(faker.internet().emailAddress())
                .age(getRandomValidAge())
                .gender(Genders.getRandom())
                .role(Roles.USER)
                .screenName(faker.name().fullName() + "_" + uniqueId)
                .password(faker.internet().password(7, 15, true, false, true));
    }
}
