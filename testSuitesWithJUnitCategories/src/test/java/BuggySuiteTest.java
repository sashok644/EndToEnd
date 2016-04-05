

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import categories.Smoke;
import features.TodosE2ETest;
import features.TodosOperationsAtAllFilterTest;

/**
 * Created by 64 on 04.04.2016.
 */

@RunWith(Categories.class)
@Suite.SuiteClasses({TodosOperationsAtAllFilterTest.class, TodosE2ETest.class})
@Categories.ExcludeCategory(Smoke.class)

public class BuggySuiteTest {
}
