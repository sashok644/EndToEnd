package qa.test.pageobjects_and_modules.pagemodules;

import org.junit.Test;

import static com.codeborne.selenide.Selenide.$;
import static qa.test.pageobjects_and_modules.pagemodules.pages.TodoMVC.TaskType.ACTIVE;
import static qa.test.pageobjects_and_modules.pagemodules.pages.TodoMVC.TaskType.COMPLETED;
import static qa.test.pageobjects_and_modules.pagemodules.pages.TodoMVC.*;

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

}
