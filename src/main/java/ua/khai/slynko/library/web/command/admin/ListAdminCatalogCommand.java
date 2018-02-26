package ua.khai.slynko.library.web.command.admin;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.abstractCommand.Command;

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
		if (addBook != null && addBook.equals("true")) {
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
			List<CatalogItem> catalogItems = getListCatalogItems(author, title);
			if (catalogItems == null || catalogItems.size() == 0) {
				request.setAttribute("noMatchesFound", true);
			}
			String sortCriteria = request.getParameter("sortBy");
			sortCatalogItemsBy(catalogItems, sortCriteria);
			session.setAttribute("catalogItems", catalogItems);
		}
		return address;
	}

	private void sortCatalogItemsBy(List<CatalogItem> catalogItems, String criteria) {
		if (criteria == null || criteria.equals("title")) {
			catalogItems.sort(Comparator.comparing(CatalogItem::getTitle));
		} else if (criteria.equals("author")) {
			catalogItems.sort(Comparator.comparing(CatalogItem::getAuthor));
		} else if (criteria.equals("edition")) {
			catalogItems.sort(Comparator.comparing(CatalogItem::getEdition));
		} else if (criteria.equals("publicationYear")) {
			catalogItems.sort(Comparator.comparing(CatalogItem::getPublicationYear));
		}
	}

	private List<CatalogItem> getListCatalogItems(String author, String title) throws DBException {
		DBManager dbManager = DBManager.getInstance();
		List<CatalogItem> catalogItems = null;
		if (StringUtils.isEmpty(author) && StringUtils.isEmpty(title)) {
			catalogItems = dbManager.findCatalogItems();
		} else if (StringUtils.isEmpty(author) && !StringUtils.isEmpty(title)) {
			catalogItems = dbManager.findCatalogItemsByTitle(title);
		} else if (!StringUtils.isEmpty(author) && (StringUtils.isEmpty(title))) {
			catalogItems = dbManager.findCatalogItemsByAuthor(author);
		} else if (!StringUtils.isEmpty(author) && !StringUtils.isEmpty(title)) {
			catalogItems = dbManager.findCatalogItemsByAuthorAndTitle(author, title);
		}
		return catalogItems;
	}
}