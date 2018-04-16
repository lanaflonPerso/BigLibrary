package ua.khai.slynko.library.web.command.outOfControl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.entity.Role;
import ua.khai.slynko.library.db.dao.UserDao;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.mail.MailHelper;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.validation.model.UserForm;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Sign up command.
 * 
 * @author O.Slynko
 * 
 */
public class SignUpCommand extends Command {

	private static final Logger LOG = Logger.getLogger(SignUpCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)	throws AppException {
		if (request.getSession().getAttribute("userRole") != null) {
			throw new AppException("You do not have permission to access the requested resource");
		}

		if (!inputDataIsValid(request)) {
			return Constants.Path.PAGE_SIGN_UP;
		} else {
			signUp(request);
			return Constants.Path.PAGE_LOGIN_REDERECT;
		}
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws AppException {
		return buildUserForm(request)
				.validateAndPrefillRequestWithErrors(request);
	}

	private UserForm buildUserForm(HttpServletRequest request) {
		return new UserForm(request.getParameter("firstName"),
				request.getParameter("lastName"),
				request.getParameter("email"),
				request.getParameter("login"),
				request.getParameter("password1"),
				request.getParameter("password2"));
	}

	private User buildUser(HttpServletRequest request) {
		User reader = new User();
		reader.setLogin(request.getParameter("login"));
		reader.setPassword(Password.hash(request.getParameter("password1")));
		reader.setFirstName(request.getParameter("firstName"));
		reader.setLastName(request.getParameter("lastName"));
		reader.setEmail(request.getParameter("email"));
		reader.setRoleId(Role.READER.getValue());
		return reader;
	}

	private void signUp(HttpServletRequest request) throws DBException {
		new UserDao().createReader(buildUser(request));
		try {
			MailHelper.sendMail(request.getParameter("email")
					, "Library registration"
					, "Registration is successful!" + " Your username is: " + request.getParameter("login") + " , password: "
							+ request.getParameter("password1"));
		} catch (Exception ex) {
			LOG.trace("Mail has not been sent");
		}
		request.getSession().setAttribute("signUpSuccessful", true);
		request.setAttribute("sendRedirect", true);
	}
}