package ua.khai.slynko.library.web.command.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.validation.model.UserForm;
import ua.khai.slynko.library.web.abstractCommand.Command;
import ua.khai.slynko.library.web.command.utils.CommandUtils;

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
			createLibrarian(request);
			return Path.PAGE_LOGIN_REDERECT;
		}
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws AppException {
		return buildAddLibrarianForm(request)
				.validateAndPrefillRequestWithErrors(request);
	}

	private UserForm buildAddLibrarianForm(HttpServletRequest request) {
		return new UserForm(request.getParameter("firstName"),
				request.getParameter("lastName"),
				request.getParameter("email"),
				request.getParameter("login"),
				request.getParameter("password1"),
				request.getParameter("password2"));
	}

	private void createLibrarian(HttpServletRequest request) throws DBException	{
		DBManager.getInstance().createUser(buildLibrarian(request));
		populateRequestSuccess(request);
		CommandUtils.setRedirect(request);
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

	private void populateRequestSuccess(HttpServletRequest request) {
		request.getSession().setAttribute("librarianAddIsSuccessful", true);
		request.getSession().setAttribute("redirectPage", Path.COMMAND_LIST_LIBRARIANS);
	}
}