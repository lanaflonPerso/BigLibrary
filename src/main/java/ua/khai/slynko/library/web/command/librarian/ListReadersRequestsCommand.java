package ua.khai.slynko.library.web.command.librarian;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Status;
import ua.khai.slynko.library.db.bean.CatalogItemRequestBean;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Lists reader's requests.
 * 
 * @author O.Slynko
 * 
 */
public class ListReadersRequestsCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		if (isOpenRequestCommand(request)) {
			findCatalogItemRequestBean(request);
			return Path.PAGE_CONFIRM_REQUEST_FORM;
		} else {
			findCatalogItemRequestsAndSort(request);
			return Path.PAGE_LIST_READERS_REQUESTS;
		}
	}

	private boolean isOpenRequestCommand(HttpServletRequest request) {
		return request.getParameter("beanId") != null;
	}

	private void findCatalogItemRequestsAndSort(HttpServletRequest request) throws DBException {
		List<CatalogItemRequestBean> catalogItemRequestsList = DBManager.getInstance()
				.findCatalogItemRequests(Status.NOT_CONFIRMED.getValue());
		if (catalogItemRequestsList == null || catalogItemRequestsList.size() == 0) {
			request.setAttribute("noMatchesFound", true);
		} else {
			catalogItemRequestsList.sort(Comparator.comparing(CatalogItemRequestBean::getTitle));
			request.getSession().setAttribute("catalogItemRequestsList", catalogItemRequestsList);
		}
	}

	public void findCatalogItemRequestBean(HttpServletRequest request) {
		((List<CatalogItemRequestBean>) request.getSession().getAttribute("catalogItemRequestsList")).stream()
				.filter(cirbListItem -> cirbListItem.getId() == Long.parseLong(request.getParameter("beanId")))
				.findFirst()
				.ifPresent(cirbListItem -> request.getSession().setAttribute("catalogItemRequestBean", cirbListItem));
	}
}