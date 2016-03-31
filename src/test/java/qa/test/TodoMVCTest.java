package qa.test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static qa.test.TodoMVCTest.TaskType.ACTIVE;
import static qa.test.TodoMVCTest.TaskType.COMPLETED;

/**
 * Created by 64 on 24.02.2016.
 */
public class TodoMVCTest extends BaseTest {


    @Test
    public void testTaskMainFlow() {

        givenAtAll();
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

        givenAtAll(aTask(ACTIVE, "A"));
        assertTasks("A");

        startEdit("A", "A edited");
        $("#header").click();
        assertTasks("A edited");
        assertItemsLeft(1);
    }

    @Test
    public void testEditByPressTabAtActive() {

        givenAtActive(aTask(ACTIVE, "A"));
        assertVisibleTasks("A");

        startEdit("A", "A edited").pressTab();
        assertVisibleTasks("A edited");
        assertItemsLeft(1);
    }

    @Test
    public void testDeleteByRemovingTaskNameAtCompleted() {

        givenAtCompleted(aTask(COMPLETED, "A"));
        assertVisibleTasks("A");

        startEdit("A", "").pressEnter();
        assertNoVisibleTasks();
    }

    @Test
    public void testReopenAllTaskAtCompleted() {

        givenAtCompleted(COMPLETED, "A", "B", "C");
        toggleAll();
        filterCompleted();
        assertVisibleTasks("A", "B", "C");

        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeft(3);
    }

    ElementsCollection tasks = $$("#todo-list>li");


    public enum TaskType {
        ACTIVE, COMPLETED
    }

    public enum Filter {

        ALL("http://todomvc4tasj.herokuapp.com/#/"),
        ACTIVE("http://todomvc4tasj.herokuapp.com/#/active"),
        COMPLETED("http://todomvc4tasj.herokuapp.com/#/completed");

        private String url;

        Filter(String url) {
            this.url = url;
        }

        public String getURL() {
            return url;
        }

    }


    public class Task {
        TaskType taskType;
        String taskText;


        public Task(TaskType taskType, String taskText) {
            this.taskType = taskType;
            this.taskText = taskText;
        }
    }

    public Task aTask(TaskType taskType, String taskText) {
        Task task = new Task(taskType, taskText);
        return task;
    }

    public void ensurePageOpened(Filter filter) {
        if (!url().equals(filter.getURL())) {
            open(filter.getURL());
        }
    }

    public void given(Task... tasks) {
        String result = "";

        for (Task task : tasks) {
            result = result + "{\\\"completed\\\":" + (task.taskType == ACTIVE ? "false" : "true") +
                    ", \\\"title\\\":\\\"" + task.taskText + "\\\"},";

        }
        if (tasks.length > 0) {
            result = result.substring(0, result.length() - 1);
        }

        String JS = "localStorage.setItem(\"todos-troopjs\", \"[" + result + "]\")";

        executeJavaScript(JS);
        executeJavaScript("location.reload()");

    }

    public void given(TaskType taskType, String... taskText) {

        String result = "";

        for (int i = 0; i < taskText.length; i++) {
            result = result + "{\\\"completed\\\":" + (taskType == ACTIVE ? "false" : "true") + ", \\\"title\\\":\\\"" + taskText[i] + "\\\"},";
        }
        if (taskText.length > 0) {
            result = result.substring(0, result.length() - 1);
        }
        String JS = "localStorage.setItem(\"todos-troopjs\", \"[" + result + "]\")";

        executeJavaScript(JS);
        executeJavaScript("location.reload()");
    }

    public void givenAtAll(Task... tasks) {
        ensurePageOpened(Filter.ALL);
        given(tasks);
    }

    public void givenAtActive(Task... tasks) {
        ensurePageOpened(Filter.ACTIVE);
        given(tasks);
    }

    public void givenAtCompleted(Task... tasks) {
        ensurePageOpened(Filter.COMPLETED);
        given(tasks);
    }


    public void givenAtAll(TaskType taskType, String... taskTexts) {
        ensurePageOpened(Filter.ALL);
        given(taskType, taskTexts);

    }

    public void givenAtActive(TaskType taskType, String... taskTexts) {
        ensurePageOpened(Filter.ACTIVE);
        given(taskType, taskTexts);

    }

    public void givenAtCompleted(TaskType taskType, String... taskTexts) {
        ensurePageOpened(Filter.COMPLETED);
        given(taskType, taskTexts);

    }

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
