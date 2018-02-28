package ua.khai.slynko.library.web.command.librarian;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Status;
import ua.khai.slynko.library.db.bean.CatalogItemRequestBean;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Confirm request command.
 * 
 * @author O.Slynko
 * 
 */
public class ConfirmRequestCommand extends Command {

	private static final Logger LOG = Logger.getLogger(ConfirmRequestCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		HttpSession session = request.getSession();
		DBManager dbManager = DBManager.getInstance();
		CatalogItemRequestBean catalogItemRequestBean = (CatalogItemRequestBean) session
				.getAttribute("catalogItemRequestBean");
		String address;
		String confirmationType = request.getParameter("bookStatus");
		String deleteRequest = request.getParameter("deleteRequest");
		Long id = catalogItemRequestBean.getId();

		if (deleteRequest != null && deleteRequest.equals("true")) {
			dbManager.removeLibraryCardItemById(Collections.singletonList(id.toString()));
			request.setAttribute("sendRedirect", true);
			request.getSession().setAttribute("requestIsDeletedSuccessfully", true);
			address = Path.PAGE_HOME_REDERECT;
		} else {
			if (!inputDataIsValid(request)) {
				address = Path.PAGE_CONFIRM_REQUEST_FORM;
			} else {
				String dateToStr = request.getParameter("dateTo");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date dateFrom = new Date();
				Date dateTo;
				try {
					dateTo = sdf.parse(dateToStr);
				} catch (ParseException e) {
					dateTo = new Date();
				}
				Integer penaltySize = Integer.parseInt(request.getParameter("penaltySize"));
				if (confirmationType.equals("libraryCard")) {
					dbManager.updateCatalogItemRequestDateFromDateToPenaltySizeById(dateFrom, dateTo, penaltySize,
							Status.LIBRARY_CARD.getValue(), id, true, catalogItemRequestBean.getUserEmail());
				} else if (confirmationType.equals("readingRoom")) {
					dbManager.updateCatalogItemRequestDateFromDateToPenaltySizeById(dateFrom, dateTo, penaltySize,
							Status.READING_ROOM.getValue(), id, false, catalogItemRequestBean.getUserEmail());
				}
				request.setAttribute("sendRedirect", true);
				request.getSession().setAttribute("requestIsConfirmedSuccessfully", true);
				address = Path.PAGE_HOME_REDERECT;
			}
		}
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws AppException {
		boolean isValid = true;
		HttpSession session = request.getSession();
		String dateToLiteral = "dateTo";
		String confirmationType = request.getParameter("bookStatus");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CatalogItemRequestBean catalogItemRequestBean = (CatalogItemRequestBean) session
				.getAttribute("catalogItemRequestBean");
		Long id = catalogItemRequestBean.getId();

		String dateTo = request.getParameter(dateToLiteral);
		String penaltySize = request.getParameter("penaltySize");

		Pattern dateToRegexp = Pattern.compile("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$");

		Matcher dateToMatcher = dateToRegexp.matcher(dateTo);

		String currentLocale = (String) session.getAttribute("currentLocale");
		ResourceBundle rb;
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}

		if (DBManager.getInstance().getCatalogItemInstancesByLibraryCardId(id) == 0) {
			request.setAttribute("validationMessage", rb.getString("confirmRequest.noSuchBook"));
			isValid = false;
		}
		if (!dateToMatcher.matches()) {
			request.setAttribute("dateToMessage", rb.getString("confirmRequest.dateToIsNotCorrect"));
			isValid = false;
		} else {
			Date dateToDate = null;
			try {
				dateToDate = sdf.parse(dateTo);
			} catch (ParseException e) {
				LOG.trace("Date parse error");
			}
			if (confirmationType.equals("libraryCard")) {
				if (dateToDate.before(new Date())) {
					request.setAttribute("dateToMessage", rb.getString("confirmRequest.dateToIsInThePast"));
					isValid = false;
				} else {
					request.setAttribute(dateToLiteral, dateTo);
				}
			} else if (confirmationType.equals("readingRoom")) {
				if (!isToday(dateToDate)) {
					request.setAttribute("dateToMessage", rb.getString("confirmRequest.dateToOnlyCurrent"));
					isValid = false;
				} else {
					request.setAttribute(dateToLiteral, dateTo);
				}
			}
		}
		if (!StringUtils.isNumeric(penaltySize)) {
			request.setAttribute("penaltySizeMessage", rb.getString("confirmRequest.penaltySizeIsNotCorrect"));
			isValid = false;
		} else {
			request.setAttribute("penaltySize", penaltySize);
		}
		return isValid;
	}

	/**
	 * <p>
	 * Checks if two dates are on the same day ignoring time.
	 * </p>
	 * 
	 * @param date1
	 *            the first date, not altered, not null
	 * @param date2
	 *            the second date, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either date is <code>null</code>
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	/**
	 * <p>
	 * Checks if two calendars represent the same day ignoring time.
	 * </p>
	 * 
	 * @param cal1
	 *            the first calendar, not altered, not null
	 * @param cal2
	 *            the second calendar, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either calendar is <code>null</code>
	 */
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * <p>
	 * Checks if a date is today.
	 * </p>
	 * 
	 * @param date
	 *            the date, not altered, not null.
	 * @return true if the date is today.
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 */
	private static boolean isToday(Date date) {
		return isSameDay(date, Calendar.getInstance().getTime());
	}
}