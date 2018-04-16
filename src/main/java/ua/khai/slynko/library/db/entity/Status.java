package ua.khai.slynko.library.db.entity;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Status entity.
 * 
 * @author O.Slynko
 * 
 */
public enum Status {
	READING_ROOM("readingRoom", 0), LIBRARY_CARD("libraryCard", 1), CLOSED("closed", 2)
	, NOT_CONFIRMED("notConfirmed", 3);

	private final String key;
	private final int value;

	Status(String key, int value) {
		this.key = key;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}

	public static Status getByKey(String key) {
		Optional<Status> status = Arrays.stream(values())
				.filter(s -> s.getKey().equals(key))
				.findFirst();
			return status.orElseThrow(NoSuchElementException::new);
	}
}