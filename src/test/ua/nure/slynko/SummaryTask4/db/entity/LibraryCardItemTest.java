package ua.nure.slynko.SummaryTask4.db.entity;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import ua.khai.slynko.library.db.entity.LibraryCardItem;

public class LibraryCardItemTest {
	private final LibraryCardItem lci = new LibraryCardItem();

	@Before
	public void setUpLCI() throws ParseException {
		lci.setCatalogItemId(3);
		lci.setLibraryCardId(5);
		lci.setPenaltySize(100);
		lci.setStatusId(1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "2015-01-25";
		Date dateTo = sdf.parse(dateInString);
		lci.setDateTo(dateTo);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString1 = "2015-01-21";
		Date dateFrom = sdf1.parse(dateInString1);
		lci.setDateTo(dateFrom);
	}

	@Test
	public void testGetLibraryCardId() {
		assertEquals(lci.getLibraryCardId(), (Integer) 5);
	}

	@Test
	public void testSetLibraryCardId() {
		lci.setLibraryCardId(7);
		assertEquals(lci.getLibraryCardId(), (Integer) 7);
	}

	@Test
	public void testGetCatalogItemId() {
		assertEquals(lci.getCatalogItemId(), (Integer) 3);
	}

	@Test
	public void testSetCatalogItemId() {
		lci.setCatalogItemId(5);
		assertEquals(lci.getCatalogItemId(), (Integer) 5);
	}

	@Test
	public void testGetDateFrom() throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString1 = "2015-01-21";
		Date dateFrom = sdf1.parse(dateInString1);
		lci.setDateFrom(dateFrom);
		assertEquals(lci.getDateFrom(), dateFrom);
	}

	@Test
	public void testSetDateFrom() throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString1 = "2015-01-23";
		Date dateFrom = sdf1.parse(dateInString1);
		lci.setDateFrom(dateFrom);
		assertEquals(lci.getDateFrom(), dateFrom);
	}

	@Test
	public void testGetDateTo() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "2015-01-25";
		Date dateTo = sdf.parse(dateInString);
		lci.setDateTo(dateTo);
		assertEquals(lci.getDateTo(), dateTo);
	}

	@Test
	public void testSetDateTo() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "2015-01-25";
		Date dateTo = sdf.parse(dateInString);
		lci.setDateTo(dateTo);
		assertEquals(lci.getDateTo(), dateTo);
	}

	@Test
	public void testGetPenaltySize() {
		assertEquals(lci.getPenaltySize(), (Integer) 100);
	}

	@Test
	public void testSetPenaltySize() {
		lci.setPenaltySize(101);
		assertEquals(lci.getPenaltySize(), (Integer) 101);
	}

	@Test
	public void testGetStatusId() {
		assertEquals(lci.getStatusId(), (Integer) 1);
	}

	@Test
	public void testSetStatusId() {
		lci.setStatusId(3);
		assertEquals(lci.getStatusId(), (Integer) 3);
	}

}
