package pages;

import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by 64 on 11.04.2016.
 */
public class MailsPage {
    ElementsCollection listMails = $$("[role = 'main'] .zA");

    public void send(String to, String subject) {
        $(byText("COMPOSE")).click();
        $(By.name("to")).setValue(to).pressEnter();
        $(By.name("subjectbox")).setValue(subject).pressEnter();
        $(byText("Send")).click();
    }

    public void assertMail(int index, String subject) {
        listMails.get(index).shouldHave(text(subject));
    }

    public void assertMails(String... subjects) {
        listMails.shouldHave(texts(subjects));
    }


    public void searchBySubject(String subject) {
        $(By.name("q")).setValue("subject: " + subject).pressEnter();
    }
}
