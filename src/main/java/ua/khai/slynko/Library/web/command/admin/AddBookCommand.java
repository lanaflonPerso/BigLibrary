package ua.khai.slynko.Library.web.command.admin;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.Library.Path;
import ua.khai.slynko.Library.db.DBManager;
import ua.khai.slynko.Library.db.entity.CatalogItem;
import ua.khai.slynko.Library.exception.AppException;
import ua.khai.slynko.Library.web.abstractCommand.Command;

/**
 * Login command.
 * 
 * @author O.Slynko
 * 
 */
public class AddBookCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		if (!inputDataIsValid(request)) {
			return Path.PAGE_ADD_BOOK_FORM;
		} else {
			CatalogItem catalogItem = new CatalogItem();
			catalogItem.setTitle(request.getParameter("bookTitle"));
			catalogItem.setAuthor(request.getParameter("author"));
			catalogItem.setEdition(request.getParameter("edition"));
			catalogItem.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
			catalogItem.setInstancesNumber(Integer.parseInt(request.getParameter("instancesNumber")));
			DBManager.getInstance().createCatalogItem(catalogItem);
			request.getSession().setAttribute("bookAddIsSuccessful", true);
			request.setAttribute("sendRedirect", true);
			return Path.PAGE_HOME_REDERECT;
		}
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws IOException, ServletException, AppException {
		final Pattern PATTERN_NUMBER = Pattern.compile("^[0-9]+$");

		boolean isValid = true;
		String title = request.getParameter("bookTitle");
		String author = request.getParameter("author");
		String edition = request.getParameter("edition");
		String publicationYear = request.getParameter("publicationYear");
		String instancesNumber = request.getParameter("instancesNumber");

		Calendar calendar = Calendar.getInstance();
		Matcher publicationYearMatcher = PATTERN_NUMBER.matcher(publicationYear);
		Matcher instancesNumberMatcher = PATTERN_NUMBER.matcher(instancesNumber);

		String currentLocale = (String) request.getSession().getAttribute("currentLocale");
		ResourceBundle rb;
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}
		if (title == null || title.length() == 0) {
			request.setAttribute("titleMessage", rb.getString("modifyBook.titleIsEmpty"));
			isValid = false;
		} else if (title.length() > 50) {
			request.setAttribute("titleMessage", rb.getString("modifyBook.titleIsTooLong"));
			isValid = false;
		} else {
			request.setAttribute("bookTitle", title);
		}
		if (author == null || author.length() == 0) {
			request.setAttribute("authorMessage", rb.getString("modifyBook.authorIsEmpty"));
			isValid = false;
		} else if (author.length() > 50) {
			request.setAttribute("authorMessage", rb.getString("modifyBook.authorIsTooLong"));
			isValid = false;
		} else {
			request.setAttribute("author", author);
		}
		if (edition == null || edition.length() == 0) {
			request.setAttribute("editionMessage", rb.getString("modifyBook.editionIsEmpty"));
			isValid = false;
		} else if (edition.length() > 50) {
			request.setAttribute("editionMessage", rb.getString("modifyBook.editionIsTooLong"));
			isValid = false;
		} else {
			request.setAttribute("edition", edition);
		}
		if (!publicationYearMatcher.matches()) {
			request.setAttribute("publicationYearMessage", rb.getString("modifyBook.notANumber"));
			isValid = false;
		} else if (Integer.parseInt(publicationYear) > calendar.get(Calendar.YEAR)) {
			request.setAttribute("publicationYearMessage", rb.getString("modifyBook.publicationYearIsNotValid"));
			isValid = false;
		} else {
			request.setAttribute("publicationYear", publicationYear);
		}
		if (!instancesNumberMatcher.matches()) {
			request.setAttribute("instancesNumberMessage", rb.getString("modifyBook.notANumber"));
			isValid = false;
		} else {
			request.setAttribute("instancesNumber", instancesNumber);
		}
		return isValid;
	}
}