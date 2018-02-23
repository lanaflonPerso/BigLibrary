package ua.khai.slynko.library.web.command.admin;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import nl.captcha.Captcha;
import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Add librarian command.
 * 
 * @author O.Slynko
 * 
 */
public class AddLibrarianCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;
	private static final Logger LOG = Logger.getLogger(AddLibrarianCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		// obtain session from request
		HttpSession session = request.getSession();
		// init address to forward/redirect
		String address = Path.PAGE_ERROR_PAGE;
		LOG.trace("Forward address --> " + address);
		// check if input data is valid
		if (!inputDataIsValid(request)) {
			address = Path.PAGE_ADD_LIBRARIAN;
			LOG.trace("Forward address --> " + address);
		} else {
			// create user and initialize it's fields
			User user = new User();
			user.setLogin((String) request.getParameter("login"));
			try {
				user.setPassword(Password.hash((String) request.getParameter("password1")));
			} catch (NoSuchAlgorithmException e) {
				throw new AppException("NoSuchAlgorithmException", e);
			}
			user.setFirstName((String) request.getParameter("firstName"));
			user.setLastName((String) request.getParameter("lastName"));
			user.setEmail((String) request.getParameter("email"));
			user.setRoleId(Role.LIBRARIAN.getValue());
			LOG.info("User " + user + " is going to be inserted into database");
			// create new user in db
			DBManager.getInstance().createUser(user);
			LOG.trace("Inserted in DB: user --> " + user);
			// set send redirect ture into request
			request.setAttribute("sendRedirect", true);
			// update address to forward/redirect
			address = Path.PAGE_LOGIN_REDERECT;
			LOG.trace("Redirect address --> " + address);
			// set redirect page into session
			session.setAttribute("redirectPage", Path.COMMAND_LIST_LIBRARIANS);
			LOG.trace("Redirect page --> " + Path.COMMAND_LIST_LIBRARIANS);
			// set librarian add is successful into session
			session.setAttribute("librarianAddIsSuccessful", true);
			LOG.trace("librarianAddIsSuccessful --> " + true);
		}
		LOG.debug("Command finished");
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws IOException, ServletException, AppException {
		boolean isValid = true;
		DBManager dbManager = DBManager.getInstance();
		// init fields for validation from request
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");

		Pattern emailPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9\\.-]*\\@[a-zA-Z]+\\.[a-zA-Z]+$");
		HttpSession session = request.getSession();
		
		// obtain resource bundle by current locale
		String currentLocale = (String) session.getAttribute("currentLocale");
		LOG.debug("Locale --> " + currentLocale);
		ResourceBundle rb;
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}

		Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
		String capthaAnswer = request.getParameter("capthaAnswer");

		LOG.trace("Validation starts");

		// check if fields are filled
		if (login == null && email == null && firstName == null && lastName == null) {
			request.setAttribute("fillInMessage", rb.getString("signup.fieldsAreNotFilled"));
			isValid = false;
			return isValid;
		}
		// first name validation
		if (firstName == null || firstName.length() < 1) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooShort"));
			isValid = false;
		} else if (firstName.length() > 20) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute("firstName", firstName);
		}
		// last name validation
		if (lastName == null || lastName.length() < 1) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooShort"));
			isValid = false;
		} else if (lastName.length() > 20) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute("lastName", lastName);
		}
		// login validation
		String loginMessage = "loginMessage";
		if (login == null || login.length() < 3) {
			request.setAttribute(loginMessage, rb.getString("signup.loginTooShort"));
			isValid = false;
		} else if (login.length() > 20) {
			request.setAttribute(loginMessage, rb.getString("signup.loginTooLong"));
			isValid = false;
		} else if (login.contains(" ")) {
			request.setAttribute(loginMessage, rb.getString("signup.loginContainsSpace"));
			isValid = false;
		} else if (dbManager.findUserByLogin(login) != null) {
			request.setAttribute(loginMessage, rb.getString("signup.loginIsTaken"));
			isValid = false;
		} else {
			request.setAttribute("login", login);
		}
		// email validation
		String emailMessage = "emailMessage";
		if (email == null || !emailPattern.matcher(email).matches()) {
			request.setAttribute(emailMessage, rb.getString("signup.emailIsNotValid"));
			isValid = false;
		} else if (email.length() < 5) {
			request.setAttribute(emailMessage, rb.getString("signup.emailTooShort"));
			isValid = false;
		} else if (email.length() > 30) {
			request.setAttribute(emailMessage, rb.getString("signup.emailTooLong"));
			isValid = false;
		} else if (email.contains(" ")) {
			request.setAttribute(emailMessage, rb.getString("signup.emailContainsSpace"));
			isValid = false;
		} else if (dbManager.findUserByEmail(email) != null) {
			request.setAttribute(emailMessage, rb.getString("signup.emailIsTaken"));
			isValid = false;
		} else {
			request.setAttribute("email", email);
		}
		// password validation
		if (password1 == null || password1.length() < 5) {
			request.setAttribute("passwordMessage", rb.getString("signup.passwordTooShort"));
			isValid = false;
		} else if (password1.length() > 20) {
			request.setAttribute("passwordMessage", rb.getString("signup.passwordTooLong"));
			isValid = false;
		} else if (password2 == null || !password1.equals(password2)) {
			request.setAttribute("passwordMessage", rb.getString("signup.passwordsAreNotEqual"));
			isValid = false;
		}
		// captcha validation
		if (captcha != null && (capthaAnswer == null || !captcha.isCorrect(capthaAnswer))) {
			request.setAttribute("captchaMessage", rb.getString("signup.captchaIsNotCorrect"));
			isValid = false;
		}
		LOG.trace("Validation end");
		return isValid;
	}
}