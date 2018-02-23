package ua.khai.slynko.library.web.command.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.validation.model.BookForm;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Update book command.
 * 
 * @author O.Slynko
 * 
 */
public class UpdateBookCommand extends Command {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		HttpSession session = request.getSession();
		DBManager dbManager = DBManager.getInstance();
		CatalogItem catalogItem = (CatalogItem) session.getAttribute("catalogItem");
		String address;
		String deleteBook = request.getParameter("delete");
		if (deleteBook != null && deleteBook.equals("true")) {
			dbManager.removeCatalogItem(catalogItem.getId());
			request.setAttribute("sendRedirect", true);
			address = Path.PAGE_HOME_REDERECT;
			session.setAttribute("bookDeleteIsSuccessful", true);
		} else if (!isInputDataValid(request)) {
			address = Path.PAGE_MODIFY_BOOK;
		} else {
			dbManager.updateCatalogItem(updateCatalogItem(request, catalogItem));
			request.setAttribute("sendRedirect", true);
			address = Path.PAGE_HOME_REDERECT;
			session.setAttribute("bookUpdateIsSuccessful", true);
		}
		return address;
	}

	private boolean isInputDataValid(HttpServletRequest request) throws IOException, ServletException, AppException {
		return buildAddBookForm(request)
				.validateAndPrefillRequestWithErrors(request);
	}

	private BookForm buildAddBookForm(HttpServletRequest request) {
		return new BookForm(
				request.getParameter("bookTitle"), request.getParameter("edition"),
				request.getParameter("author"), request.getParameter("publicationYear"),
				request.getParameter("instancesNumber"));
	}

	private CatalogItem updateCatalogItem(HttpServletRequest request, CatalogItem catalogItem) {
		catalogItem.setTitle(request.getParameter("bookTitle"));
		catalogItem.setAuthor(request.getParameter("author"));
		catalogItem.setEdition(request.getParameter("edition"));
		catalogItem.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
		catalogItem.setInstancesNumber(Integer.parseInt(request.getParameter("instancesNumber")));
		return catalogItem;
	}
}