package ua.khai.slynko.library.web.command.utils;

import javax.servlet.http.HttpServletRequest;

public final class CommandUtils {
  private CommandUtils() {}

  public static void setRedirect(HttpServletRequest request) {
    request.setAttribute("sendRedirect", true);
  }
}
