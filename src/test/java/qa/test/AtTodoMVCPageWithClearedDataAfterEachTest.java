package qa.test;

import static com.codeborne.selenide.Selenide.executeJavaScript;

/**
 * Created by 64 on 07.03.2016.
 */
public class AtTodoMVCPageWithClearedDataAfterEachTest extends BaseTest {

//    @Before

    // @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");
    }
}
