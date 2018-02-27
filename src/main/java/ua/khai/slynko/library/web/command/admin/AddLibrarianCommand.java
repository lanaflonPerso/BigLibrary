package ua.khai.slynko.library.web.command.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.validation.model.LibrarianForm;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Add librarian command.
 *
 * @author O.Slynko
 *
 */
public class AddLibrarianCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		if (!inputDataIsValid(request)) {
			return Path.PAGE_ADD_LIBRARIAN;
		} else {
            DBManager.getInstance().createUser(buildLibrarian(request));
            request.setAttribute("sendRedirect", true);
			request.getSession().setAttribute("redirectPage", Path.COMMAND_LIST_LIBRARIANS);
			request.getSession().setAttribute("librarianAddIsSuccessful", true);
			return Path.PAGE_LOGIN_REDERECT;
		}
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws AppException {
		return buildAddLibrarianForm(request)
				.validateAndPrefillRequestWithErrors(request);
	}

	private LibrarianForm buildAddLibrarianForm(HttpServletRequest request) {
		return new LibrarianForm(request.getParameter("firstName"), request.getParameter("lastName"),
				request.getParameter("email"), request.getParameter("login"),
				request.getParameter("password1"), request.getParameter("password2"));

	}

	private User buildLibrarian(HttpServletRequest request) {
		User librarian = new User();
		librarian.setLogin(request.getParameter("login"));
		librarian.setPassword(Password.hash(request.getParameter("password1")));
		librarian.setFirstName(request.getParameter("firstName"));
		librarian.setLastName(request.getParameter("lastName"));
		librarian.setEmail(request.getParameter("email"));
		librarian.setRoleId(Role.LIBRARIAN.getValue());
		return librarian;
	}
}