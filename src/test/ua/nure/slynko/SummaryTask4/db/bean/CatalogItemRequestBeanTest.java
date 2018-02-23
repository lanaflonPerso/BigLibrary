package ua.nure.slynko.SummaryTask4.db.bean;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ua.khai.slynko.library.db.bean.CatalogItemRequestBean;

public class CatalogItemRequestBeanTest {
	private final CatalogItemRequestBean bean = new CatalogItemRequestBean();

	@Before
	public void setUpBeanData() {
		bean.setAuthor("Author");
		bean.setTitle("Title");
		bean.setFirstName("First name");
		bean.setLastName("Last name");
	}

	@Test
	public void testGetTitle() {
		assertEquals(bean.getTitle(), "Title");
	}

	@Test
	public void testSetTitle() {
		bean.setTitle("Title1");
		assertEquals(bean.getTitle(), "Title1");
	}

	@Test
	public void testGetAuthor() {
		assertEquals(bean.getAuthor(), "Author");
	}

	@Test
	public void testSetAuthor() {
		bean.setAuthor("Author1");
		assertEquals(bean.getAuthor(), "Author1");
	}

	@Test
	public void testGetFirstName() {
		assertEquals(bean.getFirstName(), "First name");
	}

	@Test
	public void testSetFirstName() {
		bean.setFirstName("First name1");
		assertEquals(bean.getFirstName(), "First name1");
	}

	@Test
	public void testGetLastName() {
		assertEquals(bean.getLastName(), "Last name");
	}

	@Test
	public void testSetLastName() {
		bean.setLastName("Last name1");
		assertEquals(bean.getLastName(), "Last name1");
	}

	@Test
	public void testToString() {
		assertEquals(bean.toString(),
				"CatalogItemRequestBean [title=Title, author=Author, firstName=First name, lastName=Last name]");
	}
}
