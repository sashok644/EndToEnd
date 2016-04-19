package google;

import com.codeborne.selenide.Configuration;
import org.junit.Test;
import pages.GmailPage;
import pages.MailsPage;
import pages.NavigationPage;
import config.TestData;

import java.util.Random;

/**
 * Created by 64 on 06.04.2016.
 */
public class GmailSendAndSearchTest {

    static {
        Configuration.timeout = 15000;
        Configuration.holdBrowserOpen = true;
    }

    GmailPage gmail = new GmailPage();
    MailsPage mails = new MailsPage();
    NavigationPage navigation = new NavigationPage();
    TestData test = new TestData();

    @Test
    public void testGmailSendAndSearch() {

        gmail.open();
        gmail.login(test.email, test.password);             // fill in required login and password for email in the ...config/Testada.java
        mails.send(test.email, subject);
        navigation.refresh();
        mails.assertMail(0, subject);


        navigation.openSent();
        mails.assertMail(0, subject);

        navigation.openInbox();
        mails.searchBySubject(subject);
        mails.assertMails(subject);
    }

    public static String subject = "subject" + (new Random()).nextInt();
}

