package ua.khai.slynko.library.db;

/**
 * Status entity.
 * 
 * @author O.Slynko
 * 
 */
public enum Status {
	READING_ROOM(0), LIBRARY_CARD(1), CLOSED(2), NOT_CONFIRMED(3);

	private final int value;

	private Status(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}