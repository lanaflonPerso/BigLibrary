package ua.khai.slynko.Library.web.filter;

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

import ua.khai.slynko.Library.Path;
import ua.khai.slynko.Library.db.DBManager;
import ua.khai.slynko.Library.db.Role;
import ua.khai.slynko.Library.db.entity.User;
import ua.khai.slynko.Library.exception.DBException;

/**
 * Do logout filter
 * 
 * @author O.Slynko
 * 
 */
public class DoLogoutFilter implements Filter {

	private static final Logger LOG = Logger.getLogger(DoLogoutFilter.class);

	public void destroy() {
		LOG.debug("Filter destruction starts");
		// do nothing
		LOG.debug("Filter destruction finished");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOG.debug("Filter starts");
		// check if access allowed
		if (isAccessAllowed(request)) {
			LOG.debug("Filter finished");
			chain.doFilter(request, response);
		} else {
			LOG.trace("You don't have acces to this page anymore");
			request.getRequestDispatcher(Path.COMMAND_LOGOUT).forward(request, response);
		}
	}

	private boolean isAccessAllowed(ServletRequest request) {
		boolean accessAllowed = true;
		HttpSession session = ((HttpServletRequest) request).getSession();
		User user = (User) session.getAttribute("user");
		LOG.debug("user --> " + user);
		Role userRole = (Role) session.getAttribute("userRole");
		LOG.debug("userRole --> " + userRole);

		if (user != null && userRole != null) {
			DBManager dbManager = null;
			try {
				dbManager = DBManager.getInstance();
				// ckeck if reader is blocked
				if (userRole == Role.READER && dbManager.isUserBlocked(user.getId())) {
					accessAllowed = false;
				}
				// check if librarian exists
				if (userRole == Role.LIBRARIAN && dbManager.findUser(user.getId()) == null) {
					accessAllowed = false;
				}
			} catch (DBException e) {
				LOG.error(e.getMessage());
			}
		}
		return accessAllowed;
	}

	public void init(FilterConfig fConfig) throws ServletException {
		LOG.debug("Filter initialization starts");

		LOG.debug("Filter initialization finished");
	}
}