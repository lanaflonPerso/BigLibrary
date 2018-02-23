package ua.nure.slynko.SummaryTask4.db.bean;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import ua.khai.slynko.library.db.bean.UserCatalogItemBean;

public class UserCatalogItemBeanTest {
	private final UserCatalogItemBean bean = new UserCatalogItemBean();

	@Before
	public void setUpBeanData() throws ParseException {
		bean.setAuthor("Author");
		bean.setTitle("Title");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "2015-01-25";
		Date dateTo = sdf.parse(dateInString);
		bean.setDateTo(dateTo);
		bean.setPenaltySize(100);
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
	public void testGetDateTo() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "2015-01-25";
		Date dateTo = sdf.parse(dateInString);
		assertEquals(bean.getDateTo(), dateTo);
	}

	@Test
	public void testSetFirstName() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "2015-01-25";
		Date dateTo = sdf.parse(dateInString);
		assertEquals(bean.getDateTo(), dateTo);
	}

	@Test
	public void testGetPenaltySize() {
		assertEquals(bean.getPenaltySize(), 100);
	}

	@Test
	public void testSetPenaltySize() {
		bean.setPenaltySize(170);
		assertEquals(bean.getPenaltySize(), 170);
	}

}
