package ua.khai.slynko.library.web.command.outOfControl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Login command.
 * 
 * @author O.Slynko
 * 
 */
public class LoginCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();
		DBManager manager = DBManager.getInstance();

		// obtain login and password from a request
		String login = request.getParameter("login");
		String rememberMe = request.getParameter("rememberMe");

		LOG.trace("Request parameter: loging --> " + login);
		LOG.trace("Request parameter: rememberMe --> " + rememberMe);

		// obtain password hash
		String password = null;
		try {
			password = Password.hash(request.getParameter("password"));
		} catch (NoSuchAlgorithmException e) {
			throw new AppException("NoSuchAlgorithmException", e);
		}

		// check if login/password are not empty
		if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
			throw new AppException("Login/password cannot be empty");
		}

		// find user in db
		User user = manager.findUserByLogin(login);
		LOG.trace("Found in DB: user --> " + user);

		// check if user was not found
		if (user == null || !password.equals(user.getPassword())) {
			throw new AppException("Cannot find user with such login/password");
		}

		// check if user is not blocked
		boolean userIdBlocked = manager.isUserBlocked(user.getId());
		LOG.trace("Is user blocked --> " + userIdBlocked);
		if (userIdBlocked) {
			throw new AppException("User is blocked. Please contact your system administrator.");
		}

		// obtain user role
		Role userRole = Role.getRole(user);
		LOG.trace("userRole --> " + userRole);

		// set user in session
		session.setAttribute("user", user);
		LOG.trace("Set the session attribute: user --> " + user);

		// set user role in session
		session.setAttribute("userRole", userRole);
		LOG.trace("Set the session attribute: userRole --> " + userRole);

		// set cookie if remember me is true
		if (rememberMe != null && rememberMe.equals("true")) {
			Cookie c = new Cookie("userId", user.getId().toString());
			c.setMaxAge(24 * 60 * 60);
			response.addCookie(c);
		}
		LOG.info("User " + user + " logged as " + userRole.toString().toLowerCase());

		// get user locale and set locale
		String address = "changeLocale.jsp?locale=" + user.getLocale();
		LOG.trace("Set forward address --> " + address);

		LOG.debug("Command finished");
		return address;
	}
}