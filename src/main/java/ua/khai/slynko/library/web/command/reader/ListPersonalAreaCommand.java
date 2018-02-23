package ua.khai.slynko.library.web.command.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Status;
import ua.khai.slynko.library.db.bean.UserCatalogItemBean;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * List personal area.
 * 
 * @author O.Slynko
 * 
 */
public class ListPersonalAreaCommand extends Command {

	private static final long serialVersionUID = 1863978254689586513L;
	private static final Logger LOG = Logger.getLogger(ListPersonalAreaCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Commands starts");
		// obtain session
		HttpSession session = request.getSession();

		// obtain user from session
		User user = (User) session.getAttribute("user");

		// init address
		String address = Path.PAGE_LIST_CATALOG_ITEMS;

		// find catalog items by criteria
		String findCriteria = request.getParameter("bookStatus");
		if (findCriteria != null) {
			request.setAttribute("bookStatus", findCriteria);
		}

		// obtain bean id array from request
		String[] beanIdsArr = request.getParameterValues("beanId");
		
		LOG.trace("Catalog items will be found by --> " + findCriteria);
		// check if bean ids arr is null
		if (beanIdsArr != null) {
			// obtain dbManager instance
			DBManager dbManager = DBManager.getInstance();
			// init bean ids list
			List<String> beanIds = new ArrayList<String>(Arrays.asList(beanIdsArr));
			LOG.trace("Requested beans ids to delete -->" + beanIds);

			// remove requests if find criteria is not conformed
			if (findCriteria == null || findCriteria.equals("notConfirmed")) {
				dbManager.removeLibraryCardItemById(beanIds);
				// set cancelation is successful in session
				session.setAttribute("requestIsCanceledSuccessfully", true);
				LOG.trace("Request has been canceled");
				// set redirect page in session
				session.setAttribute("redirectPage", Path.COMMAND_LIST_PERSONAL_AREA);
			} else if (findCriteria.equals("libraryCard")) {
				dbManager.updateLibraryCardsItemIds(beanIds, Status.CLOSED.getValue());
				LOG.trace("Books have been returned");
				// set redirect page in session
				session.setAttribute("redirectPage", Path.COMMAND_LIST_PERSONAL_AREA_LIBRARY_CARD);
				// set books have been returned seccussfully in session
				session.setAttribute("bookIsReturnedSuccessfully", true);
				LOG.trace("Book has been returned");
			}
			// set redirect page
			address = Path.PAGE_HOME_REDERECT;
			// set send redirect true in session
			request.setAttribute("sendRedirect", true);
		} else {
			// obtain bean items by find criteria
			List<UserCatalogItemBean> catalogItemBeanList = findCatalogItemsBy(user.getId(), findCriteria);
			LOG.trace("Found in DB: catalogItemBeanList --> " + catalogItemBeanList);
			// set no matches found if list is empty
			if (catalogItemBeanList == null || catalogItemBeanList.size() == 0) {
				request.setAttribute("noMatchesFound", true);
				LOG.trace("Set request attribute noMatchesFound --> true");
			}

			// put user order beans list to request
			request.setAttribute("catalogItemBeanList", catalogItemBeanList);
			LOG.trace("Set the request attribute: catalogItemBeanList --> " + catalogItemBeanList);
		}
		LOG.debug("Commands finished");
		return address;
	}

	private List<UserCatalogItemBean> findCatalogItemsBy(Long userId, String criteria) throws DBException {
		List<UserCatalogItemBean> catalogItemBeanList = new ArrayList<UserCatalogItemBean>();
		DBManager dbManager = DBManager.getInstance();
		if (criteria == null || criteria.equals("notConfirmed")) {
			catalogItemBeanList = dbManager.getUserCatalogItemBeansByStatusId(userId, Status.NOT_CONFIRMED.getValue());
		} else if (criteria.equals("libraryCard")) {
			catalogItemBeanList = dbManager.getUserCatalogItemBeansByStatusId(userId, Status.LIBRARY_CARD.getValue());
		}
		return catalogItemBeanList;
	}
}