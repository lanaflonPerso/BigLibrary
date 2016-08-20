package ua.nure.slynko.SummaryTask4.exception;

import org.junit.Test;

import ua.khai.slynko.Library.exception.DBException;

public class DBExceptionTest {

	@Test(expected = DBException.class)
	public void testDBException() throws DBException {
		throw new DBException();
	}

	@Test(expected = DBException.class)
	public void testDBExceptionString() throws DBException {
		throw new DBException("Message");
	}

	@Test(expected = DBException.class)
	public void testDBExceptionStringThrowable() throws DBException {
		throw new DBException("Message", new Throwable());
	}

	@Test(expected = DBException.class)
	public void testFire() throws Exception {
		DBException.fire("Message");
	}

}
