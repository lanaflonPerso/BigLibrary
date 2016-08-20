package ua.khai.slynko.Library.web.command.librarian;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.khai.slynko.Library.Path;
import ua.khai.slynko.Library.db.DBManager;
import ua.khai.slynko.Library.db.Status;
import ua.khai.slynko.Library.db.bean.CatalogItemRequestBean;
import ua.khai.slynko.Library.exception.AppException;
import ua.khai.slynko.Library.web.abstractCommand.Command;

/**
 * Confirm request command.
 * 
 * @author O.Slynko
 * 
 */
public class ConfirmRequestCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(ConfirmRequestCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		// obtain session from request
		HttpSession session = request.getSession();
		// obtain dbManager instance
		DBManager dbManager = DBManager.getInstance();
		// obtain request bean from session
		CatalogItemRequestBean catalogItemRequestBean = (CatalogItemRequestBean) session
				.getAttribute("catalogItemRequestBean");
		// init address
		String address = Path.PAGE_ERROR_PAGE;
		// init confirmation type
		String confirmationType = request.getParameter("bookStatus");
		String deleteRequest = request.getParameter("deleteRequest");
		// obtain catalog item id from bean
		Long id = catalogItemRequestBean.getId();
		LOG.trace("deleteRequest --> " + deleteRequest);
		LOG.trace("Forward address --> " + address);

		if (deleteRequest != null && deleteRequest.equals("true")) {
			// init items list to delete
			List<String> idList = new ArrayList<String>();
			idList.add(id.toString());
			// remove items from db
			dbManager.removeLibraryCardItemById(idList);
			// set send redirect into request
			request.setAttribute("sendRedirect", true);
			// init redirect address
			address = Path.PAGE_HOME_REDERECT;
			LOG.trace("Redirect address --> " + address);
			// set request is deleted successfully into session
			session.setAttribute("requestIsDeletedSuccessfully", true);
		} else {
			// check if input data is valid
			if (!inputDataIsValid(request)) {
				address = Path.PAGE_CONFIRM_REQUEST_FORM;
				LOG.trace("Forward address --> " + address);
			} else {
				// init date from and date to from request
				String dateToStr = request.getParameter("dateTo");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date dateFrom = new Date();
				Date dateTo = null;
				try {
					dateTo = sdf.parse(dateToStr);
				} catch (ParseException e) {
					LOG.trace("Date parse error");
					dateTo = new Date();
				}
				// init penalty size
				Integer penaltySize = (Integer) Integer.parseInt(request.getParameter("penaltySize"));
				LOG.info("id: " + id + ", dateFrom: " + dateFrom + ", dateTo: " + dateTo + ", penaltySize: "
						+ penaltySize + " are going to be inserted into database");
				
				
				LOG.trace("userEmail ==>" + catalogItemRequestBean.getUserEmail());
				
				
				// update library card item by confirmation type
				if (confirmationType.equals("libraryCard")) {
					dbManager.updateCatalogItemRequestDateFromDateToPenaltySizeById(dateFrom, dateTo, penaltySize,
							Status.LIBRARY_CARD.getValue(), id, true, catalogItemRequestBean.getUserEmail());
				} else if (confirmationType.equals("readingRoom")) {
					dbManager.updateCatalogItemRequestDateFromDateToPenaltySizeById(dateFrom, dateTo, penaltySize,
							Status.READING_ROOM.getValue(), id, false, catalogItemRequestBean.getUserEmail());
				}
				
				LOG.trace("Insertion is successful ");
				// set send redirect true into request
				request.setAttribute("sendRedirect", true);
				// init redirect address
				address = Path.PAGE_HOME_REDERECT;
				LOG.trace("Redirect address --> " + address);
				// set request is confirmed successfully into session
				session.setAttribute("requestIsConfirmedSuccessfully", true);
			}
		}
		LOG.debug("Command finished");
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws IOException, ServletException, AppException {
		boolean isValid = true;
		// obtain session
		HttpSession session = request.getSession();
		// init params for validation
		String dateToLiteral = "dateTo";
		String confirmationType = request.getParameter("bookStatus");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CatalogItemRequestBean catalogItemRequestBean = (CatalogItemRequestBean) session
				.getAttribute("catalogItemRequestBean");
		Long id = catalogItemRequestBean.getId();

		String dateTo = request.getParameter(dateToLiteral);
		String penaltySize = request.getParameter("penaltySize");

		Pattern dateToRegexp = Pattern.compile("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$");
		Pattern penaltySizeRegexp = Pattern.compile("^[0-9]+$");

		Matcher dateToMatcher = dateToRegexp.matcher(dateTo);
		Matcher penaltySizeMatcher = penaltySizeRegexp.matcher(penaltySize);

		// obtain resource bundle by current locale
		String currentLocale = (String) session.getAttribute("currentLocale");
		LOG.debug("Locale --> " + currentLocale);
		ResourceBundle rb;
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}

		LOG.trace("Validation starts");
		if (DBManager.getInstance().getCatalogItemInstancesByLibraryCardId(id) == 0) {
			request.setAttribute("validationMessage", rb.getString("confirmRequest.noSuchBook"));
			isValid = false;
		}
		// date to validation
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
			// validate date to depending on confirmation type
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
		// penalty size validation
		if (!penaltySizeMatcher.matches()) {
			request.setAttribute("penaltySizeMessage", rb.getString("confirmRequest.penaltySizeIsNotCorrect"));
			isValid = false;
		} else {
			request.setAttribute("penaltySize", penaltySize);
		}
		LOG.trace("Validation ends");
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