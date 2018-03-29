package ua.khai.slynko.library.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.DBException;

/**
 * Already logged in filter
 * 
 * @author O.Slynko
 * 
 */
public class AlreadyLoggedInFilter implements Filter {

	private static final Logger LOG = Logger.getLogger(AlreadyLoggedInFilter.class);

	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		User user = (User) session.getAttribute("user");
		Role userRole = (Role) session.getAttribute("userRole");
		String address;
		
		if (user == null) {
			Cookie[] cookies = ((HttpServletRequest) request).getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie c = cookies[i];
					if (c.getName().equals("userId")) {
						try {
							user = DBManager.getInstance().findUser(Long.parseLong(c.getValue()));
							userRole = Role.getRole(user);
							session.setAttribute("user", user);
							session.setAttribute("userRole", userRole);
						} catch (NumberFormatException | DBException e) {
							LOG.error(e.getMessage());
						}
					}
				}
			}
		}
		
		if (user != null && userRole != null) {
			address = (String) session.getAttribute("redirectPage");
			if (userRole == Role.ADMIN) {
				if (address == null) {
					address = Path.COMMAND_LIST_ADMIN_CATALOG;
				}
				initRequestAdminMessages((HttpServletRequest) request);
			}
			if (userRole == Role.READER) {
				if (address == null) {
					address = Path.COMMAND_LIST_CATALOG;
				}
				initRequestReaderMessages((HttpServletRequest) request);
			}
			if (userRole == Role.LIBRARIAN) {
				if (address == null) {
					address = Path.COMMAND_READERS_REQUESTS;
				}
				initRequestLibrarianMessages((HttpServletRequest) request);
			}
			request.getRequestDispatcher(address).forward(request, response);
		}
		initRequestCommonMessages((HttpServletRequest) request);
		chain.doFilter(request, response);
	}

	/**
	 * Method obtains session admin attributes and put it into request (after redirect has been sent)
	 * @param request
	 */
	private void initRequestAdminMessages(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean bookUpdateIsSuccessful = (Boolean) session.getAttribute("bookUpdateIsSuccessful");
		Boolean bookDeleteIsSuccessful = (Boolean) session.getAttribute("bookDeleteIsSuccessful");
		Boolean bookAddIsSuccessful = (Boolean) session.getAttribute("bookAddIsSuccessful");
		Boolean librarianAddIsSuccessful = (Boolean) session.getAttribute("librarianAddIsSuccessful");
		Boolean librarianDeleteIsSuccessful = (Boolean) session.getAttribute("librarianDeleteIsSuccessful");
		Boolean readerIsUnblockedSuccessfully = (Boolean) session.getAttribute("readerIsUnblockedSuccessfully");
		Boolean readerIsBlockedSuccessfully = (Boolean) session.getAttribute("readerIsBlockedSuccessfully");
		
		if (readerIsUnblockedSuccessfully != null) {
			request.setAttribute("readerIsUnblockedSuccessfully", readerIsUnblockedSuccessfully);
			session.removeAttribute("readerIsUnblockedSuccessfully");
		}
		if (readerIsBlockedSuccessfully != null) {
			request.setAttribute("readerIsBlockedSuccessfully", readerIsBlockedSuccessfully);
			session.removeAttribute("readerIsBlockedSuccessfully");
		}
		if (bookUpdateIsSuccessful != null) {
			request.setAttribute("bookUpdateIsSuccessful", bookUpdateIsSuccessful);
			session.removeAttribute("bookUpdateIsSuccessful");
		}
		if (bookDeleteIsSuccessful != null) {
			request.setAttribute("bookDeleteIsSuccessful", bookDeleteIsSuccessful);
			session.removeAttribute("bookDeleteIsSuccessful");
		}
		if (bookAddIsSuccessful != null) {
			request.setAttribute("bookAddIsSuccessful", bookAddIsSuccessful);
			session.removeAttribute("bookAddIsSuccessful");
		}
		if (librarianAddIsSuccessful != null) {
			request.setAttribute("librarianAddIsSuccessful", librarianAddIsSuccessful);
			session.removeAttribute("librarianAddIsSuccessful");
		}
		if (librarianDeleteIsSuccessful != null) {
			request.setAttribute("librarianDeleteIsSuccessful", librarianDeleteIsSuccessful);
			session.removeAttribute("librarianDeleteIsSuccessful");
		}
	}
	
	/**
	 * Method obtains session reader attributes and put it into request (after redirect has been sent)
	 * @param request
	 */
	private void initRequestReaderMessages(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean bookRequestIsSent = (Boolean) session.getAttribute("bookRequestIsSent");
		Boolean requestIsCanceledSuccessfully = (Boolean) session.getAttribute("requestIsCanceledSuccessfully");
		Boolean bookIsReturnedSuccessfully = (Boolean) session.getAttribute("bookIsReturnedSuccessfully");

		if (bookRequestIsSent != null) {
			request.setAttribute("bookRequestIsSent", bookRequestIsSent);
			session.removeAttribute("bookRequestIsSent");
		}
		if (requestIsCanceledSuccessfully != null) {
			request.setAttribute("requestIsCanceledSuccessfully", requestIsCanceledSuccessfully);
			session.removeAttribute("requestIsCanceledSuccessfully");
		}
		if (bookIsReturnedSuccessfully != null) {
			request.setAttribute("bookIsReturnedSuccessfully", bookIsReturnedSuccessfully);
			session.removeAttribute("bookIsReturnedSuccessfully");
		}
		
	}
	
	/**
	 * Method obtains session librarian attributes and put it into request (after redirect has been sent)
	 * @param request
	 */
	private void initRequestLibrarianMessages(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean requestIsConfirmedSuccessfully = (Boolean) session.getAttribute("requestIsConfirmedSuccessfully");
		Boolean requestIsDeletedSuccessfully = (Boolean) session.getAttribute("requestIsDeletedSuccessfully");
		
		if (requestIsConfirmedSuccessfully != null) {
			request.setAttribute("requestIsConfirmedSuccessfully", requestIsConfirmedSuccessfully);
			session.removeAttribute("requestIsConfirmedSuccessfully");
		}
		if (requestIsDeletedSuccessfully != null) {
			request.setAttribute("requestIsDeletedSuccessfully", requestIsDeletedSuccessfully);
			session.removeAttribute("requestIsDeletedSuccessfully");
		}
	}
	
	/**
	 * Method obtains session common attributes and put it into request (after redirect has been sent)
	 * @param request
	 */
	private void initRequestCommonMessages(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean registrationSeccessful = (Boolean) session.getAttribute("signUpSuccessful");
		Boolean passwordRestorationIsSuccessful = (Boolean) session.getAttribute("passwordRestorationIsSuccessful");
		Boolean profileUpdateIsSuccessful = (Boolean) session.getAttribute("profileUpdateIsSuccessful");
		Boolean passwordRestorationUserIsNotFound = (Boolean) session.getAttribute("passwordRestorationUserIsNotFound");

		if (passwordRestorationUserIsNotFound != null) {
			request.setAttribute("passwordRestorationUserIsNotFound", passwordRestorationUserIsNotFound);
			session.removeAttribute("passwordRestorationUserIsNotFound");
		}
		if (registrationSeccessful != null) {
			request.setAttribute("signUpSuccessful", registrationSeccessful);
			session.removeAttribute("signUpSuccessful");
		}
		if (passwordRestorationIsSuccessful != null) {
			request.setAttribute("passwordRestorationIsSuccessful", passwordRestorationIsSuccessful);
			session.removeAttribute("passwordRestorationIsSuccessful");
		}
		if (profileUpdateIsSuccessful != null) {
			request.setAttribute("profileUpdateIsSuccessful", profileUpdateIsSuccessful);
			session.removeAttribute("profileUpdateIsSuccessful");
		}
		
		session.removeAttribute("redirectPage");
	}

	public void init(FilterConfig fConfig) {
		// do nothing
	}
}