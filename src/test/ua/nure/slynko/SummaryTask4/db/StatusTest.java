package ua.nure.slynko.SummaryTask4.db;

import static org.junit.Assert.*;

import org.junit.Test;

import ua.khai.slynko.library.db.Status;

public class StatusTest {
	private final Status status = Status.LIBRARY_CARD;
	
	@Test
	public void testGetValue() {
		assertEquals(status.getValue(), 1);
	}
	
	@Test
	public void testValueof() {
		assertEquals(Status.valueOf("LIBRARY_CARD"), Status.LIBRARY_CARD);
	}
}
