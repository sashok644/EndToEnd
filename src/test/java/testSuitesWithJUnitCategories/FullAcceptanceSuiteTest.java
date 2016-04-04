package testSuitesWithJUnitCategories;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testSuitesWithJUnitCategories.categories.Buggy;
import testSuitesWithJUnitCategories.features.TodosE2ETest;
import testSuitesWithJUnitCategories.features.TodosOperationsAtAllFilterTest;

/**
 * Created by 64 on 04.04.2016.
 */


@RunWith(Categories.class)
@Suite.SuiteClasses({TodosOperationsAtAllFilterTest.class, TodosE2ETest.class})
@Categories.ExcludeCategory(Buggy.class)

public class FullAcceptanceSuiteTest {
}
