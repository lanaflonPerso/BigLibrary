package ua.khai.slynko.library.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.web.abstractCommand.Command;

import java.util.Arrays;

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
		cleanUserIdCookie(request, response);
		return Path.PAGE_LOGIN;
	}

	private void invalidateSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	private void cleanUserIdCookie(HttpServletRequest request, HttpServletResponse response) {
		Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals("userId"))
				.findFirst()
				.ifPresent(cookie -> {
					cookie.setValue(null);
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				});
	}
}