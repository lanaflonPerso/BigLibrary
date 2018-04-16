package ua.khai.slynko.library.web.command.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.ConnectionManager;
import ua.khai.slynko.library.db.dao.CatalogItemDao;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.abstractCommand.Command;
import ua.khai.slynko.library.web.command.utils.CommandUtils;

import static ua.khai.slynko.library.constant.Constants.TRUE;

/**
 * Lists admin catalog items.
 * 
 * @author O.Slynko
 * 
 */
public class ListAdminCatalogCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
		if (isAddBookCommand(request)) {
			return Constants.Path.PAGE_ADD_BOOK;
		} else if (isModifyBookCommand(request)) {
			putSelectedBookIntoSession(request);
			return Constants.Path.PAGE_MODIFY_BOOK;
		} else {
			findBooksAndSort(request);
			return Constants.Path.PAGE_LIST_ADMIN_CATALOG;
		}
	}

	private boolean isAddBookCommand(HttpServletRequest request) {
		return TRUE.equals(request.getParameter("addBook"));
	}

	private boolean isModifyBookCommand(HttpServletRequest request) {
		return request.getParameter("itemId") != null;
	}

	private void putSelectedBookIntoSession(HttpServletRequest request) {
		((List<CatalogItem>) request.getSession().getAttribute("catalogItems")).stream()
				.filter(catalogItem -> catalogItem.getId() == Long.parseLong(request.getParameter("itemId")))
				.findFirst()
				.ifPresent(catalogItem -> request.getSession().setAttribute("catalogItem", catalogItem));
	}

	private void findBooksAndSort(HttpServletRequest request) throws DBException {
		List<CatalogItem> catalogItems = new CatalogItemDao().getListCatalogItems(
				request.getParameter("author"),
				request.getParameter("title"));
		if (catalogItems == null || catalogItems.size() == 0) {
			request.setAttribute("noMatchesFound", true);
		}
		CommandUtils.sortCatalogItemsBy(catalogItems, request.getParameter("sortBy"));
		request.getSession().setAttribute("catalogItems", catalogItems);
	}
}