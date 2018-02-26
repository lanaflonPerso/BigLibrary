package ua.khai.slynko.library.web.command.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.web.abstractCommand.Command;

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
		HttpSession session = request.getSession();
		DBManager dbManager = DBManager.getInstance();
		Long userId = Long.parseLong(request.getParameter("userId"));
		if (dbManager.isUserBlocked(userId)) {
			dbManager.unblockUser(userId);
			session.setAttribute("readerIsUnblockedSuccessfully", true);
		} else {
			dbManager.blockUser(userId);
			session.setAttribute("readerIsBlockedSuccessfully", true);
		}
		String address = Path.PAGE_HOME_REDERECT;
		request.setAttribute("sendRedirect", true);
		session.setAttribute("redirectPage", Path.COMMAND_LIST_READERS);
		return address;
	}
}