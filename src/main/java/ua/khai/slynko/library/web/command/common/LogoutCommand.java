package ua.khai.slynko.library.web.command.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Logout command.
 * 
 * @author O.Slynko
 * 
 */
public class LogoutCommand extends Command {

	private static final long serialVersionUID = -2785976616686657267L;

	private static final Logger LOG = Logger.getLogger(LogoutCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("Command starts");

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		// remove user id (remember me) from cookies
		Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c.getName().equals("userId")) {
					c.setValue(null);
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
		}

		LOG.debug("Command finished");
		return Path.PAGE_LOGIN;
	}

}