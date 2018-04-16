package ua.khai.slynko.library.web.command.admin;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Lists readers.
 * 
 * @author O.Slynko
 * 
 */
public class ListReadersCommand extends Command {

	private static final Logger LOG = Logger.getLogger(ListReadersCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
		if (isUserDetailsCommand(request)) {
			findReaderDetails(request);
			return Constants.Path.PAGE_USER_DETAILS;
		} else {
			findReadersAndSort(request);
			return Constants.Path.PAGE_LIST_USERS;
		}
	}

	private boolean isUserDetailsCommand(HttpServletRequest request) {
		return request.getParameter("userId") != null;
	}

	private void findReaderDetails(HttpServletRequest request) {
		((List<User>) request.getSession().getAttribute("listUsers")).stream()
				.filter(user -> user.getId() == Long.parseLong(request.getParameter("userId")))
				.findFirst()
				.ifPresent(user -> {
					request.setAttribute("user", user);
					try	{
						request.setAttribute("userIdBlocked", DBManager.getInstance().isUserBlocked(user.getId()));
					}	catch (DBException e)	{
						LOG.error(e.getMessage());
					}
				});
	}

	private void findReadersAndSort(HttpServletRequest request) throws DBException {
		List<User> listReaders = DBManager.getInstance().findUsers(Role.READER.getValue(),
				request.getParameter("firstName"),
				request.getParameter("lastName"));

		if (listReaders == null || listReaders.isEmpty()) {
			request.setAttribute("noMatchesFound", true);
		} else {
			listReaders.sort(Comparator.comparing(User::getLastName));
			request.getSession().setAttribute("listUsers", listReaders);
		}
	}
}