package ua.khai.slynko.library.web.command.admin;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Lists readers.
 * 
 * @author O.Slynko
 * 
 */
public class ListReadersCommand extends Command {

	private static final long serialVersionUID = 7732286214029478505L;

	private static final Logger LOG = Logger.getLogger(ListReadersCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		// obtain session from request
		HttpSession session = request.getSession();
		// init address for redirect/forward
		String address = Path.PAGE_LIST_USERS;

		String userId = request.getParameter("userId");
		LOG.trace("Requested item id array --> " + userId);
		// check if user is null
		if (userId != null) {
			// obtain users list from session
			List<User> listReaders = (List<User>) session.getAttribute("listUsers");
			LOG.trace("Found in session listLibrarians --> " + listReaders);
			// set user into request / set user is blocked into request
			for (User user : listReaders) {
				if (user.getId() == Long.parseLong(userId)) {
					request.setAttribute("user", user);
					request.setAttribute("userIdBlocked", DBManager.getInstance().isUserBlocked(user.getId()));
					LOG.trace("Set into request user --> " + user);
				}
			}
			// update address for redirect/forward
			address = Path.PAGE_USER_DETAILS;
			LOG.trace("Forward set to --> " + address);
		} else {
			// get users list
			String firstName = request.getParameter("firstName");
			LOG.trace("firstName --> " + firstName);
			String lastName = request.getParameter("lastName");
			LOG.trace("lastName --> " + lastName);
			List<User> listReaders = getReadersList(firstName, lastName);
			LOG.trace("Found in DB: listReaders --> " + listReaders);

			if (listReaders == null || listReaders.size() == 0) {
				request.setAttribute("noMatchesFound", true);
				LOG.trace("Set request attribute noMatchesFound --> true");
			} else {
				// sort librarians by last name
				Collections.sort(listReaders, compareByLastName);

				// put catalog items list to the request
				session.setAttribute("listUsers", listReaders);
				LOG.trace("Set the session attribute: listReaders --> " + listReaders);
			}
		}

		LOG.debug("Command finished");
		return address;
	}

	private static class CompareByLastName implements Comparator<User>, Serializable {
		private static final long serialVersionUID = -1573481565177573283L;

		public int compare(User u1, User u2) {
			return u1.getLastName().compareTo(u2.getLastName());
		}
	}

	private static Comparator<User> compareByLastName = new CompareByLastName();

	private List<User> getReadersList(String firstName, String lastName) throws DBException {
		DBManager dbManager = DBManager.getInstance();
		List<User> catalogItems = null;
		Integer roleId = Role.READER.getValue();
		if ((firstName == null || firstName.equals("")) && (lastName == null || lastName.equals(""))) {
			catalogItems = dbManager.findUsersByRole(roleId);
		} else if ((firstName == null || firstName.equals("")) && lastName != null && lastName.length() > 0) {
			catalogItems = dbManager.findUsersByRoleAndLastName(roleId, lastName);
		} else if (firstName != null && firstName.length() > 0 && (lastName == null || lastName.equals(""))) {
			catalogItems = dbManager.findUsersByRoleAndFirstName(roleId, firstName);
		} else if (firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0) {
			catalogItems = dbManager.findUsersByRoleAndFirstNameAndLastName(roleId, firstName, lastName);
		}
		return catalogItems;
	}
}