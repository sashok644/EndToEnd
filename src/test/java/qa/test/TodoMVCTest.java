package qa.test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.SelenideElement;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by 64 on 24.02.2016.
 */
public class TodoMVCTest {

    @After
    public void tearDown() throws IOException {
        screenshot();
    }

    @Attachment(type = "image/png")
    public byte[] screenshot() throws IOException {
        File screenshot = Screenshots.getLastScreenshot();
        return Files.toByteArray(screenshot);
    }

    @Before
    public void openPage() {
        open("https://todomvc4tasj.herokuapp.com/");
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");
    }

    @Test
    public void testTaskMainFlow() {

        add("A");
        startEdit("A", "A edited").pressEnter();
        // setCompleted
        toggle("A edited");
        assertTasks("A edited");

        filterActive();
        assertNoVisibleTasks();

        add("B");
        assertVisibleTasks("B");
        assertItemsLeft(1);
        // completeAll
        toggleAll();
        assertNoVisibleTasks();

        filterCompleted();
        assertVisibleTasks("A edited", "B");
        //setActive
        toggle("A edited");
        assertVisibleTasks("B");
        clearCompleted();
        assertNoVisibleTasks();

        filterAll();
        startEdit("A edited", "A").pressEscape();
        delete("A edited");
        assertNoTasks();

    }

    @Test
    public void testEditByClickOutsideAtAll() {

        //given - task on all filter
        add("A");
        assertTasks("A");

        startEdit("A", "A edited");
        $("#header").click();
        assertTasks("A edited");
        assertItemsLeft(1);
    }

    @Test
    public void testEditByPressTabAtActive() {

        //given - task on active filter
        add("A");
        filterActive();
        assertVisibleTasks("A");

        startEdit("A", "A edited").pressTab();
        assertVisibleTasks("A edited");
        assertItemsLeft(1);
    }

    @Test
    public void testDeleteByRemovingTaskNameAtCompleted() {

        //given - task on completed filter
        add("A");
        toggle("A");
        filterCompleted();
        assertVisibleTasks("A");

        startEdit("A", "").pressEnter();
        assertNoVisibleTasks();
    }

    @Test
    public void testReopenAllTaskAtCompleted() {

        //given - task on completed filter
        add("A", "B", "C");
        assertTasks("A", "B", "C");
        toggleAll();
        filterCompleted();
        assertVisibleTasks("A", "B", "C");

        // reopenAll
        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeft(3);
    }

    ElementsCollection tasks = $$("#todo-list>li");

    @Step
    private void assertItemsLeft(Integer count) {
        $("#todo-count>strong").shouldHave(exactText(String.valueOf(count)));
    }

    @Step
    private void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    private SelenideElement startEdit(String oldTaskText, String newTaskText) {
        tasks.find(exactText(oldTaskText)).doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").val(newTaskText);
    }

    @Step
    private void filterAll() {
        $(By.linkText("All")).click();
    }

    @Step
    private void filterActive() {
        $(By.linkText("Active")).click();
    }

    @Step
    private void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    @Step
    private void toggle(String taskText) {
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    @Step
    private void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldNotBe(visible);
    }

    @Step
    private void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    @Step
    private void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    @Step
    private void assertTasks(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    @Step
    private void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    @Step
    private void assertVisibleTasks(String... taskTexts) {
        tasks.filter(visible).shouldHave(exactTexts(taskTexts));
    }

    @Step
    private void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

}




