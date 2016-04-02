package qa.test.pageobjects_and_modules;

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
import static qa.test.pageobjects_and_modules.TodoMVCTest.TaskType.ACTIVE;
import static qa.test.pageobjects_and_modules.TodoMVCTest.TaskType.COMPLETED;

/**
 * Created by 64 on 24.02.2016.
 */
public class TodoMVCTest extends BaseTest {

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
    public void testCompleteAllAtAll() {
        givenAtAll(ACTIVE, "A", "B", "C", "D");

        toggleAll();
        assertTasks("A", "B", "C", "D");
        assertItemsLeft(0);
    }

    @Test
    public void testReopenAtAll() {

        givenAtAll(aTask(COMPLETED, "A"));

        toggle("A");
        assertVisibleTasks("A");
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAllAtAll() {

        givenAtAll(COMPLETED, "A", "B", "C", "D");

        toggleAll();
        assertVisibleTasks("A", "B", "C", "D");
        assertItemsLeft(4);
    }

    @Test
    public void testClearCompletedAtAll() {

        givenAtAll(COMPLETED, "A", "B", "C", "D");
        givenAtAll(aTask(ACTIVE, "E"));


        clearCompleted();
        assertVisibleTasks("E");
        assertItemsLeft(1);

    }

    @Test
    public void testEditByClickOutsideAtAll() {

        givenAtAll(aTask(ACTIVE, "A"));

        startEdit("A", "A edited");
        $("#new-todo").click();
        assertTasks("A edited");
        assertItemsLeft(1);
    }

    @Test
    public void testMoveFromAllToActive() {
        givenAtAll(aTask(ACTIVE, "A"), aTask(COMPLETED, "B"));

        filterActive();
        assertVisibleTasks("A");
        assertItemsLeft(1);
    }

    @Test
    public void testEditAtActive() {

        givenAtActive(aTask(ACTIVE, "A"), aTask(ACTIVE, "B"));

        startEdit("B", "B edited").pressEnter();
        assertTasks("A", "B edited");
        assertItemsLeft(2);
    }

    @Test
    public void testDeleteAtActive() {

        givenAtActive(aTask(ACTIVE, "A"));

        delete("A");
        assertNoTasks();
    }

    @Test
    public void testCompleteAtActive() {

        givenAtActive(aTask(ACTIVE, "A"));

        toggle("A");
        assertNoVisibleTasks();
        assertItemsLeft(0);
    }

    @Test
    public void testClearCompletedAtActive() {

        givenAtActive(aTask(COMPLETED, "A"));

        assertItemsLeft(0);
        clearCompleted();
    }

    @Test
    public void testCancelEditByEscAtActive() {

        givenAtActive(aTask(ACTIVE, "A"));

        startEdit("A", "A edited").pressEscape();
        assertTasks("A");
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
    public void testMoveFromActiveToCompleted() {

        givenAtActive(aTask(ACTIVE, "A"), aTask(COMPLETED, "B"));

        filterCompleted();
        assertVisibleTasks("B");
        assertItemsLeft(1);
    }

    @Test
    public void testEditAtCompleted() {

        givenAtCompleted(aTask(COMPLETED, "A"));

        startEdit("A", "A edited").pressEnter();
        assertTasks("A edited");
        assertItemsLeft(0);
    }

    @Test
    public void testDeleteAtCompleted() {

        givenAtCompleted(aTask(COMPLETED, "A"), aTask(ACTIVE, "B"));

        assertItemsLeft(1);
        delete("A");
        assertNoVisibleTasks();
    }

    @Test
    public void testReopenAllTaskAtCompleted() {

        givenAtCompleted(COMPLETED, "A", "B", "C");

        assertVisibleTasks("A", "B", "C");
        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeft(3);
    }

    @Test
    public void testCancelEditByEscAtCompleted() {

        givenAtCompleted(aTask(ACTIVE, "A"), aTask(COMPLETED, "B"));

        startEdit("B", "B edited").pressEscape();
        assertVisibleTasks("B");
        assertItemsLeft(1);
    }

    @Test
    public void testDeleteByRemovingTextAtCompleted() {

        givenAtCompleted(aTask(COMPLETED, "A"));

        assertVisibleTasks("A");
        startEdit("A", "").pressEnter();
        assertNoVisibleTasks();
    }

    @Test
    public void testMoveFromCompletedToAll() {
        givenAtCompleted(aTask(COMPLETED, "A"), aTask(ACTIVE, "B"));

        filterAll();
        assertTasks("A", "B");
        assertItemsLeft(1);
    }


    private ElementsCollection tasks = $$("#todo-list>li");

    public enum TaskType {
        ACTIVE, COMPLETED
    }

    public enum Filter {

        ALL(""),
        ACTIVE("active"),
        COMPLETED("completed");

        private String url;

        Filter(String url) {
            this.url = url;
        }

        public String getURL() {
            return "http://todomvc4tasj.herokuapp.com/#/" + url;
        }

    }

    private class Task {
        TaskType taskType;
        String taskText;


        public Task(TaskType taskType, String taskText) {
            this.taskType = taskType;
            this.taskText = taskText;
        }
    }

    private Task aTask(TaskType taskType, String taskText) {
        Task task = new Task(taskType, taskText);
        return task;
    }

    private void getTaskArray(Filter filter, Task... tasks) {

        if (!url().equals(filter.getURL())) {
            open(filter.getURL());
        }

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

    private Task[] getTaskArray(TaskType taskType, String... taskTexts) {

        Task[] tasks = new Task[taskTexts.length];
        for (int i = 0; i < taskTexts.length; i++) {
            tasks[i] = aTask(taskType, taskTexts[i]);
        }
        return (tasks);
    }

    private void givenAtAll(Task... tasks) {
        getTaskArray(Filter.ALL, tasks);
    }

    private void givenAtActive(Task... tasks) {
        getTaskArray(Filter.ACTIVE, tasks);
    }

    private void givenAtCompleted(Task... tasks) {
        getTaskArray(Filter.COMPLETED, tasks);
    }

    private void givenAtAll(TaskType taskType, String... taskTexts) {
        getTaskArray(Filter.ALL, getTaskArray(taskType, taskTexts));

    }

    private void givenAtActive(TaskType taskType, String... taskTexts) {
        getTaskArray(Filter.ACTIVE, getTaskArray(taskType, taskTexts));

    }

    private void givenAtCompleted(TaskType taskType, String... taskTexts) {
        getTaskArray(Filter.COMPLETED, getTaskArray(taskType, taskTexts));

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


