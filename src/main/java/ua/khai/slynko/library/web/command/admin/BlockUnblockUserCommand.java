package ua.khai.slynko.library.web.command.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.exception.AppException;
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
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		Long userId = Long.parseLong(request.getParameter("userId"));
		DBManager.getInstance().toggleUserBlockStatus(userId);
		populateRequestSuccess(request);
		CommandUtils.setRedirect(request);
		return Path.PAGE_HOME_REDERECT;
	}

	private void populateRequestSuccess(HttpServletRequest request) {
		request.getSession().setAttribute("redirectPage", Path.COMMAND_LIST_READERS);
	}
}