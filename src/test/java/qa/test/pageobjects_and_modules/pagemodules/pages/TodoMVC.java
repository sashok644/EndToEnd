package qa.test.pageobjects_and_modules.pagemodules.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static qa.test.pageobjects_and_modules.pagemodules.pages.TodoMVC.TaskType.*;

/**
 * Created by 64 on 31.03.2016.
 */
public class TodoMVC {
    public static ElementsCollection tasks = $$("#todo-list>li");

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


    public static class Task {
        TaskType taskType;
        String taskText;


        public Task(TaskType taskType, String taskText) {
            this.taskType = taskType;
            this.taskText = taskText;
        }
    }

    public static Task aTask(TaskType taskType, String taskText) {
        Task task = new Task(taskType, taskText);
        return task;
    }

    public static void ensurePageOpened(Filter filter) {
        if (!url().equals(filter.getURL())) {
            open(filter.getURL());
        }
    }

    public static void given(Task... tasks) {
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

    public static void given(TaskType taskType, String... taskText) {

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

    public static void givenAtAll(Task... tasks) {
        ensurePageOpened(Filter.ALL);
        given(tasks);
    }

    public static void givenAtActive(Task... tasks) {
        ensurePageOpened(Filter.ACTIVE);
        given(tasks);
    }

    public static void givenAtCompleted(Task... tasks) {
        ensurePageOpened(Filter.COMPLETED);
        given(tasks);
    }


    public static void givenAtAll(TaskType taskType, String... taskTexts) {
        ensurePageOpened(Filter.ALL);
        given(taskType, taskTexts);

    }

    public static void givenAtActive(TaskType taskType, String... taskTexts) {
        ensurePageOpened(Filter.ACTIVE);
        given(taskType, taskTexts);

    }

    public static void givenAtCompleted(TaskType taskType, String... taskTexts) {
        ensurePageOpened(Filter.COMPLETED);
        given(taskType, taskTexts);

    }

    @Step
    public static void assertItemsLeft(Integer count) {
        $("#todo-count>strong").shouldHave(exactText(String.valueOf(count)));
    }

    @Step
    public static void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public static SelenideElement startEdit(String oldTaskText, String newTaskText) {
        tasks.find(exactText(oldTaskText)).doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").val(newTaskText);
    }

    @Step
    public static void filterAll() {
        $(By.linkText("All")).click();
    }

    @Step
    public static void filterActive() {
        $(By.linkText("Active")).click();
    }

    @Step
    public static void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    @Step
    public static void toggle(String taskText) {
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    @Step
    public static void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldNotBe(visible);
    }

    @Step
    public static void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    @Step
    public static void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    @Step
    public static void assertTasks(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    @Step
    public static void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    @Step
    public static void assertVisibleTasks(String... taskTexts) {
        tasks.filter(visible).shouldHave(exactTexts(taskTexts));
    }

    @Step
    public static void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }
}


