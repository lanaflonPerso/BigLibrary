package ua.khai.slynko.Library.tag;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

/**
 * Print penalty
 * 
 * @author O.Slynko
 *
 */
public class PrintPenalty extends BodyTagSupport {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(PrintPenalty.class);

	private Date penaltyDate;

	public void setPenaltyDate(Date penaltyDate) {
		if (penaltyDate != null) {
			this.penaltyDate = new Date(penaltyDate.getTime());
		}
	}

	private Integer penaltySize;

	public void setPenaltySize(Integer penaltySize) {
		this.penaltySize = penaltySize;
	}

	@Override
	public int doStartTag() throws JspException {

		// print penalty size or 0 depending on current date
		if (penaltyDate != null) {
			String currentLocaleStr = (String) pageContext.getSession().getAttribute("currentLocale");
			LOG.debug("Current locale : " + currentLocaleStr);
			Locale locale = getLocale(currentLocaleStr);
			NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

			Calendar penaltyCalendar = Calendar.getInstance();
			penaltyCalendar.setTime(penaltyDate);
			Calendar currentCalendar = Calendar.getInstance();
			if (currentCalendar.getTimeInMillis() > penaltyCalendar.getTimeInMillis()) {
				try {
					pageContext.getOut()
							.write("<td class='message'>" + currencyFormatter.format(penaltySize) + "</td>");
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
			} else {
				try {
					pageContext.getOut().write("<td>" + currencyFormatter.format(0) + "</td>");
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
			}
		} else {
			try {
				pageContext.getOut().write("<td></td>");
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
		}
		return SKIP_BODY;
	}

	private Locale getLocale(String currentLocaleStr) {
		Locale locale = null;
		if (currentLocaleStr.equals("ru") || currentLocaleStr.equals("RU")) {
			locale = new Locale("ru", "UA");
		} else {
			locale = new Locale("en", "US");
		}
		return locale;
	}
}