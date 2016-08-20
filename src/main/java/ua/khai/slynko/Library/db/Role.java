package ua.khai.slynko.Library.db;

import ua.khai.slynko.Library.db.entity.User;

/**
 * Role entity.
 * 
 * @author O.Slynko
 * 
 */

public enum Role {
	ADMIN(0), READER(1), LIBRARIAN(2);

	public static Role getRole(User user) {
		int roleId = user.getRoleId();
		return Role.values()[roleId];
	}

	public String getName() {
		return name().toLowerCase();
	}

	private final int value;

	private Role(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
