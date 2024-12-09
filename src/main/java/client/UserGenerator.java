package client;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.util.Locale;

public class UserGenerator {

    static Faker ruFaker = new Faker(new Locale("ru"));
    static FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());

    private static final String NEW_EMAIL = fakeValuesService.bothify("?????####@yandex.ru");
    private static final String NEW_PASSWORD = fakeValuesService.regexify("[A-z1-9]{7}");
    private static final String NEW_NAME = ruFaker.name().firstName();

    public static User getRandom() {
        User user = new User();
        user.setEmail(NEW_EMAIL);
        user.setPassword(NEW_PASSWORD);
        user.setName(NEW_NAME);
        return user;
    }

}
