package ua.khai.slynko.library.validation.model;

import org.apache.commons.lang3.StringUtils;
import ua.khai.slynko.library.db.ConnectionManager;
import ua.khai.slynko.library.db.bean.CatalogItemRequestBean;
import ua.khai.slynko.library.db.dao.CatalogItemDao;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.web.command.utils.CommandUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static ua.khai.slynko.library.constant.Constants.DATE_PATTERN;

public class CatalogItemRequestForm {
  private String confirmationType;
  private String penaltySize;
  private String dateTo;

  public CatalogItemRequestForm(String confirmationType, String penaltySize, String dateTo) {
    this.confirmationType = confirmationType;
    this.penaltySize = penaltySize;
    this.dateTo = dateTo;
  }

  public boolean validateAndPrefillRequestWithErrors(HttpServletRequest request) throws DBException  {
    boolean isValid = true;
    CatalogItemRequestBean catalogItemRequestBean = (CatalogItemRequestBean) request.getSession()
        .getAttribute("catalogItemRequestBean");
    String currentLocale = (String) request.getSession().getAttribute("currentLocale");
    ResourceBundle rb;
    if (currentLocale == null) {
      rb = ResourceBundle.getBundle("resources", Locale.getDefault());
    } else {
      rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
    }

    if (new CatalogItemDao().getCatalogItemInstancesByLibraryCardId(catalogItemRequestBean.getId()) == 0) {
      request.setAttribute("validationMessage", rb.getString("confirmRequest.noSuchBook"));
      isValid = false;
    }
    if (!Pattern.compile(DATE_PATTERN).matcher(dateTo).matches()) {
      request.setAttribute("dateToMessage", rb.getString("confirmRequest.dateToIsNotCorrect"));
      isValid = false;
    } else {
      if (confirmationType.equals("libraryCard")) {
        if (CommandUtils.isDateBefore(dateTo, new Date())) {
          request.setAttribute("dateToMessage", rb.getString("confirmRequest.dateToIsInThePast"));
          isValid = false;
        } else {
          request.setAttribute("dateTo", dateTo);
        }
      } else if (confirmationType.equals("readingRoom")) {
        if (!CommandUtils.isToday(dateTo)) {
          request.setAttribute("dateToMessage", rb.getString("confirmRequest.dateToOnlyCurrent"));
          isValid = false;
        } else {
          request.setAttribute("dateTo", dateTo);
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
}
