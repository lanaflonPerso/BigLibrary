package ua.khai.slynko.Library.web.command.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.Library.Path;
import ua.khai.slynko.Library.db.DBManager;
import ua.khai.slynko.Library.exception.AppException;
import ua.khai.slynko.Library.web.abstractCommand.Command;

/**
 * Login command.
 * 
 * @author O.Slynko
 * 
 */
public class BlockUnblockUserCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;
	private static final Logger LOG = Logger.getLogger(BlockUnblockUserCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		// obtain session from request
		HttpSession session = request.getSession();
		// obtain db manager instance
		DBManager dbManager = DBManager.getInstance();
		// obtain user id
		Long userId = Long.parseLong(request.getParameter("userId"));
		// block/unblock depending on current status
		if (dbManager.isUserBlocked(userId)) {
			dbManager.unblockUser(userId);
			session.setAttribute("readerIsUnblockedSuccessfully", true);
		} else {
			dbManager.blockUser(userId);
			session.setAttribute("readerIsBlockedSuccessfully", true);
		}
		// set redirect/forward address
		String address = Path.PAGE_HOME_REDERECT;
		LOG.trace("Redirect address --> " + address);
		// set send redirect true into request
		request.setAttribute("sendRedirect", true);
		// set after redirect page into session
		session.setAttribute("redirectPage", Path.COMMAND_LIST_READERS); 
		LOG.debug("Command finished");
		return address;
	}
}