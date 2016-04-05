package features;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import categories.Smoke;

import static pages.TodoMVC.*;


/**
 * Created by 64 on 04.04.2016.
 */
public class TodosE2ETest extends BaseTest {

    @Test
    @Category(Smoke.class)
    public void testLifeCycle() {

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
}
