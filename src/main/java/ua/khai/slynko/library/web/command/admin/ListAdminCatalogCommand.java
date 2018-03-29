package ua.khai.slynko.library.web.command.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.exception.AppException;
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
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		HttpSession session = request.getSession();
		String address = Path.PAGE_LIST_ADMIN_CATALOG;
		String addBook = request.getParameter("addBook");
		String itemId = request.getParameter("itemId");
		if (TRUE.equals(addBook)) {
			address = Path.PAGE_ADD_BOOK;
		} else if (itemId != null) {
			List<CatalogItem> catalogItems = (List<CatalogItem>) session.getAttribute("catalogItems");
			for (CatalogItem catalogItem : catalogItems) {
				if (catalogItem.getId() == Long.parseLong(itemId)) {
					session.setAttribute("catalogItem", catalogItem);
				}
			}
			address = Path.PAGE_MODIFY_BOOK;
		} else {
			String author = request.getParameter("author");
			String title = request.getParameter("title");
			List<CatalogItem> catalogItems = DBManager.getInstance().getListCatalogItems(author, title);
			if (catalogItems == null || catalogItems.size() == 0) {
				request.setAttribute("noMatchesFound", true);
			}
			String sortCriteria = request.getParameter("sortBy");
			CommandUtils.sortCatalogItemsBy(catalogItems, sortCriteria);
			session.setAttribute("catalogItems", catalogItems);
		}
		return address;
	}
}