package ua.nure.slynko.SummaryTask4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ua.nure.slynko.SummaryTask4.db.FieldsTest;
import ua.nure.slynko.SummaryTask4.db.RoleTest;
import ua.nure.slynko.SummaryTask4.db.StatusTest;
import ua.nure.slynko.SummaryTask4.db.bean.CatalogItemRequestBeanTest;
import ua.nure.slynko.SummaryTask4.db.bean.UserCatalogItemBeanTest;
import ua.nure.slynko.SummaryTask4.db.entity.CatalogItemTest;
import ua.nure.slynko.SummaryTask4.db.entity.LibraryCardItemTest;
import ua.nure.slynko.SummaryTask4.db.entity.LibraryCardTest;
import ua.nure.slynko.SummaryTask4.db.entity.UserTest;
import ua.nure.slynko.SummaryTask4.exception.AppExceptionTest;
import ua.nure.slynko.SummaryTask4.exception.DBExceptionTest;
import ua.nure.slynko.SummaryTask4.security.PasswordTest;

@RunWith(Suite.class)
@SuiteClasses({ CatalogItemRequestBeanTest.class, UserCatalogItemBeanTest.class, CatalogItemTest.class,
		LibraryCardTest.class, LibraryCardItemTest.class, UserTest.class, FieldsTest.class, RoleTest.class,
		StatusTest.class, AppExceptionTest.class, DBExceptionTest.class, PasswordTest.class })
public class AllTests {

}
