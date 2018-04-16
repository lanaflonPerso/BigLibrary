package ua.khai.slynko.library.web.command.outOfControl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import ua.khai.slynko.library.db.entity.Role;
import ua.khai.slynko.library.db.dao.UserDao;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.web.abstractCommand.Command;

import static ua.khai.slynko.library.constant.Constants.TRUE;

/**
 * Login command.
 * 
 * @author O.Slynko
 * 
 */
public class LoginCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)	throws AppException {
		String password = Password.hash(request.getParameter("password"));
		if (StringUtils.isEmpty(request.getParameter("login"))
				|| StringUtils.isEmpty(request.getParameter("password"))) {
			throw new AppException("Login/password cannot be empty");
		}

		User user = new UserDao().findUserByLogin(request.getParameter("login"));
		if (user == null || !password.equals(user.getPassword())) {
			throw new AppException("Cannot find user with such login/password");
		}

		boolean userIdBlocked = new UserDao().isUserBlocked(user.getId());
		if (userIdBlocked) {
			throw new AppException("User is blocked. Please contact your system administrator.");
		}

		Role userRole = Role.getRole(user);
		request.getSession().setAttribute("user", user);
		request.getSession().setAttribute("userRole", userRole);
		if (TRUE.equals(request.getParameter("rememberMe"))) {
			Cookie c = new Cookie("userId", user.getId().toString());
			c.setMaxAge(24 * 60 * 60);
			response.addCookie(c);
		}
		return "changeLocale.jsp?locale=" + user.getLocale();
	}
}