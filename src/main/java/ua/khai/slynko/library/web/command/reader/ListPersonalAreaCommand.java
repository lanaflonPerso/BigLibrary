package ua.khai.slynko.library.web.command.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		HttpSession session = request.getSession();
		String address = Path.PAGE_LIST_CATALOG_ITEMS;
		String findCriteria = request.getParameter("bookStatus");
		if (findCriteria != null) {
			request.setAttribute("bookStatus", findCriteria);
		}

		String[] beanIdsArr = request.getParameterValues("beanId");
		if (beanIdsArr != null) {
			DBManager dbManager = DBManager.getInstance();
			List<String> beanIds = new ArrayList<>(Arrays.asList(beanIdsArr));
			if (findCriteria == null || findCriteria.equals("notConfirmed")) {
				dbManager.removeLibraryCardItemById(beanIds);
				session.setAttribute("requestIsCanceledSuccessfully", true);
				session.setAttribute("redirectPage", Path.COMMAND_LIST_PERSONAL_AREA);
			} else if (findCriteria.equals("libraryCard")) {
				dbManager.updateLibraryCardsItemIds(beanIds, Status.CLOSED.getValue());
				session.setAttribute("redirectPage", Path.COMMAND_LIST_PERSONAL_AREA_LIBRARY_CARD);
				session.setAttribute("bookIsReturnedSuccessfully", true);
			}
			address = Path.PAGE_HOME_REDERECT;
			request.setAttribute("sendRedirect", true);
		} else {
			List<UserCatalogItemBean> catalogItemBeanList = findCatalogItemsBy(((User) session.getAttribute("user")).getId(), findCriteria);
			if (catalogItemBeanList == null || catalogItemBeanList.size() == 0) {
				request.setAttribute("noMatchesFound", true);
			}
			request.setAttribute("catalogItemBeanList", catalogItemBeanList);
		}
		return address;
	}

	private List<UserCatalogItemBean> findCatalogItemsBy(Long userId, String criteria) throws DBException {
		List<UserCatalogItemBean> catalogItemBeanList = new ArrayList<>();
		DBManager dbManager = DBManager.getInstance();
		if (criteria == null || criteria.equals("notConfirmed")) {
			catalogItemBeanList = dbManager.getUserCatalogItemBeansByStatusId(userId, Status.NOT_CONFIRMED.getValue());
		} else if (criteria.equals("libraryCard")) {
			catalogItemBeanList = dbManager.getUserCatalogItemBeansByStatusId(userId, Status.LIBRARY_CARD.getValue());
		}
		return catalogItemBeanList;
	}
}