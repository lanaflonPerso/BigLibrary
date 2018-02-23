package ua.nure.slynko.SummaryTask4.db.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ua.khai.slynko.library.db.entity.LibraryCard;

public class LibraryCardTest {
	private final LibraryCard libraryCard = new LibraryCard();
	
	@Before 
	public void setUpEntityData() {
		libraryCard.setUserId((long) 3);
	}
	@Test
	public void testGetUserId() {
		assertEquals(libraryCard.getUserId(), new Long(3));
	}

	@Test
	public void testSetUserId() {
		libraryCard.setUserId((long) 7); 
		assertEquals(libraryCard.getUserId(), new Long(7));
	}

	@Test
	public void testToString() {
		assertEquals(libraryCard.toString(), "Subscription [userId=3]");
	}

}
