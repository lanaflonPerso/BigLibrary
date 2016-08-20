package ua.khai.slynko.Library.web.command.librarian;

import java.io.IOException;
import java.io.Serializable;
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
import ua.khai.slynko.Library.db.bean.CatalogItemRequestBean;
import ua.khai.slynko.Library.exception.AppException;
import ua.khai.slynko.Library.web.abstractCommand.Command;

/**
 * Lists reader's requests.
 * 
 * @author O.Slynko
 * 
 */
public class ListReadersRequestsCommand extends Command {

	private static final long serialVersionUID = 1863978254689586513L;
	private static final Logger LOG = Logger.getLogger(ListReadersRequestsCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Commands starts");
		// obtain session
		HttpSession session = request.getSession();
		// init address
		String address = Path.PAGE_LIST_READERS_REQUESTS;
		// obtain bean id fro request
		String beanId = request.getParameter("beanId");
		LOG.trace("Requested item id --> " + beanId);
		// check if bean id is null
		if (beanId != null) {
			CatalogItemRequestBean catalogItemRequestBean = null;
			// obtain catalog requests list from session
			List<CatalogItemRequestBean> catalogItemRequestsList = (List<CatalogItemRequestBean>) session
					.getAttribute("catalogItemRequestsList");
			LOG.trace("Found in session catalogItemRequestsList --> " + catalogItemRequestsList);
			// find request in requests list by id
			for (CatalogItemRequestBean cirbListItem : catalogItemRequestsList) {
				if (cirbListItem.getId() == Long.parseLong(beanId)) {
					catalogItemRequestBean = cirbListItem;
				}
			}

			// obtain request bean (user is on the same page)
			if (catalogItemRequestBean == null) {
				catalogItemRequestBean = (CatalogItemRequestBean) session.getAttribute("catalogItemRequestBean");
			}
			LOG.trace("Found in session catalogItemRequestBean --> " + catalogItemRequestBean);
			// set found request bean into session
			session.setAttribute("catalogItemRequestBean", catalogItemRequestBean);
			address = Path.PAGE_CONFIRM_REQUEST_FORM;
		} else {
			// obtain requests list from db
			List<CatalogItemRequestBean> catalogItemRequestsList = DBManager.getInstance()
					.findCatalogItemRequests(Status.NOT_CONFIRMED.getValue());
			LOG.trace("Found in DB: catalogItemRequestsList --> " + catalogItemRequestsList);

			// set no matches found if list is empty
			if (catalogItemRequestsList == null || catalogItemRequestsList.size() == 0) {
				request.setAttribute("noMatchesFound", true);
				LOG.trace("Set request attribute noMatchesFound --> true");
			} else {
				// sort list by title
				Collections.sort(catalogItemRequestsList, compareByTitle);
				// put user order beans list to request
				session.setAttribute("catalogItemRequestsList", catalogItemRequestsList);
				LOG.trace("Set the request attribute: catalogItemRequestsList --> " + catalogItemRequestsList);
			}
		}
		LOG.debug("Commands finished");
		return address;
	}

	/**
	 * Serializable comparator used with TreeMap container. When the servlet
	 * container tries to serialize the session it may fail because the session
	 * can contain TreeMap object with not serializable comparator.
	 * 
	 * @author O.Slynko
	 * 
	 */
	private static class CompareByTitle implements Comparator<CatalogItemRequestBean>, Serializable {
		private static final long serialVersionUID = -1573481565177573283L;

		public int compare(CatalogItemRequestBean bean1, CatalogItemRequestBean bean2) {
			return bean1.getTitle().compareTo(bean2.getTitle());
		}
	}

	private static Comparator<CatalogItemRequestBean> compareByTitle = new CompareByTitle();

}