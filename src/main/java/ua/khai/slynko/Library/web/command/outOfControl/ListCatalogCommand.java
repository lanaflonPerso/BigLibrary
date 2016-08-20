package ua.khai.slynko.Library.web.command.outOfControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import ua.khai.slynko.Library.db.Status;
import ua.khai.slynko.Library.db.entity.CatalogItem;
import ua.khai.slynko.Library.db.entity.User;
import ua.khai.slynko.Library.exception.AppException;
import ua.khai.slynko.Library.exception.DBException;
import ua.khai.slynko.Library.web.abstractCommand.Command;

/**
 * Lists catalog items.
 * 
 * @author O.Slynko
 * 
 */
public class ListCatalogCommand extends Command {

	private static final long serialVersionUID = 7732286214029478505L;

	private static final Logger LOG = Logger.getLogger(ListCatalogCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");

		// obtain dbManager and session
		DBManager dbManager = DBManager.getInstance();
		HttpSession session = request.getSession();

		// obtain catalog items list by author and title
		String author = request.getParameter("author");
		LOG.trace("Get request parameter author --> " + author);
		String title = request.getParameter("title");
		LOG.trace("Get request parameter title --> " + title);
		List<CatalogItem> catalogItems = getListCatalogItems(author, title);
		LOG.trace("Found in DB: catalogItemsList --> " + catalogItems);

		// check if no matches found
		if (catalogItems == null || catalogItems.size() == 0) {
			request.setAttribute("noMatchesFound", true);
			LOG.trace("Set request attribute noMatchesFound --> true");
		} else {
			// sort catalog by criteria
			String sortCriteria = request.getParameter("sortBy");
			sortCatalogItemsBy(catalogItems, sortCriteria);
			LOG.trace("Catalog items sorted by --> " + sortCriteria);

			// put catalog items list to the request
			request.setAttribute("catalogItems", catalogItems);
			LOG.trace("Set the request attribute: catalogItems --> " + catalogItems);
		}

		// init address
		String address = Path.PAGE_LIST_CATALOG;

		// obtain item ids to send request
		String[] itemIdsArr = request.getParameterValues("itemId");

		// init list of ids
		List<String> itemIds = null;
		if (itemIdsArr != null) {
			itemIds = new ArrayList<String>(Arrays.asList(itemIdsArr));
		}

		// send requests
		if (itemIds != null) {
			User user = (User) session.getAttribute("user");
			LOG.trace("Requested Items before remove already requested -->" + itemIds);

			// remove already requested items
			itemIds.removeAll(
					dbManager.findCatalogItemIdsByUserIdAndStatusId(user.getId(), Status.NOT_CONFIRMED.getValue()));
			LOG.trace("Requested Items  after remove already requested -->" + itemIds);

			// send requests
			dbManager.sendCatalogItemRequest(user.getId(), itemIds);

			// set request is sent into session
			session.setAttribute("bookRequestIsSent", true);
			LOG.trace("Set session attribute bookRequestIsSent --> true");

			// set send redirect true
			request.setAttribute("sendRedirect", true);
			LOG.trace("Set request attribute sendRedirect --> true");

			// set redirect path
			address = Path.PAGE_HOME_REDERECT;
			LOG.trace("Forward set to --> " + address);
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