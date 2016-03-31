package qa.test.pageobjects_and_modules.pageobjects.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static qa.test.pageobjects_and_modules.pageobjects.pages.TodoMVCPage.TaskType.*;

/**
 * Created by 64 on 31.03.2016.
 */
public class TodoMVCPage {

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
    public void assertItemsLeft(Integer count) {
        $("#todo-count>strong").shouldHave(exactText(String.valueOf(count)));
    }

    @Step
    public void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public SelenideElement startEdit(String oldTaskText, String newTaskText) {
        tasks.find(exactText(oldTaskText)).doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").val(newTaskText);
    }

    @Step
    public void filterAll() {
        $(By.linkText("All")).click();
    }

    @Step
    public void filterActive() {
        $(By.linkText("Active")).click();
    }

    @Step
    public void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    @Step
    public void toggle(String taskText) {
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    @Step
    public void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldNotBe(visible);
    }

    @Step
    public void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    @Step
    public void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    @Step
    public void assertTasks(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    @Step
    public void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    @Step
    public void assertVisibleTasks(String... taskTexts) {
        tasks.filter(visible).shouldHave(exactTexts(taskTexts));
    }

    @Step
    public void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }
}
