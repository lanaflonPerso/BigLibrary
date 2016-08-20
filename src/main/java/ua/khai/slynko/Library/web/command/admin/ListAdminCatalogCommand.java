package ua.khai.slynko.Library.web.command.admin;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.Library.Path;
import ua.khai.slynko.Library.db.DBManager;
import ua.khai.slynko.Library.db.entity.CatalogItem;
import ua.khai.slynko.Library.exception.AppException;
import ua.khai.slynko.Library.exception.DBException;
import ua.khai.slynko.Library.web.abstractCommand.Command;

/**
 * Lists admin catalog items.
 * 
 * @author O.Slynko
 * 
 */
public class ListAdminCatalogCommand extends Command {

	private static final long serialVersionUID = 7732286214029478505L;
	private static final Logger LOG = Logger.getLogger(ListAdminCatalogCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		// obtain session
		HttpSession session = request.getSession();
		// init address
		String address = Path.PAGE_LIST_ADMIN_CATALOG;
		// obtain request params
		String addBook = request.getParameter("addBook");
		String itemId = request.getParameter("itemId");
		// check if action is add book
		if (addBook != null && addBook.equals("true")) {
			LOG.trace("Requested item id array --> " + itemId);
			address = Path.PAGE_ADD_BOOK_FORM;
			LOG.trace("Forward set to --> " + address);
		} else if (itemId != null) {
			// obtain catalog items list from session
			List<CatalogItem> catalogItems = (List<CatalogItem>) session.getAttribute("catalogItems");
			LOG.trace("Found in session catalogItems --> " + catalogItems);
			// set requested item into session
			for (CatalogItem catalogItem : catalogItems) {
				if (catalogItem.getId() == Long.parseLong(itemId)) {
					session.setAttribute("catalogItem", catalogItem);
				}
			}
			// update address
			address = Path.PAGE_MODIFY_BOOK_FORM;
			LOG.trace("Forward set to --> " + address);
		} else {
			// get catalog items list
			String author = request.getParameter("author");
			LOG.trace("Get request parameter author --> " + author);
			String title = request.getParameter("title");
			LOG.trace("Get request parameter title --> " + title);
			List<CatalogItem> catalogItems = getListCatalogItems(author, title);
			LOG.trace("Found in DB: catalogItemsList --> " + catalogItems);
			// set no mathches into request if item list is empty
			if (catalogItems == null || catalogItems.size() == 0) {
				request.setAttribute("noMatchesFound", true);
				LOG.trace("Set request attribute noMatchesFound --> true");
			}

			// sort catalog by criteria
			String sortCriteria = request.getParameter("sortBy");
			sortCatalogItemsBy(catalogItems, sortCriteria);
			LOG.trace("Catalog items sorted by --> " + sortCriteria);

			// put catalog items list to the request
			session.setAttribute("catalogItems", catalogItems);
			LOG.trace("Set the session attribute: catalogItems --> " + catalogItems);
		}

		LOG.debug("Command finished");
		return address;
	}

	private void sortCatalogItemsBy(List<CatalogItem> catalogItems, String criteria) {

		if (criteria == null || criteria.equals("title")) {
			Collections.sort(catalogItems, new Comparator<CatalogItem>() {
				public int compare(CatalogItem o1, CatalogItem o2) {
					return o1.getTitle().compareTo(o2.getTitle());
				}
			});
		} else if (criteria.equals("author")) {
			Collections.sort(catalogItems, new Comparator<CatalogItem>() {
				public int compare(CatalogItem o1, CatalogItem o2) {
					return o1.getAuthor().compareTo(o2.getAuthor());
				}
			});
		} else if (criteria.equals("edition")) {
			Collections.sort(catalogItems, new Comparator<CatalogItem>() {
				public int compare(CatalogItem o1, CatalogItem o2) {
					return o1.getEdition().compareTo(o2.getEdition());
				}
			});
		} else if (criteria.equals("publicationYear")) {
			Collections.sort(catalogItems, new Comparator<CatalogItem>() {
				public int compare(CatalogItem o1, CatalogItem o2) {
					return o1.getPublicationYear() - o2.getPublicationYear();
				}
			});
		}
	}

	private List<CatalogItem> getListCatalogItems(String author, String title) throws DBException {
		DBManager dbManager = DBManager.getInstance();
		List<CatalogItem> catalogItems = null;

		if ((author == null || author.equals("")) && (title == null || title.equals(""))) {
			catalogItems = dbManager.findCatalogItems();
		} else if ((author == null || author.equals("")) && title != null && title.length() > 0) {
			catalogItems = dbManager.findCatalogItemsByTitle(title);
		} else if (author != null && author.length() > 0 && (title == null || title.equals(""))) {
			catalogItems = dbManager.findCatalogItemsByAuthor(author);
		} else if (author != null && author.length() > 0 && title != null && title.length() > 0) {
			catalogItems = dbManager.findCatalogItemsByAuthorAndTitle(author, title);
		}
		return catalogItems;
	}
}