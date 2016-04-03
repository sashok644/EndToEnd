package full_coverage;

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
import static full_coverage.TodoMVCTest.TaskType.ACTIVE;
import static full_coverage.TodoMVCTest.TaskType.COMPLETED;

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

    /*******************************
     * **********All filter***********
     *******************************/

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

        givenAtAll(aTask(COMPLETED, "A"), aTask(COMPLETED, "B"), aTask(ACTIVE, "C"));


        clearCompleted();
        assertVisibleTasks("C");
        assertItemsLeft(1);

    }

    @Test
    public void testEditByClickOutsideAtAll() {

        givenAtAll(aTask(ACTIVE, "A"));

        startEdit("A", "A edited");
        newTask.click();
        assertTasks("A edited");
        assertItemsLeft(1);
    }

    @Test
    public void testMoveFromAllToCompleted() {
        givenAtAll(aTask(ACTIVE, "A"), aTask(COMPLETED, "B"));

        filterCompleted();
        assertVisibleTasks("B");
        assertItemsLeft(1);
    }

    /******************************
     *********Active filter*********
     ******************************/

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

        clearCompleted();
        assertNoTasks();
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
    public void testMoveFromActiveToAll() {

        givenAtActive(aTask(ACTIVE, "A"), aTask(COMPLETED, "B"));

        filterAll();
        assertVisibleTasks("A", "B");
        assertItemsLeft(1);
    }

    /******************************
     ********Completed filter*******
     ******************************/

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

        delete("A");
        assertNoVisibleTasks();
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAllTaskAtCompleted() {

        givenAtCompleted(COMPLETED, "A", "B", "C");

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

        startEdit("A", "").pressEnter();
        assertNoVisibleTasks();
    }

    @Test
    public void testMoveFromCompletedToActive() {
        givenAtCompleted(aTask(COMPLETED, "A"), aTask(ACTIVE, "B"));

        filterActive();
        assertTasks("B");
        assertItemsLeft(1);
    }

    /*****************************
     ************Steps*************
     *****************************/

    private ElementsCollection tasks = $$("#todo-list>li");
    private SelenideElement newTask = $("#new-todo");

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
            newTask.setValue(text).pressEnter();
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

    /*****************************
     ************Given*************
     *****************************/

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

    private void given(Filter filter, Task... tasks) {

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
        given(Filter.ALL, tasks);
    }

    private void givenAtActive(Task... tasks) {
        given(Filter.ACTIVE, tasks);
    }

    private void givenAtCompleted(Task... tasks) {
        given(Filter.COMPLETED, tasks);
    }

    private void givenAtAll(TaskType taskType, String... taskTexts) {
        given(Filter.ALL, getTaskArray(taskType, taskTexts));

    }

    private void givenAtActive(TaskType taskType, String... taskTexts) {
        given(Filter.ACTIVE, getTaskArray(taskType, taskTexts));

    }

    private void givenAtCompleted(TaskType taskType, String... taskTexts) {
        given(Filter.COMPLETED, getTaskArray(taskType, taskTexts));

    }
}


