package ua.khai.slynko.library.db.entity;

/**
 * Locale entity.
 * 
 * @author O.Slynko
 * 
 */
public enum Locale {
	EN(1), RU(2);

	private final int value;

	private Locale(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static boolean contains(String test) {
		for (Locale l : Locale.values()) {
			if (l.name().equals(test.toLowerCase()) || l.name().equals(test.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
}