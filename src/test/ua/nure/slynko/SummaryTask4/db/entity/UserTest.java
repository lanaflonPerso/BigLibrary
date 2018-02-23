package ua.nure.slynko.SummaryTask4.db.entity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ua.khai.slynko.library.db.entity.User;

public class UserTest {
	private final User user = new User();

	@Before
	public void setUpUserData() {
		user.setEmail("Email");
		user.setFirstName("First name");
		user.setLastName("Last name");
		user.setLocale("ru");
		user.setLogin("Login");
		user.setPassword("Password");
		user.setRoleId(1);
	}

	@Test
	public void testGetLogin() {
		assertEquals(user.getLogin(), "Login");
	}

	@Test
	public void testSetLogin() {
		user.setLogin("Login1");
		assertEquals(user.getLogin(), "Login1");
	}

	@Test
	public void testGetPassword() {
		assertEquals(user.getPassword(), "Password");
	}

	@Test
	public void testSetPassword() {
		user.setPassword("Password1");
		assertEquals(user.getPassword(), "Password1");
	}

	@Test
	public void testGetFirstName() {
		assertEquals(user.getFirstName(), "First name");
	}

	@Test
	public void testSetFirstName() {
		user.setFirstName("First name1");
		assertEquals(user.getFirstName(), "First name1");
	}

	@Test
	public void testGetLastName() {
		assertEquals(user.getLastName(), "Last name");
	}

	@Test
	public void testSetLastName() {
		user.setLastName("Last name1");
		assertEquals(user.getLastName(), "Last name1");
	}

	@Test
	public void testGetEmail() {
		assertEquals(user.getEmail(), "Email");
	}

	@Test
	public void testSetEmail() {
		user.setEmail("Email1");
		assertEquals(user.getEmail(), "Email1");
	}

	@Test
	public void testGetLocale() {
		assertEquals(user.getLocale(), "ru");
	}

	@Test
	public void testSetLocale() {
		user.setLocale("en");
		assertEquals(user.getLocale(), "en");
	}

	@Test
	public void testGetRoleId() {
		assertEquals(user.getRoleId(), 1);
	}

	@Test
	public void testSetRoleId() {
		user.setRoleId(3);
		assertEquals(user.getRoleId(), 3);
	}

	@Test
	public void testToString() {
		assertEquals(user.toString(),
				"User [login=Login, password=Password, firstName=First name, lastName=Last name, email=Email, locale=ru, roleId=1]");
	}

}
