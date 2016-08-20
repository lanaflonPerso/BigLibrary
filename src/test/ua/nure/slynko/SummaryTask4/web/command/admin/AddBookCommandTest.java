package ua.nure.slynko.SummaryTask4.web.command.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.khai.slynko.Library.exception.AppException;
import ua.khai.slynko.Library.web.command.admin.AddBookCommand;


public class AddBookCommandTest {
	private final HttpServletRequest request = mock(HttpServletRequest.class);
	private final HttpServletResponse response = mock(HttpServletResponse.class);
	private final AddBookCommand addBookCommand = new AddBookCommand();

	@BeforeClass
	public static void setUpClass() throws Exception {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
		InitialContext ic = new InitialContext();

		ic.createSubcontext("java:");
		ic.createSubcontext("java:/comp");
		ic.createSubcontext("java:/comp/env");
		ic.createSubcontext("java:/comp/env/jdbc");

		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.apache.derby.jdbc.ClientDriver");		
		ds.setUrl("jdbc:derby://localhost:1527/librarydb");
		ds.setUsername("test");
		ds.setPassword("test");

		ic.bind("java:/comp/env/jdbc/LIBRARYDB", ds);
	}

	@Before
	public void setUpData() {
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("bookTitle")).thenReturn("Title");
		when(request.getParameter("author")).thenReturn("Author");
		when(request.getParameter("edition")).thenReturn("Edition");
		when(request.getParameter("publicationYear")).thenReturn("2000");
		when(request.getParameter("instancesNumber")).thenReturn("5");
	}

	@Test
	public void testExecuteSuccess() throws IOException, ServletException, AppException {

		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("bookTitle")).thenReturn("Title");
		when(request.getParameter("author")).thenReturn("Author");
		when(request.getParameter("edition")).thenReturn("Edition");
		when(request.getParameter("publicationYear")).thenReturn("2000");
		when(request.getParameter("instancesNumber")).thenReturn("5");

		assertEquals(addBookCommand.execute(request, response), "home");
	}

	@Test
	public void testIsSameDayDateDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSameDayCalendarCalendar() {
		fail("Not yet implemented");
	}

}
