package features;

import com.codeborne.selenide.Configuration;

/**
 * Created by 64 on 07.03.2016.
 */
public class BaseTest {

    {
        Configuration.browser = System.getProperty("driver.browser");
    }
}
