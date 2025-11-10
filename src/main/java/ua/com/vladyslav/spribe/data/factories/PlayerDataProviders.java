package ua.com.vladyslav.spribe.data.factories;

import org.testng.annotations.DataProvider;

public class PlayerDataProviders {
    // --- AGE DATA PROVIDERS ---

    @DataProvider(name = "validBoundaryAges", parallel = true)
    public static Object[][] validAgesForCreation() {
        return new Object[][]{
                {17},
                {59}
        };
    }

    @DataProvider(name = "invalidBoundaryAges", parallel = true)
    public static Object[][] invalidAgesForCreation() {
        return new Object[][]{
                {16},
                {60}
        };
    }

    // --- PASSWORD DATA PROVIDERS ---

    @DataProvider(name = "invalidPasswords", parallel = true)
    public static Object[][] invalidPasswords() {
        return new Object[][]{
                {"a1b2c3", "Too short (<7)"},
                {"abcdefgh", "No digits"},
                {"12345678", "No letters"},
                {"Abcdefghijklmnop123", "Too long (>15)"}
        };
    }

    @DataProvider(name = "validPasswords", parallel = true)
    public static Object[][] validPasswords() {
        return new Object[][]{
                {"Abcdef1", "Min boundary valid (7 chars)"},
                {"Abcdefghijklm12", "Max boundary valid (15 chars)"},
                {"a1B2c3D4", "Mixed valid"}
        };
    }

    // --- REQUIRED FIELDS DATA PROVIDERS ---

    @DataProvider(name = "requiredFields", parallel = true)
    public Object[][] requiredFields() {
        return new Object[][]{
                {"login"},
                {"password"},
                {"role"},
                {"screenName"},
                {"age"},
                {"gender"}
        };
    }
}
