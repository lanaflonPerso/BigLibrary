package ua.khai.slynko.library.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.entity.Role;
import ua.khai.slynko.library.db.dao.UserDao;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.DBException;

/**
 * Do logout filter
 * 
 * @author O.Slynko
 * 
 */
public class DoLogoutFilter implements Filter {

	private static final Logger LOG = Logger.getLogger(DoLogoutFilter.class);

	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (isAccessAllowed(request)) {
			chain.doFilter(request, response);
		} else {
			request.getRequestDispatcher(Constants.Path.COMMAND_LOGOUT).forward(request, response);
		}
	}

	private boolean isAccessAllowed(ServletRequest request) {
		boolean accessAllowed = true;
		HttpSession session = ((HttpServletRequest) request).getSession();
		User user = (User) session.getAttribute("user");
		Role userRole = (Role) session.getAttribute("userRole");
		if (user != null && userRole != null) {
			try {
				if (userRole == Role.READER && new UserDao().isUserBlocked(user.getId())) {
					accessAllowed = false;
				}
				if (userRole == Role.LIBRARIAN && new UserDao().findUser(user.getId()) == null) {
					accessAllowed = false;
				}
			} catch (DBException e) {
				LOG.error(e.getMessage());
			}
		}
		return accessAllowed;
	}

	public void init(FilterConfig fConfig) {
		// do nothing
	}
}