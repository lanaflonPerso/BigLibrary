package ua.khai.slynko.library.web.command.outOfControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Status;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.abstractCommand.Command;
import ua.khai.slynko.library.web.command.utils.CommandUtils;

/**
 * Lists catalog items.
 * 
 * @author O.Slynko
 * 
 */
public class ListCatalogCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
		if (isSendBookRequestCommand(request)) {
			sendBookRequest(request);
			return Constants.Path.PAGE_HOME_REDERECT;
		} else {
			findBooksAndSort(request);
			return Constants.Path.PAGE_LIST_CATALOG;
		}
	}

	private boolean isSendBookRequestCommand(HttpServletRequest request) {
		return request.getParameterValues("itemId") != null;
	}

	private void sendBookRequest(HttpServletRequest request) throws DBException {
		List<String> itemIds = new ArrayList<>(Arrays.asList(request.getParameterValues("itemId")));
		User user = (User) request.getSession().getAttribute("user");
		itemIds.removeAll(DBManager.getInstance().findCatalogItemIdsByUserIdAndStatusId(
				user.getId(), Status.NOT_CONFIRMED.getValue()));
		DBManager.getInstance().sendCatalogItemRequest(user.getId(), itemIds);
		request.getSession().setAttribute("bookRequestIsSent", true);
		request.setAttribute("sendRedirect", true);
	}

	private void findBooksAndSort(HttpServletRequest request) throws DBException {
		List<CatalogItem> catalogItems = DBManager.getInstance().getListCatalogItems(
				request.getParameter("author"), request.getParameter("title"));
		if (catalogItems == null || catalogItems.size() == 0)	{
			request.setAttribute("noMatchesFound", true);
		}	else {
			String sortCriteria = request.getParameter("sortBy");
			CommandUtils.sortCatalogItemsBy(catalogItems, sortCriteria);
			request.setAttribute("catalogItems", catalogItems);
		}
	}
}