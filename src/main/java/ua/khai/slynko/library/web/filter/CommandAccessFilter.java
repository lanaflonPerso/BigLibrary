package ua.khai.slynko.library.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;

/**
 * Command access filter
 * 
 * @author O.Slynko
 * 
 */
public class CommandAccessFilter implements Filter {
	
	// commands access
	private Map<Role, List<String>> accessMap = new HashMap<>();
	private List<String> commons = new ArrayList<>();
	private List<String> outOfControl = new ArrayList<>();
	
	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (accessAllowed(request)) {
			chain.doFilter(request, response);
		} else {
			String errorMessasge = "You do not have permission to access the requested resource";
			request.setAttribute("errorMessage", errorMessasge);
			request.getRequestDispatcher(Path.PAGE_ERROR_PAGE)
					.forward(request, response);
		}
	}
	
	private boolean accessAllowed(ServletRequest request) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

		String commandName = request.getParameter("command");
		if (commandName == null || commandName.isEmpty()) {
			return false;
		}
		
		if (outOfControl.contains(commandName)) {
			return true;
		}
		
		HttpSession session = httpRequest.getSession(false);
		if (session == null) { 
			return false;
		}
		
		Role userRole = (Role)session.getAttribute("userRole");
		if (userRole == null) {
			return false;
		}
		
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return false;
		}
		
		return accessMap.get(userRole).contains(commandName)
				|| commons.contains(commandName);
	}

	public void init(FilterConfig fConfig) {
		// roles
		accessMap.put(Role.ADMIN, asList(fConfig.getInitParameter("admin")));
		accessMap.put(Role.READER, asList(fConfig.getInitParameter("reader")));
		accessMap.put(Role.LIBRARIAN, asList(fConfig.getInitParameter("librarian")));

		// commons
		commons = asList(fConfig.getInitParameter("common"));

		// out of control
		outOfControl = asList(fConfig.getInitParameter("out-of-control"));
	}
	
	/**
	 * Extracts parameter values from string.
	 * 
	 * @param str
	 *            parameter values string.
	 * @return list of parameter values.
	 */
	private List<String> asList(String str) {
		List<String> list = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(str);
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
		return list;		
	}
	
}