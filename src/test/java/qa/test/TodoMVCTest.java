package qa.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static qa.test.TodoMVCTest.TaskType.ACTIVE;


/**
 * Created by 64 on 24.02.2016.
 */
public class TodoMVCTest extends BaseTest {


    @Test
    public void testTaskMainFlow() {
        //Configuration.timeout = 10000;


        open(ALL);
        given(aTask(ACTIVE, "A"), aTask(ACTIVE, "B"));
        startEdit("A", "A edited").pressEnter();
        // setCompleted
        toggle("A edited");
        assertTasks("A edited", "B");

        open(ACTIVEFILTER);

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
        open(ALL);
        given(aTask(ACTIVE, "A"));

        startEdit("A", "A edited");
        $("#header").click();
        assertTasks("A edited");
        assertItemsLeft(1);
    }

    @Test
    public void testEditByPressTabAtActive() {

        //given - task on active filter
        open(COMPLETEDFILTER);
        givenAllCompleted("A");

        startEdit("A", "A edited").pressTab();
        assertVisibleTasks("A edited");
        assertItemsLeft(0);
    }

    @Test
    public void testDeleteByRemovingTaskNameAtCompleted() {

        //given - task on completed filter
        open(COMPLETEDFILTER);
        givenAllCompleted("A");
        assertVisibleTasks("A");

        startEdit("A", "").pressEnter();
        assertNoVisibleTasks();
    }

    @Test
    public void testReopenAllTaskAtCompleted() {

        //given - tasks on completed filter
        open(COMPLETEDFILTER);
        givenAllCompleted("A", "B", "C");
        assertVisibleTasks("A", "B", "C");

        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeft(3);
    }


    ElementsCollection tasks = $$("#todo-list>li");

    String startJS = "localStorage.setItem(\"todos-troopjs\", \"[";
    String activeState = "{\\\"completed\\\":false, \\\"title\\\":\\\"";
    String completedState = "{\\\"completed\\\":true, \\\"title\\\":\\\"";
    String endJScript = "\\\"},";
    String endJS = "]\")";

    public enum TaskType {
        ACTIVE, COMPLETED

      /*  ACTIVE("false"), COMPLETED("true")


        private String flag;

        TaskType(String flag) {
            this.flag = flag;
        }

        public String getFlag(){
            return flag;
        }*/
    }


    final String ALL = "http://todomvc4tasj.herokuapp.com/#/";
    final String ACTIVEFILTER = "http://todomvc4tasj.herokuapp.com/#/active";
    final String COMPLETEDFILTER = "http://todomvc4tasj.herokuapp.com/#/completed";


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


    public void given(Task... tasks) {

        String JScript = "";
        for (Task task : tasks) {
            if (task.taskType == ACTIVE) {
                JScript += activeState + task.taskText + endJScript;
            } else
                JScript += completedState + task.taskText + endJScript;
        }
        JScript = JScript.substring(0, JScript.length() - 1);

        String result = startJS + JScript + endJS;

        executeJavaScript(result);
        executeJavaScript("location.reload()");

    }

    private void givenAllActive(String... taskTexts) {

        String JScript = "";
        for (String taskName : taskTexts) {
            JScript = JScript + activeState + taskName + endJScript;
        }

        JScript = JScript.substring(0, JScript.length() - 1);

        String result = startJS + JScript + endJS;

        executeJavaScript(result);
        executeJavaScript("location.reload()");
    }

    private void givenAllCompleted(String... taskTexts) {


        String JScript = "";
        for (String taskName : taskTexts) {
            JScript = JScript + completedState + taskName + endJScript;
        }

        JScript = JScript.substring(0, JScript.length() - 1);

        String result = startJS + JScript + endJS;

        executeJavaScript(result);
        executeJavaScript("location.reload()");
    }

    private void given2(boolean state, String... taskTexts) {

        String JScript = "";
        if (state == true) {
            for (String taskName : taskTexts) {
                JScript = JScript + completedState + taskName + endJScript;
            }
        } else if (state == false) {
            for (String taskName : taskTexts) {
                JScript = JScript + activeState + taskName + endJScript;
            }
        }

        JScript = JScript.substring(0, JScript.length() - 1);

        String result = startJS + JScript + endJS;

        executeJavaScript(result);
        executeJavaScript("location.reload()");
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
