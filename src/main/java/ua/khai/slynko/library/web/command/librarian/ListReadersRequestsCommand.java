package ua.khai.slynko.library.web.command.librarian;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Status;
import ua.khai.slynko.library.db.bean.CatalogItemRequestBean;
import ua.khai.slynko.library.exception.AppException;
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
		HttpSession session = request.getSession();
		String address = Path.PAGE_LIST_READERS_REQUESTS;
		String beanId = request.getParameter("beanId");

		if (beanId != null) {
			CatalogItemRequestBean catalogItemRequestBean = null;
			List<CatalogItemRequestBean> catalogItemRequestsList = (List<CatalogItemRequestBean>) session
					.getAttribute("catalogItemRequestsList");
			for (CatalogItemRequestBean cirbListItem : catalogItemRequestsList) {
				if (cirbListItem.getId() == Long.parseLong(beanId)) {
					catalogItemRequestBean = cirbListItem;
				}
			}

			if (catalogItemRequestBean == null) {
				catalogItemRequestBean = (CatalogItemRequestBean) session.getAttribute("catalogItemRequestBean");
			}
			session.setAttribute("catalogItemRequestBean", catalogItemRequestBean);
			address = Path.PAGE_CONFIRM_REQUEST_FORM;
		} else {
			List<CatalogItemRequestBean> catalogItemRequestsList = DBManager.getInstance()
					.findCatalogItemRequests(Status.NOT_CONFIRMED.getValue());
			if (catalogItemRequestsList == null || catalogItemRequestsList.size() == 0) {
				request.setAttribute("noMatchesFound", true);
			} else {
				catalogItemRequestsList.sort(Comparator.comparing(CatalogItemRequestBean::getTitle));
				session.setAttribute("catalogItemRequestsList", catalogItemRequestsList);
			}
		}
		return address;
	}
}