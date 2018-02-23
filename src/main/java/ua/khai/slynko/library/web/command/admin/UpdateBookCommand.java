package ua.khai.slynko.library.web.command.admin;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Update book command.
 * 
 * @author O.Slynko
 * 
 */
public class UpdateBookCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(UpdateBookCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		// obtain session from request
		HttpSession session = request.getSession();
		// obtain db Manager instance
		DBManager dbManager = DBManager.getInstance();
		// obtain catalog item from session
		CatalogItem catalogItem = (CatalogItem) session.getAttribute("catalogItem");
		// init address
		String address = Path.PAGE_ERROR_PAGE;
		LOG.trace("Forward address --> " + address);
		// obtain action
		String deleteBook = request.getParameter("delete");
		LOG.trace("Delete book? --> " + deleteBook);
		// check if action is delete book
		if (deleteBook != null && deleteBook.equals("true")) {
			// remove item from catalog
			dbManager.removeCatalogItem(catalogItem.getId());
			LOG.trace("Id to delete --> " + catalogItem.getId());
			// set send redirect to session
			request.setAttribute("sendRedirect", true);
			// update redirect page
			address = Path.PAGE_HOME_REDERECT;
			LOG.trace("Redirect address --> " + address);
			// set book is deleted successfully into session
			session.setAttribute("bookDeleteIsSuccessful", true);
			LOG.trace("bookDeleteIsSuccessful --> true ");
		} else if (!inputDataIsValid(request)) {
			address = Path.PAGE_MODIFY_BOOK_FORM;
			LOG.trace("Forward address --> " + address);
		} else {
			// update catalog item fields
			catalogItem.setAuthor(request.getParameter("author"));
			catalogItem.setTitle(request.getParameter("title"));
			catalogItem.setEdition(request.getParameter("edition"));
			catalogItem.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
			catalogItem.setInstancesNumber(Integer.parseInt(request.getParameter("instancesNumber")));
			LOG.info("CatalogItem " + catalogItem + " is going to be inserted into database");
			// update item in db
			dbManager.updateCatalogItem(catalogItem);
			LOG.trace("Update is successful ");
			// set send redirect arrtibute into session
			request.setAttribute("sendRedirect", true);
			address = Path.PAGE_HOME_REDERECT;
			LOG.trace("Redirect address --> " + address);
			// set book update is successful attr into session
			session.setAttribute("bookUpdateIsSuccessful", true);
			LOG.trace("bookUpdateIsSuccessful --> true ");
		}
		LOG.debug("Command finished");
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request)
			throws IOException, ServletException, AppException {
		boolean isValid = true;
		// obtain fields for validation
		String title = request.getParameter("title");
		LOG.trace("title --> " + title);
		String author = request.getParameter("author");
		LOG.trace("author --> " + author);
		String edition = request.getParameter("edition");
		LOG.trace("edition --> " + edition);
		String publicationYear = request.getParameter("publicationYear");
		LOG.trace("publicationYear --> " + publicationYear);
		String instancesNumber = request.getParameter("instancesNumber");
		LOG.trace("instancesNumber --> " + instancesNumber);

		Calendar calendar = Calendar.getInstance();
		
		Matcher publicationYearMatcher = PATTERN_NUMBER.matcher(publicationYear);
		Matcher instancesNumberrMatcher = PATTERN_NUMBER.matcher(instancesNumber);
		
		// obtain resource bundle
		String currentLocale = (String) request.getSession().getAttribute("currentLocale");
		LOG.debug("Locale --> " + currentLocale);
		ResourceBundle rb;
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}
		
		LOG.trace("Validation starts");
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
		if (!StringUtils.isNumeric(publicationYear)) {
			request.setAttribute("publicationYearMessage", rb.getString("modifyBook.notANumber"));
			isValid = false;
		} else if(Integer.parseInt(publicationYear) > calendar.get(Calendar.YEAR)) {
			request.setAttribute("publicationYearMessage", rb.getString("modifyBook.publicationYearIsNotValid"));
			isValid = false;
		} else {
			request.setAttribute("publicationYear", publicationYear);
		}
		// instances number validation
		if (!StringUtils.isNumeric(instancesNumber)) {
			request.setAttribute("instancesNumberMessage", rb.getString("modifyBook.notANumber"));
			isValid = false;
		} else {
			request.setAttribute("instancesNumber", instancesNumber);
		}
		LOG.trace("Validation ends");
		return isValid;
	}
}