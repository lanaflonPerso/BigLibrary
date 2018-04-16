package ua.khai.slynko.library.web.command.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
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
	public String execute(HttpServletRequest request, HttpServletResponse response)	throws AppException {
		if (isLibraryCardSelected(request)) {
			removeOrUpdateLibraryCard(request);
			return Constants.Path.PAGE_HOME_REDERECT;
		} else {
			findCatalogItems(request);
			return Constants.Path.PAGE_LIST_CATALOG_ITEMS;
		}
	}

	private boolean isLibraryCardSelected(HttpServletRequest request) {
		return request.getParameterValues("beanId") != null;
	}

	private void removeOrUpdateLibraryCard(HttpServletRequest request) throws DBException	{
		String findCriteria = request.getParameter("bookStatus");
		if (findCriteria != null) {
			request.setAttribute("bookStatus", findCriteria);
		}
		List<String> beanIds = new ArrayList<>(Arrays.asList(request.getParameterValues("beanId")));
		if ("notConfirmed".equals(findCriteria)) {
			DBManager.getInstance().removeLibraryCardItemById(beanIds);
			request.getSession().setAttribute("requestIsCanceledSuccessfully", true);
			request.getSession().setAttribute("redirectPage", Constants.Path.COMMAND_LIST_PERSONAL_AREA);
		} else if ("libraryCard".equals(findCriteria)) {
			DBManager.getInstance().updateLibraryCardsItemIds(beanIds, Status.CLOSED.getValue());
			request.getSession().setAttribute("redirectPage", Constants.Path.COMMAND_LIST_PERSONAL_AREA_LIBRARY_CARD);
			request.getSession().setAttribute("bookIsReturnedSuccessfully", true);
		}
		request.setAttribute("sendRedirect", true);
	}

	private void findCatalogItems(HttpServletRequest request) throws DBException {
		String findCriteria = request.getParameter("bookStatus");
		if (findCriteria != null) {
			request.setAttribute("bookStatus", findCriteria);
		}
		List<UserCatalogItemBean> catalogItemBeanList =	DBManager.getInstance().findCatalogItemsBy(
				((User) request.getSession().getAttribute("user")).getId(), request.getParameter("bookStatus"));
		if (catalogItemBeanList == null || catalogItemBeanList.size() == 0) {
			request.setAttribute("noMatchesFound", true);
		}
		request.setAttribute("catalogItemBeanList", catalogItemBeanList);
	}
}