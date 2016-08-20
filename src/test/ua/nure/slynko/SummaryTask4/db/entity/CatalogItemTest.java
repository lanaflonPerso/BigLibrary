package ua.nure.slynko.SummaryTask4.db.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ua.khai.slynko.Library.db.entity.CatalogItem;

public class CatalogItemTest {
	private final CatalogItem catalogItem = new CatalogItem();

	@Before
	public void setUpEntityData() {
		catalogItem.setAuthor("Author");
		catalogItem.setTitle("Title");
		catalogItem.setEdition("Edition");
		catalogItem.setInstancesNumber(7);
		catalogItem.setPublicationYear(2016);
		catalogItem.setId((long) 5);
	}

	@Test
	public void testGetTitle() {
		assertEquals(catalogItem.getTitle(), "Title");
	}

	@Test
	public void testSetTitle() {
		catalogItem.setTitle("Title1");
		assertEquals(catalogItem.getTitle(), "Title1");
	}

	@Test
	public void testGetAuthor() {
		assertEquals(catalogItem.getAuthor(), "Author");
	}

	@Test
	public void testSetAuthor() {
		catalogItem.setAuthor("Author1");
		assertEquals(catalogItem.getAuthor(), "Author1");
	}

	@Test
	public void testGetEdition() {
		assertEquals(catalogItem.getEdition(), "Edition");
	}

	@Test
	public void testSetEdition() {
		catalogItem.setEdition("Edition1");
		assertEquals(catalogItem.getEdition(), "Edition1");
	}

	@Test
	public void testGetPublicationYear() {
		assertEquals(catalogItem.getPublicationYear(), (Integer) 2016);
	}

	@Test
	public void testSetPublicationYear() {
		catalogItem.setPublicationYear(2015);
		assertEquals(catalogItem.getPublicationYear(), (Integer) 2015);
	}

	@Test
	public void testGetInstancesNumber() {
		assertEquals(catalogItem.getInstancesNumber(), (Integer) 7);
	}

	@Test
	public void testSetInstancesNumber() {
		catalogItem.setInstancesNumber(9);
		assertEquals(catalogItem.getInstancesNumber(), (Integer) 9);
	}

	@Test
	public void testToString() {
		System.out.println(catalogItem);
		assertEquals(catalogItem.toString(),
				"CatalogItem [id= 5, title=Title, author=Author, edition=Edition, publicationYear=2016, instancesNumber=7]");
	}

}
