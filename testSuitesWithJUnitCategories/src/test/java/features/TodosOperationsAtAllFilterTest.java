package features;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import categories.Buggy;

import static pages.TodoMVC.*;
import static pages.TodoMVC.TaskType.*;



/**
 * Created by 64 on 24.02.2016.
 */
public class TodosOperationsAtAllFilterTest extends BaseTest {

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
    @Category(Buggy.class)
    public void testMoveFromAllToCompleted() {
        givenAtAll(aTask(ACTIVE, "A"), aTask(COMPLETED, "B"));

        filterCompleted();
        assertVisibleTasks("B");
        assertItemsLeft(2);
    }

}
