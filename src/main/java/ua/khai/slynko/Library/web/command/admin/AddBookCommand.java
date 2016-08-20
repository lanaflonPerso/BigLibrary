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
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

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
	private static final Logger LOG = Logger.getLogger(AddBookCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		// obtain session from request
		HttpSession session = request.getSession();
		// init redirect/forward path
		String address = Path.PAGE_ERROR_PAGE;
		LOG.trace("Forward address --> " + address);
		// check if input data is valid
		if (!inputDataIsValid(request)) {
			address = Path.PAGE_ADD_BOOK_FORM;
			LOG.trace("Forward address --> " + address);
		} else {
			// create and initialize new book
			CatalogItem catalogItem = new CatalogItem();
			catalogItem.setTitle(request.getParameter("bookTitle"));
			catalogItem.setAuthor(request.getParameter("author"));
			catalogItem.setEdition(request.getParameter("edition"));
			catalogItem.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
			catalogItem.setInstancesNumber(Integer.parseInt(request.getParameter("instancesNumber")));
			LOG.info("CatalogItem " + catalogItem + " is going to be inserted into database");
			// insert new book into db
			DBManager.getInstance().createCatalogItem(catalogItem);
			LOG.trace("Insertion is successful ");
			// set book add is successful into session
			session.setAttribute("bookAddIsSuccessful", true);
			LOG.trace("bookAddIsSuccessful --> " + true);
			// set send redirect true into request
			request.setAttribute("sendRedirect", true);
			// update redirect/forward path
			address = Path.PAGE_HOME_REDERECT;
			LOG.trace("Redirect address --> " + address);
		}
		LOG.debug("Command finished");
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws IOException, ServletException, AppException {
		LOG.trace("Validation starts");
		boolean isValid = true;
		// obtain fields for validation from request
		String title = request.getParameter("bookTitle");
		LOG.trace("title --> " + title);
		String author = request.getParameter("author");
		LOG.trace("author --> " + author);
		String edition = request.getParameter("edition");
		LOG.trace("edition --> " + edition);
		String publicationYear = request.getParameter("publicationYear");
		LOG.trace("publicationYear --> " + publicationYear);
		String instancesNumber = request.getParameter("instancesNumber");
		LOG.trace("instancesNumber --> " + instancesNumber);

		Pattern numbers = Pattern.compile("^[0-9]+$");

		Calendar calendar = Calendar.getInstance();

		Matcher publicationYearMatcher = numbers.matcher(publicationYear);
		Matcher instancesNumberrMatcher = numbers.matcher(instancesNumber);

		HttpSession session = request.getSession();

		// obtain resource bundle by burrent locale
		String currentLocale = (String) session.getAttribute("currentLocale");
		LOG.debug("Locale --> " + currentLocale);
		ResourceBundle rb;
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}
		// title validation
		if (title == null || title.length() == 0) {
			request.setAttribute("titleMessage", rb.getString("modifyBook.titleIsEmpty"));
			isValid = false;
		} else if (title.length() > 50) {
			request.setAttribute("titleMessage", rb.getString("modifyBook.titleIsTooLong"));
			isValid = false;
		} else {
			request.setAttribute("bookTitle", title);
		}
		// author validation
		if (author == null || author.length() == 0) {
			request.setAttribute("authorMessage", rb.getString("modifyBook.authorIsEmpty"));
			isValid = false;
		} else if (author.length() > 50) {
			request.setAttribute("authorMessage", rb.getString("modifyBook.authorIsTooLong"));
			isValid = false;
		} else {
			request.setAttribute("author", author);
		}
		// edition validation
		if (edition == null || edition.length() == 0) {
			request.setAttribute("editionMessage", rb.getString("modifyBook.editionIsEmpty"));
			isValid = false;
		} else if (edition.length() > 50) {
			request.setAttribute("editionMessage", rb.getString("modifyBook.editionIsTooLong"));
			isValid = false;
		} else {
			request.setAttribute("edition", edition);
		}
		// publication year validation
		if (!publicationYearMatcher.matches()) {
			request.setAttribute("publicationYearMessage", rb.getString("modifyBook.notANumber"));
			isValid = false;
		} else if (Integer.parseInt(publicationYear) > calendar.get(Calendar.YEAR)) {
			request.setAttribute("publicationYearMessage", rb.getString("modifyBook.publicationYearIsNotValid"));
			isValid = false;
		} else {
			request.setAttribute("publicationYear", publicationYear);
		}
		// instances number validation
		if (!instancesNumberrMatcher.matches()) {
			request.setAttribute("instancesNumberMessage", rb.getString("modifyBook.notANumber"));
			isValid = false;
		} else {
			request.setAttribute("instancesNumber", instancesNumber);
		}
		LOG.trace("Validation ends");
		return isValid;
	}
}