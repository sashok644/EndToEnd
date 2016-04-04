package qa.test.pageobjects_and_modules.pageobjects;

import org.junit.Test;
import qa.test.pageobjects_and_modules.pageobjects.pages.TodoMVCPage;

import static qa.test.pageobjects_and_modules.pageobjects.pages.TodoMVCPage.TaskType.ACTIVE;
import static qa.test.pageobjects_and_modules.pageobjects.pages.TodoMVCPage.TaskType.COMPLETED;
import static qa.test.pageobjects_and_modules.pageobjects.pages.TodoMVCPage.aTask;


/**
 * Created by 64 on 24.02.2016.
 */
public class TodoMVCTest extends BaseTest {

    TodoMVCPage page = new TodoMVCPage();

    public void testTaskMainFlow() {

        page.givenAtAll();
        page.add("A");
        page.startEdit("A", "A edited").pressEnter();
        // setCompleted
        page.toggle("A edited");
        page.assertTasks("A edited");

        page.filterActive();
        page.assertNoVisibleTasks();

        page.add("B");
        page.assertVisibleTasks("B");
        page.assertItemsLeft(1);
        // completeAll
        page.toggleAll();
        page.assertNoVisibleTasks();

        page.filterCompleted();
        page.assertVisibleTasks("A edited", "B");
        //setActive
        page.toggle("A edited");
        page.assertVisibleTasks("B");
        page.clearCompleted();
        page.assertNoVisibleTasks();

        page.filterAll();
        page.startEdit("A edited", "A").pressEscape();
        page.delete("A edited");
        page.assertNoTasks();
    }

    /*******************************
     * **********All filter***********
     *******************************/

    @Test
    public void testCompleteAllAtAll() {

        page.givenAtAll(ACTIVE, "A", "B", "C", "D");

        page.toggleAll();
        page.assertTasks("A", "B", "C", "D");
        page.assertItemsLeft(0);
    }

    @Test
    public void testReopenAtAll() {

        page.givenAtAll(aTask(COMPLETED, "A"));

        page.toggle("A");
        page.assertVisibleTasks("A");
        page.assertItemsLeft(1);
    }

    @Test
    public void testReopenAllAtAll() {

        page.givenAtAll(COMPLETED, "A", "B", "C", "D");

        page.toggleAll();
        page.assertVisibleTasks("A", "B", "C", "D");
        page.assertItemsLeft(4);
    }

    @Test
    public void testClearCompletedAtAll() {

        page.givenAtAll(aTask(COMPLETED, "A"), aTask(COMPLETED, "B"), aTask(ACTIVE, "C"));


        page.clearCompleted();
        page.assertVisibleTasks("C");
        page.assertItemsLeft(1);

    }

    @Test
    public void testEditByClickOutsideAtAll() {

        page.givenAtAll(aTask(ACTIVE, "A"));

        page.startEdit("A", "A edited");
        page.newTask.click();
        page.assertTasks("A edited");
        page.assertItemsLeft(1);
    }

    @Test
    public void testMoveFromAllToCompleted() {
        page.givenAtAll(aTask(ACTIVE, "A"), aTask(COMPLETED, "B"));

        page.filterCompleted();
        page.assertVisibleTasks("B");
        page.assertItemsLeft(1);
    }

    /******************************
     * ********Active filter*********
     ******************************/

    @Test
    public void testEditAtActive() {

        page.givenAtActive(aTask(ACTIVE, "A"), aTask(ACTIVE, "B"));

        page.startEdit("B", "B edited").pressEnter();
        page.assertTasks("A", "B edited");
        page.assertItemsLeft(2);
    }

    @Test
    public void testDeleteAtActive() {

        page.givenAtActive(aTask(ACTIVE, "A"));

        page.delete("A");
        page.assertNoTasks();
    }

    @Test
    public void testCompleteAtActive() {

        page.givenAtActive(aTask(ACTIVE, "A"));

        page.toggle("A");
        page.assertNoVisibleTasks();
        page.assertItemsLeft(0);
    }

    @Test
    public void testClearCompletedAtActive() {

        page.givenAtActive(aTask(COMPLETED, "A"));

        page.clearCompleted();
        page.assertNoTasks();
    }

    @Test
    public void testCancelEditByEscAtActive() {

        page.givenAtActive(aTask(ACTIVE, "A"));

        page.startEdit("A", "A edited").pressEscape();
        page.assertTasks("A");
        page.assertItemsLeft(1);
    }

    @Test
    public void testEditByPressTabAtActive() {

        page.givenAtActive(aTask(ACTIVE, "A"));

        page.startEdit("A", "A edited").pressTab();
        page.assertVisibleTasks("A edited");
        page.assertItemsLeft(1);
    }

    @Test
    public void testMoveFromActiveToAll() {

        page.givenAtActive(aTask(ACTIVE, "A"), aTask(COMPLETED, "B"));

        page.filterAll();
        page.assertVisibleTasks("A", "B");
        page.assertItemsLeft(1);
    }

    /******************************
     ********Completed filter*******
     ******************************/

    @Test
    public void testEditAtCompleted() {

        page.givenAtCompleted(aTask(COMPLETED, "A"));

        page.startEdit("A", "A edited").pressEnter();
        page.assertTasks("A edited");
        page.assertItemsLeft(0);
    }

    @Test
    public void testDeleteAtCompleted() {

        page.givenAtCompleted(aTask(COMPLETED, "A"), aTask(ACTIVE, "B"));

        page.delete("A");
        page.assertNoVisibleTasks();
        page.assertItemsLeft(1);
    }

    @Test
    public void testReopenAllTaskAtCompleted() {

        page.givenAtCompleted(COMPLETED, "A", "B", "C");

        page.toggleAll();
        page.assertNoVisibleTasks();
        page.assertItemsLeft(3);
    }

    @Test
    public void testCancelEditByEscAtCompleted() {

        page.givenAtCompleted(aTask(ACTIVE, "A"), aTask(COMPLETED, "B"));

        page.startEdit("B", "B edited").pressEscape();
        page.assertVisibleTasks("B");
        page.assertItemsLeft(1);
    }

    @Test
    public void testDeleteByRemovingTextAtCompleted() {

        page.givenAtCompleted(aTask(COMPLETED, "A"));

        page.startEdit("A", "").pressEnter();
        page.assertNoVisibleTasks();
    }

    @Test
    public void testMoveFromCompletedToActive() {

        page.givenAtCompleted(aTask(COMPLETED, "A"), aTask(ACTIVE, "B"));

        page.filterActive();
        page.assertTasks("B");
        page.assertItemsLeft(1);
    }
}



