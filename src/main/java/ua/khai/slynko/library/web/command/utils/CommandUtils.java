package ua.khai.slynko.library.web.command.utils;

import ua.khai.slynko.library.db.entity.CatalogItem;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

public final class CommandUtils {
  private CommandUtils() {}

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
}
