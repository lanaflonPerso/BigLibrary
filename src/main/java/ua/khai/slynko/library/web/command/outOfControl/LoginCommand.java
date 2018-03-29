package ua.khai.slynko.library.web.command.outOfControl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		DBManager manager = DBManager.getInstance();
		String login = request.getParameter("login");
		String rememberMe = request.getParameter("rememberMe");
		String password = Password.hash(request.getParameter("password"));
		if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
			throw new AppException("Login/password cannot be empty");
		}

		User user = manager.findUserByLogin(login);
		if (user == null || !password.equals(user.getPassword())) {
			throw new AppException("Cannot find user with such login/password");
		}

		boolean userIdBlocked = manager.isUserBlocked(user.getId());
		if (userIdBlocked) {
			throw new AppException("User is blocked. Please contact your system administrator.");
		}

		Role userRole = Role.getRole(user);
		request.getSession().setAttribute("user", user);
		request.getSession().setAttribute("userRole", userRole);
		if (rememberMe != null && rememberMe.equals("true")) {
			Cookie c = new Cookie("userId", user.getId().toString());
			c.setMaxAge(24 * 60 * 60);
			response.addCookie(c);
		}
		return "changeLocale.jsp?locale=" + user.getLocale();
	}
}