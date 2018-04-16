package ua.khai.slynko.library.web.command.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.ConnectionManager;
import ua.khai.slynko.library.db.dao.UserDao;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.abstractCommand.Command;
import ua.khai.slynko.library.web.command.utils.CommandUtils;

/**
 * Login command.
 * 
 * @author O.Slynko
 * 
 */
public class BlockUnblockUserCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
		toggleUserBlockStatus(request);
		return Constants.Path.PAGE_HOME_REDERECT;
	}

	private void toggleUserBlockStatus(HttpServletRequest request) throws DBException {
		Long userId = Long.parseLong(request.getParameter("userId"));
		new UserDao().toggleUserBlockStatus(userId);
		populateRequestSuccess(request);
		CommandUtils.setRedirect(request);
	}
	private void populateRequestSuccess(HttpServletRequest request) {
		request.getSession().setAttribute("redirectPage", Constants.Path.COMMAND_LIST_READERS);
	}
}