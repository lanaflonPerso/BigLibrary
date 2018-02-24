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

import static ua.khai.slynko.library.constant.Constants.EMAIL_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.EMAIL_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.LOGIN_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.LOGIN_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.PASSWORD_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.PASSWORD_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.STRING_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.STRING_MIN_LENGTH;

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
		HttpSession session = request.getSession();
		String address;
		if (!inputDataIsValid(request)) {
			address = Path.PAGE_ADD_LIBRARIAN;
			LOG.trace("Forward address --> " + address);
		} else {
			User user = new User();
			user.setLogin(request.getParameter("login"));
			try {
				user.setPassword(Password.hash(request.getParameter("password1")));
			} catch (NoSuchAlgorithmException e) {
				throw new AppException("NoSuchAlgorithmException", e);
			}
			user.setFirstName(request.getParameter("firstName"));
			user.setLastName(request.getParameter("lastName"));
			user.setEmail(request.getParameter("email"));
			user.setRoleId(Role.LIBRARIAN.getValue());
			DBManager.getInstance().createUser(user);
			request.setAttribute("sendRedirect", true);
			address = Path.PAGE_LOGIN_REDERECT;
			session.setAttribute("redirectPage", Path.COMMAND_LIST_LIBRARIANS);
			session.setAttribute("librarianAddIsSuccessful", true);
		}
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws IOException, ServletException, AppException {
		boolean isValid = true;
		DBManager dbManager = DBManager.getInstance();
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");

		Pattern emailPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9\\.-]*\\@[a-zA-Z]+\\.[a-zA-Z]+$");
		HttpSession session = request.getSession();
		
		String currentLocale = (String) session.getAttribute("currentLocale");
		ResourceBundle rb;
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}

		Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
		String capthaAnswer = request.getParameter("capthaAnswer");

		if (login == null && email == null && firstName == null && lastName == null) {
			request.setAttribute("fillInMessage", rb.getString("signup.fieldsAreNotFilled"));
			isValid = false;
			return isValid;
		}
		if (firstName == null || firstName.length() < STRING_MIN_LENGTH) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooShort"));
			isValid = false;
		} else if (firstName.length() > STRING_MAX_LENGTH) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute("firstName", firstName);
		}
		if (lastName == null || lastName.length() < STRING_MIN_LENGTH) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooShort"));
			isValid = false;
		} else if (lastName.length() > STRING_MAX_LENGTH) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute("lastName", lastName);
		}
		String loginMessage = "loginMessage";
		if (login == null || login.length() < LOGIN_MIN_LENGTH) {
			request.setAttribute(loginMessage, rb.getString("signup.loginTooShort"));
			isValid = false;
		} else if (login.length() > LOGIN_MAX_LENGTH) {
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
		String emailMessage = "emailMessage";
		if (email == null || !emailPattern.matcher(email).matches()) {
			request.setAttribute(emailMessage, rb.getString("signup.emailIsNotValid"));
			isValid = false;
		} else if (email.length() < EMAIL_MIN_LENGTH) {
			request.setAttribute(emailMessage, rb.getString("signup.emailTooShort"));
			isValid = false;
		} else if (email.length() > EMAIL_MAX_LENGTH) {
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
		if (password1 == null || password1.length() < PASSWORD_MIN_LENGTH) {
			request.setAttribute("passwordMessage", rb.getString("signup.passwordTooShort"));
			isValid = false;
		} else if (password1.length() > PASSWORD_MAX_LENGTH) {
			request.setAttribute("passwordMessage", rb.getString("signup.passwordTooLong"));
			isValid = false;
		} else if (password2 == null || !password1.equals(password2)) {
			request.setAttribute("passwordMessage", rb.getString("signup.passwordsAreNotEqual"));
			isValid = false;
		}
		if (captcha != null && (capthaAnswer == null || !captcha.isCorrect(capthaAnswer))) {
			request.setAttribute("captchaMessage", rb.getString("signup.captchaIsNotCorrect"));
			isValid = false;
		}
		return isValid;
	}
}