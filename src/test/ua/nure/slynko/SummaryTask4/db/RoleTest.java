package ua.nure.slynko.SummaryTask4.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;

public class RoleTest {
	private final Role role = Role.ADMIN;
	private final User user = new User();
	
	@Before
	public void setUpData() {
		user.setRoleId(0);
	}
	@Test
	public void testGetRole() {
		assertEquals(Role.getRole(user), Role.ADMIN);
	}

	@Test
	public void testGetName() {
		assertEquals(role.getName(), "admin");
	}

	@Test
	public void testGetValue() {
		assertEquals(role.getValue(), 0);
	}

	@Test
	public void testValueof() {
		assertEquals(Role.valueOf("ADMIN"), Role.ADMIN);
	}
}
