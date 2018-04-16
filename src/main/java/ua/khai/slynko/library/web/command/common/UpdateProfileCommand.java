package ua.khai.slynko.library.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.ConnectionManager;
import ua.khai.slynko.library.db.dao.UserDao;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.validation.model.UserForm;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Update profile command.
 * 
 * @author O.Slynko
 * 
 */
public class UpdateProfileCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
		if (!inputDataIsValid(request)) {
			return Constants.Path.PAGE_UPDATE_PROFILE;
		} else {
			updateUser(request);
			return Constants.Path.PAGE_HOME_REDERECT;
		}
	}

	private void updateUser(HttpServletRequest request) throws DBException {
		User user = buildUser(request);
		new UserDao().updateUser(user);
		request.getSession().setAttribute("user", user);
		request.getSession().setAttribute("redirectPage", Constants.Path.COMMAND_SETTINGS);
		request.getSession().setAttribute("profileUpdateIsSuccessful", true);
		request.setAttribute("sendRedirect", true);
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws DBException	{
		return buildUserForm(request)
				.validateAndPrefillRequestWithErrors(request);
	}

	private UserForm buildUserForm(HttpServletRequest request) {
		return new UserForm(request.getParameter("firstName"), request.getParameter("lastName"),
				request.getParameter("email"), request.getParameter("login"),
				request.getParameter("newPassword1"), request.getParameter("newPassword2"));
	}

	private User buildUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		user.setLogin(request.getParameter("login"));
		user.setPassword(Password.hash(request.getParameter("newPassword1")));
		user.setFirstName(request.getParameter("firstName"));
		user.setLastName(request.getParameter("lastName"));
		user.setEmail(request.getParameter("email"));
		return user;
	}
}