package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.time.zone.ZoneRulesProvider.refresh;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanMeeting() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        form.$("[ data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе
        form.$("[ data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(5))
                .shouldBe(Condition.visible);
        refresh();

        form.$$("button.button").last().click();
        $("[data-test-id=replan-notification] .notification__content button").click();
        form.$("[data-test-id = 'date'] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(secondMeetingDate);

        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(5))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should order to undelivered city")
    void shouldOrderToUndeliveredCity() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        form.$("[ data-test-id=city] input").setValue("Нижнекамск");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный " +
                "город недоступна"));
    }

    @Test
    @DisplayName("Should plan delivery for today")
    void shouldPlanDeliveryForToday() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        String Today = DataGenerator.generateDate(0);

        form.$("[ data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(Today);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='date'] [class='input__sub']").shouldHave(exactText("Заказ на выбранную " +
                "дату невозможен"));
    }

    @Test
    @DisplayName("Should not plan delivery in town on latin")
    void shouldPlanDeliveryInTownOnLatin() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        form.$("[ data-test-id=city] input").setValue(DataGenerator.generateCity("en"));
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный " +
                "город недоступна"));
    }

    @Test
    @DisplayName("Should not plan delivery with name on latin")
    void shouldPlanDeliveryWithNameOnLatin() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        form.$("[ data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(DataGenerator.generateName("en"));
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='name'] [class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные" +
                " неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName("Should not plan delivery with empty name")
    void shouldPlanDeliveryWithEmptyName() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        form.$("[ data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(" ");
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='name'] [class='input__sub']").shouldHave(exactText("Поле обязательно " +
                "для заполнения"));
    }

    @Test
    @DisplayName("Should not plan delivery with number for town")
    void shouldPlanDeliveryWithNumberForTown() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        form.$("[ data-test-id=city] input").setValue("79270000000");
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='city'] [class='input__sub']").shouldHave(exactText("Доставка в выбранный " +
                "город недоступна"));
    }

    @Test
    @DisplayName("Should not plan delivery with Empty Phone Number")
    void ShouldNotPlanDeliveryWithPhoneNumberEmpty() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        form.$("[ data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue("");
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='phone'] [class='input__sub']").shouldHave(exactText("Поле обязательно " +
                "для заполнения"));
    }

    @Test
    @DisplayName("Should not plan delivery with name with the symbols")
    void shouldPlanDeliveryWithNameWithTheSymbols() {
        SelenideElement form = $(".form");
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        form.$("[ data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue("Василий+$@ 1Васечкин");
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$$("button.button").last().click();

        $("[data-test-id='name'] [class='input__sub']").shouldHave(exactText("Имя и Фамилия указаные" +
                " неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
}
