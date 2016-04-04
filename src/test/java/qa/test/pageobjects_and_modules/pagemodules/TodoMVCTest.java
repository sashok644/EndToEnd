package qa.test.pageobjects_and_modules.pagemodules;

import org.junit.Test;

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
     * ********Active filter*********
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
     * *******Completed filter*******
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

}
