package ua.nure.slynko.SummaryTask4.db;

import static org.junit.Assert.fail;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import ua.khai.slynko.Library.exception.DBException;

public class DBManagerTest {
	private static final Logger LOG = Logger.getLogger(DBManagerTest.class);

	@Before
	public void setUp() throws NamingException {
		// Create initial context
		/*System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
		InitialContext ic = new InitialContext();

		ic.createSubcontext("java:");
		ic.createSubcontext("java:/comp");
		ic.createSubcontext("java:/comp/env");
		ic.createSubcontext("java:/comp/env/jdbc");

		ClientConnectionPoolDataSource ds = new ClientConnectionPoolDataSource();

		ds.setMaxStatements(20);

		ds.setDatabaseName("librarydb");
		ds.setCreateDatabase("create");
		ds.setUser("test");
		ds.setPassword("test");
		ds.setServerName("localhost");
		ds.setPortNumber(1527);

		ic.bind("java:/comp/env/jdbc/librarydb", ds); */
	}

	@Test
	public void testGetInstance() throws DBException, NamingException {
		//DBManager dbManager = DBManager.getInstance();
	}

	@Test
	public void testGetConnection() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUserCatalogItemBeansByStatusId() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindCatalogItemRequests() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindCatalogItemIdsByUserIdAndStatusId() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUsersByRole() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUsersByRoleAndFirstName() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUsersByRoleAndLastName() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUsersByRoleAndFirstNameAndLastName() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindCatalogItems() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindCatalogItemsByAuthor() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindCatalogItemsByTitle() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindCatalogItemsByAuthorAndTitle() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindLibraryCardItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCatalogItemInstancesByLibraryCardId() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUserByLogin() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUserByEmail() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUserByLoginToUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUserByEmailToUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUserPasswordByEmail() {
		fail("Not yet implemented");
	}

	@Test
	public void testBlockUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnblockUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsUserBlocked() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateReader() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateCatalogItemRequestDateFromDateToPenaltySizeById() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateCatalogItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateCatalogItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testSendCatalogItemRequest() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveLibraryCardItemById() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateLibraryCardsItemIds() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveCatalogItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveUserById() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetUserLocale() {
		fail("Not yet implemented");
	}

}
