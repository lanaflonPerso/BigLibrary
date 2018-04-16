package ua.khai.slynko.library.web.command.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.ConnectionManager;
import ua.khai.slynko.library.db.dao.CatalogItemDao;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
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
	public String execute(HttpServletRequest request, HttpServletResponse response)	throws AppException {
		if (isDeleteBookCommand(request)) {
			deleteBook(request);
			return Constants.Path.PAGE_HOME_REDERECT;
		} else if (!isInputDataValid(request)) {
			return Constants.Path.PAGE_MODIFY_BOOK;
		} else {
			updateBook(request);
			return Constants.Path.PAGE_HOME_REDERECT;
		}
	}

	private boolean isDeleteBookCommand(HttpServletRequest request) {
		return request.getParameter("delete") != null && request.getParameter("delete").equals("true");
	}

	private void deleteBook(HttpServletRequest request) throws DBException	{
		new CatalogItemDao().removeCatalogItem(
				((CatalogItem) request.getSession().getAttribute("catalogItem")).getId());
		request.setAttribute("sendRedirect", true);
		request.getSession().setAttribute("bookDeleteIsSuccessful", true);
	}

	private void updateBook(HttpServletRequest request) throws DBException	{
		new CatalogItemDao().updateCatalogItem(updateCatalogItem(request,
				(CatalogItem) request.getSession().getAttribute("catalogItem")));
		request.setAttribute("sendRedirect", true);
		request.getSession().setAttribute("bookUpdateIsSuccessful", true);
	}

	private boolean isInputDataValid(HttpServletRequest request) {
		return buildAddBookForm(request)
				.validateAndPrefillRequestWithErrors(request);
	}

	private BookForm buildAddBookForm(HttpServletRequest request) {
		return new BookForm(
				request.getParameter("title"), request.getParameter("edition"),
				request.getParameter("author"), request.getParameter("publicationYear"),
				request.getParameter("instancesNumber"));
	}

	private CatalogItem updateCatalogItem(HttpServletRequest request, CatalogItem catalogItem) {
		catalogItem.setTitle(request.getParameter("title"));
		catalogItem.setAuthor(request.getParameter("author"));
		catalogItem.setEdition(request.getParameter("edition"));
		catalogItem.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
		catalogItem.setInstancesNumber(Integer.parseInt(request.getParameter("instancesNumber")));
		return catalogItem;
	}
}