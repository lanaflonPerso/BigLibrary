package ua.khai.slynko.library.tag;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Locale;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.DBException;

/**
 * Change user locale tag support class
 * 
 * @author O.Slynko
 *
 */
public class ChangeUserLocale extends TagSupport {

	private static final Logger LOG = Logger.getLogger(ChangeUserLocale.class);
	private String locale;

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
	public int doStartTag() {
		HttpSession session = pageContext.getSession();
		User user = (User) session.getAttribute("user");
		if (user != null && Locale.contains(locale)) {
			Integer localeId = Locale.valueOf(locale.toUpperCase()).getValue();
			try {
				DBManager.getInstance().setUserLocale(localeId, user.getId());
				String newLocale = Locale.values()[localeId - 1].toString();
				user.setLocale(newLocale);
				session.setAttribute("user", user);
			} catch (DBException e) {
				LOG.error(e.getMessage());
			}
		}
		return SKIP_BODY;
	}
}