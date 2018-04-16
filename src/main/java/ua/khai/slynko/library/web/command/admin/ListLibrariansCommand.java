package ua.khai.slynko.library.web.command.admin;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.entity.Role;
import ua.khai.slynko.library.db.dao.UserDao;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.abstractCommand.Command;
import ua.khai.slynko.library.web.command.utils.CommandUtils;

/**
 * Lists librarians.
 * 
 * @author O.Slynko
 * 
 */
public class ListLibrariansCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
		if (isRemoveLibrarianCommand(request)) {
			removeLibrarian(request);
			return Constants.Path.PAGE_HOME_REDERECT;
		}  else	{
			findLibrariansAndSort(request);
			return Constants.Path.PAGE_LIST_USERS;
		}
	}

	private boolean isRemoveLibrarianCommand(HttpServletRequest request) {
		return request.getParameter("userId") != null;
	}

	private void removeLibrarian(HttpServletRequest request) throws DBException	{
		new UserDao().removeUserById(Long.parseLong(request.getParameter("userId")));
		populateLibrarianRemovedSuccessfully(request);
		CommandUtils.setRedirect(request);
	}

	private void populateLibrarianRemovedSuccessfully(HttpServletRequest request) {
		request.getSession().setAttribute("librarianDeleteIsSuccessful", true);
		request.getSession().setAttribute("redirectPage", Constants.Path.COMMAND_LIST_LIBRARIANS);
	}

	private void findLibrariansAndSort(HttpServletRequest request) throws DBException	{
		List<User> listLibrarians = new UserDao().findUsers(Role.LIBRARIAN.getValue(),
				request.getParameter("firstName"),
				request.getParameter("lastName"));
		if (listLibrarians == null || listLibrarians.size() == 0) {
			request.setAttribute("noMatchesFound", true);
		} else {
			listLibrarians.sort(Comparator.comparing(User::getLastName));
			request.getSession().setAttribute("listUsers", listLibrarians);
		}
	}
}