package ua.khai.slynko.library.web.command.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Logout command.
 * 
 * @author O.Slynko
 * 
 */
public class LogoutCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		invalidateSession(request);
		cleanCookies(request, response);
		return Path.PAGE_LOGIN;
	}

	private void invalidateSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	private void cleanCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals("userId")) {
					cookie.setValue(null);
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}
}