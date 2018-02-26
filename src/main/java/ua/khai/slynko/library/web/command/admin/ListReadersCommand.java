package ua.khai.slynko.library.web.command.admin;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Lists readers.
 * 
 * @author O.Slynko
 * 
 */
public class ListReadersCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		HttpSession session = request.getSession();
		String address = Path.PAGE_LIST_USERS;
		String userId = request.getParameter("userId");
		if (userId != null) {
			List<User> listReaders = (List<User>) session.getAttribute("listUsers");
			for (User user : listReaders) {
				if (user.getId() == Long.parseLong(userId)) {
					request.setAttribute("user", user);
					request.setAttribute("userIdBlocked", DBManager.getInstance().isUserBlocked(user.getId()));
				}
			}
			address = Path.PAGE_USER_DETAILS;
		} else {
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			List<User> listReaders = DBManager.getInstance().findUsers(Role.READER.getValue(), firstName, lastName);

			if (listReaders == null || listReaders.isEmpty()) {
				request.setAttribute("noMatchesFound", true);
			} else {
				listReaders.sort(Comparator.comparing(User::getLastName));
				session.setAttribute("listUsers", listReaders);
			}
		}
		return address;
	}
}