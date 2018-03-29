package ua.khai.slynko.library.web.command.outOfControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import ua.khai.slynko.library.Path;
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
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		DBManager dbManager = DBManager.getInstance();
		HttpSession session = request.getSession();
		String author = request.getParameter("author");
		String title = request.getParameter("title");
		List<CatalogItem> catalogItems = DBManager.getInstance().getListCatalogItems(author, title);
		if (catalogItems == null || catalogItems.size() == 0) {
			request.setAttribute("noMatchesFound", true);
		} else {
			String sortCriteria = request.getParameter("sortBy");
			CommandUtils.sortCatalogItemsBy(catalogItems, sortCriteria);
			request.setAttribute("catalogItems", catalogItems);
		}
		String address = Path.PAGE_LIST_CATALOG;
		String[] itemIdsArr = request.getParameterValues("itemId");
		List<String> itemIds = null;
		if (itemIdsArr != null) {
			itemIds = new ArrayList<>(Arrays.asList(itemIdsArr));
		}

		if (itemIds != null) {
			User user = (User) session.getAttribute("user");
			itemIds.removeAll(
					dbManager.findCatalogItemIdsByUserIdAndStatusId(user.getId(), Status.NOT_CONFIRMED.getValue()));
			dbManager.sendCatalogItemRequest(user.getId(), itemIds);
			session.setAttribute("bookRequestIsSent", true);
			request.setAttribute("sendRedirect", true);
			address = Path.PAGE_HOME_REDERECT;
		}
		return address;
	}

}