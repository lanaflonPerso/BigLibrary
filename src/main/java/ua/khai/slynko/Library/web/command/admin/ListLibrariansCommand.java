package ua.khai.slynko.Library.web.command.admin;

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

import ua.khai.slynko.Library.Path;
import ua.khai.slynko.Library.db.DBManager;
import ua.khai.slynko.Library.db.Role;
import ua.khai.slynko.Library.db.entity.User;
import ua.khai.slynko.Library.exception.AppException;
import ua.khai.slynko.Library.exception.DBException;
import ua.khai.slynko.Library.web.abstractCommand.Command;

/**
 * Lists librarians.
 * 
 * @author O.Slynko
 * 
 */
public class ListLibrariansCommand extends Command {

	private static final long serialVersionUID = 7732286214029478505L;
	private static final Logger LOG = Logger.getLogger(ListLibrariansCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		// obtain session from request
		HttpSession session = request.getSession();
		// init address
		String address = Path.PAGE_LIST_USERS;

		// obtain user id
		String userId = request.getParameter("userId");
		LOG.trace("Requested user id --> " + userId);

		// check if user id is null
		if (userId != null) {
			// remove user from db
			DBManager.getInstance().removeUserById(Long.parseLong(userId));
			// update address
			address = Path.PAGE_LIST_USERS;
			LOG.trace("Forward set to --> " + address);
			// set librarian delete is successful into session
			session.setAttribute("librarianDeleteIsSuccessful", true);
			LOG.trace("librarianDeleteIsSuccessful --> " + true);
			// set send redirect true into request
			request.setAttribute("sendRedirect", true);
			// set redirect page into session
			session.setAttribute("redirectPage", Path.COMMAND_LIST_LIBRARIANS);
			address = Path.PAGE_HOME_REDERECT;
		} else {
			// get librarians list
			String firstName = request.getParameter("firstName");
			LOG.trace("firstName --> " + firstName);
			String lastName = request.getParameter("lastName");
			LOG.trace("lastName --> " + lastName);
			List<User> listLibrarians = getListLibrarians(firstName, lastName);
			LOG.trace("Found in DB: listLibrarians --> " + listLibrarians);

			if (listLibrarians == null || listLibrarians.size() == 0) {
				request.setAttribute("noMatchesFound", true);
				LOG.trace("Set request attribute noMatchesFound --> true");
			} else {
				// sort librarians by last name
				Collections.sort(listLibrarians, compareByLastName);

				// put catalog items list to the request
				session.setAttribute("listUsers", listLibrarians);
				LOG.trace("Set the session attribute: listLibrarians --> " + listLibrarians);
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

	private List<User> getListLibrarians(String firstName, String lastName) throws DBException {
		DBManager dbManager = DBManager.getInstance();
		List<User> catalogItems = null;
		Integer roleId = Role.LIBRARIAN.getValue();
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