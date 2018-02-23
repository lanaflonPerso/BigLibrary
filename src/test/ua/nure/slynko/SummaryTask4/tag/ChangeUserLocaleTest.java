package ua.nure.slynko.SummaryTask4.tag;

import javax.servlet.jsp.JspException;

import org.junit.Test;

import ua.khai.slynko.library.tag.ChangeUserLocale;

public class ChangeUserLocaleTest {
	private final ChangeUserLocale cul = new ChangeUserLocale();
	
	@Test
	public void testDoStartTag() throws JspException {
		//PageContext mock = mock(PageContext.class);
		//HttpSession session = mock(HttpSession.class);
	}

	@Test
	public void testSetLocale() {
		cul.setLocale("ru");
	}

}
