package ua.khai.slynko.library.web.command.librarian;

import java.util.Collections;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Status;
import ua.khai.slynko.library.db.bean.CatalogItemRequestBean;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.validation.model.CatalogItemRequest;
import ua.khai.slynko.library.web.abstractCommand.Command;
import ua.khai.slynko.library.web.command.utils.CommandUtils;

import static ua.khai.slynko.library.constant.Constants.TRUE;

/**
 * Confirm request command.
 * 
 * @author O.Slynko
 * 
 */
public class ConfirmRequestCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
		if (isDeleteRequestCommand(request)) {
			deleteBookRequest(request);
			return Path.PAGE_HOME_REDERECT;
		} else if (!inputDataIsValid(request)) {
			return Path.PAGE_CONFIRM_REQUEST_FORM;
		} else {
			updateBookRequestDateFromAndDateToAndPenaltySize(request);
			return Path.PAGE_HOME_REDERECT;
		}
	}

	private boolean isDeleteRequestCommand(HttpServletRequest request) {
		return TRUE.equals(request.getParameter("deleteRequest"));
	}

	private void deleteBookRequest(HttpServletRequest request) throws DBException	{
		CatalogItemRequestBean catalogItemRequestBean = (CatalogItemRequestBean) request.getSession()
				.getAttribute("catalogItemRequestBean");
		DBManager.getInstance().removeLibraryCardItemById(Collections.singletonList(catalogItemRequestBean.getId().toString()));
		request.setAttribute("sendRedirect", true);
		request.getSession().setAttribute("requestIsDeletedSuccessfully", true);
	}
	private boolean inputDataIsValid(HttpServletRequest request) throws AppException {
		return new CatalogItemRequest(request.getParameter("bookStatus"),
				request.getParameter("penaltySize"),
				request.getParameter("dateTo"))
				.validateAndPrefillRequestWithErrors(request);
	}

	private void updateBookRequestDateFromAndDateToAndPenaltySize(HttpServletRequest request) throws DBException {
		CatalogItemRequestBean catalogItemRequestBean = (CatalogItemRequestBean) request.getSession()
				.getAttribute("catalogItemRequestBean");
		DBManager.getInstance().updateCatalogItemRequestDateFromDateToPenaltySizeById(new Date(), CommandUtils.getDateTo(request),
				Integer.parseInt(request.getParameter("penaltySize")),
				Status.getByKey(request.getParameter("bookStatus")).getValue(), catalogItemRequestBean.getId(),
				true, catalogItemRequestBean.getUserEmail());
		request.getSession().setAttribute("requestIsConfirmedSuccessfully", true);
		request.setAttribute("sendRedirect", true);
	}
}