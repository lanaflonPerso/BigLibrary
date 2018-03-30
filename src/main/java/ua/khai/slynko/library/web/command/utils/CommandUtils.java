package ua.khai.slynko.library.web.command.utils;

import org.apache.log4j.Logger;
import ua.khai.slynko.library.db.entity.CatalogItem;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public final class CommandUtils {
  private CommandUtils() {}

  private static final Logger LOG = Logger.getLogger(CommandUtils.class);

  public static void setRedirect(HttpServletRequest request) {
    request.setAttribute("sendRedirect", true);
  }

  public static void sortCatalogItemsBy(List<CatalogItem> catalogItems, String criteria) {
    if (criteria == null || criteria.equals("title")) {
      catalogItems.sort(Comparator.comparing(CatalogItem::getTitle));
    } else if (criteria.equals("author")) {
      catalogItems.sort(Comparator.comparing(CatalogItem::getAuthor));
    } else if (criteria.equals("edition")) {
      catalogItems.sort(Comparator.comparing(CatalogItem::getEdition));
    } else if (criteria.equals("publicationYear")) {
      catalogItems.sort(Comparator.comparing(CatalogItem::getPublicationYear));
    }
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
  public static boolean isToday(String dateString) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      date = sdf.parse(dateString);
    } catch (ParseException e) {
      LOG.error(e);
    }
    return isSameDay(date, Calendar.getInstance().getTime());
  }

  public static Date getDateTo(HttpServletRequest request) {
    String dateToStr = request.getParameter("dateTo");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date dateTo;
    try {
      dateTo = sdf.parse(dateToStr);
    } catch (ParseException e) {
      dateTo = new Date();
    }
    return dateTo;
  }

  public static boolean isDateBefore(String date, Date endDate) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date dateTo = null;
    try {
      dateTo = sdf.parse(date);
    } catch (ParseException e) {
      LOG.error(e);
    }
    return dateTo.compareTo(endDate) < 0;
  }
}
