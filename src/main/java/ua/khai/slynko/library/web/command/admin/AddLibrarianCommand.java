package ua.khai.slynko.library.web.command.admin;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import nl.captcha.Captcha;
import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.validation.model.BookForm;
import ua.khai.slynko.library.validation.model.LibrarianForm;
import ua.khai.slynko.library.web.abstractCommand.Command;

import static ua.khai.slynko.library.constant.Constants.EMAIL_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.EMAIL_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.LOGIN_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.LOGIN_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.PASSWORD_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.PASSWORD_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.STRING_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.STRING_MIN_LENGTH;

/**
 * Add librarian command.
 * 
 * @author O.Slynko
 * 
 */
public class AddLibrarianCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;
	private static final Logger LOG = Logger.getLogger(AddLibrarianCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		HttpSession session = request.getSession();
		String address;
		if (!inputDataIsValid(request)) {
			address = Path.PAGE_ADD_LIBRARIAN;
			LOG.trace("Forward address --> " + address);
		} else {
			User user = new User();
			user.setLogin(request.getParameter("login"));
			try {
				user.setPassword(Password.hash(request.getParameter("password1")));
			} catch (NoSuchAlgorithmException e) {
				throw new AppException("NoSuchAlgorithmException", e);
			}
			user.setFirstName(request.getParameter("firstName"));
			user.setLastName(request.getParameter("lastName"));
			user.setEmail(request.getParameter("email"));
			user.setRoleId(Role.LIBRARIAN.getValue());
			DBManager.getInstance().createUser(user);
			request.setAttribute("sendRedirect", true);
			address = Path.PAGE_LOGIN_REDERECT;
			session.setAttribute("redirectPage", Path.COMMAND_LIST_LIBRARIANS);
			session.setAttribute("librarianAddIsSuccessful", true);
		}
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws IOException, ServletException, AppException {
		return buildAddLibrarianForm(request)
				.validateAndPrefillRequestWithErrors(request);
	}

	private LibrarianForm buildAddLibrarianForm(HttpServletRequest request) {
		return new LibrarianForm(request.getParameter("firstName"), request.getParameter("lastName"),
				request.getParameter("email"), request.getParameter("login"),
				request.getParameter("password1"), request.getParameter("password2"));

	}
}