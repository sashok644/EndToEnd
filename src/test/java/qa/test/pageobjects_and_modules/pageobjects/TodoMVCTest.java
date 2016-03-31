package qa.test.pageobjects_and_modules.pageobjects;

import org.junit.Test;
import qa.test.pageobjects_and_modules.pageobjects.pages.TodoMVCPage;

import static com.codeborne.selenide.Selenide.$;


/**
 * Created by 64 on 24.02.2016.
 */
public class TodoMVCTest extends BaseTest {

    TodoMVCPage page = new TodoMVCPage();

    @Test
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

    @Test
    public void testEditByClickOutsideAtAll() {

        page.givenAtAll(aTask(ACTIVE, "A"));
        page.assertTasks("A");

        page.startEdit("A", "A edited");
        $("#header").click();
        page.assertTasks("A edited");
        page.assertItemsLeft(1);
    }

    @Test
    public void testEditByPressTabAtActive() {

        page.givenAtActive(aTask(ACTIVE, "A"));
        page.assertVisibleTasks("A");

        page.startEdit("A", "A edited").pressTab();
        page.assertVisibleTasks("A edited");
        page.assertItemsLeft(1);
    }

    @Test
    public void testDeleteByRemovingTaskNameAtCompleted() {

        page.givenAtCompleted(aTask(COMPLETED, "A"));
        page.assertVisibleTasks("A");

        page.startEdit("A", "").pressEnter();
        page.assertNoVisibleTasks();
    }

    @Test
    public void testReopenAllTaskAtCompleted() {

        page.givenAtCompleted(COMPLETED, "A", "B", "C");
        page.toggleAll();
        page.filterCompleted();
        page.assertVisibleTasks("A", "B", "C");

        page.toggleAll();
        page.assertNoVisibleTasks();
        page.assertItemsLeft(3);
    }

}
