package ua.khai.slynko.library.web.command.outOfControl;

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

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.mail.MailHelper;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Sign up command.
 * 
 * @author O.Slynko
 * 
 */
public class SignUpCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;
	private static final Logger LOG = Logger.getLogger(SignUpCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");

		// obtain session
		HttpSession session = request.getSession();

		// obtain user role from session
		Role userRole = (Role) session.getAttribute("userRole");
		LOG.trace("Get userRole from session --> " + userRole);

		// check if user role is not null
		if (userRole != null) {
			throw new AppException("You do not have permission to access the requested resource");
		}

		// initialize address
		String address = Path.PAGE_ERROR_PAGE;
		LOG.trace("Forward address --> " + address);

		// check if input data is valid
		if (!inputDataIsValid(request)) {
			address = Path.PAGE_SIGN_UP;
			LOG.trace("Forward address --> " + address);
		} else {
			// create and initialize
			User user = new User();
			user.setLogin((String) request.getParameter("login"));
			try {
				user.setPassword(Password.hash(request.getParameter("password1")));
			} catch (NoSuchAlgorithmException e) {
				throw new AppException("NoSuchAlgorithmException", e);
			}
			user.setFirstName((String) request.getParameter("firstName"));
			user.setLastName((String) request.getParameter("lastName"));
			user.setEmail((String) request.getParameter("email"));
			user.setRoleId(Role.READER.getValue());
			LOG.info("User " + user + " is going to be inserted into database");

			// insert new user into db
			DBManager.getInstance().createReader(user);
			LOG.trace("Inserted in DB: user --> " + user);

			// set send redirect into request (because of prg)
			request.setAttribute("sendRedirect", true);

			// set redirect page
			address = Path.PAGE_LOGIN_REDERECT;
			LOG.trace("Redirect address --> " + address);
			session.setAttribute("signUpSuccessful", true);

			// send mail (registration is successful)
			String email = user.getEmail();
			String subject = "Library registration";
			String message = "Registration is successful!" + " Your username is: " + user.getLogin() + " , password: "
					+ request.getParameter("password1");
			try {
				MailHelper.sendMail(email, subject, message);
			} catch (Exception ex) {
				LOG.trace("Mail has not been sent");
			}

		}
		LOG.debug("Command finished");
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws IOException, ServletException, AppException {
		boolean isValid = true;

		// obtain dbManager
		DBManager dbManager = DBManager.getInstance();

		// obtain fields to validate from request
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		HttpSession session = request.getSession();

		// obtain captcha from session
		String capthaAnswer = request.getParameter("capthaAnswer");

		// obtain current locale for resource bundle
		String currentLocale = (String) session.getAttribute("currentLocale");
		LOG.debug("Locale --> " + currentLocale);

		// init resource bundle
		ResourceBundle rb;
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}

		Pattern emailPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9\\.-]*\\@[a-zA-Z]+\\.[a-zA-Z]+$");

		LOG.trace("Validation starts");

		// check if fields are filled
		if (firstName == null && lastName == null && login == null && email == null) {
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

		return isValid;
	}
}