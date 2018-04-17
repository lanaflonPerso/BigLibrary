package ua.khai.slynko.library.db;

import org.apache.log4j.Logger;
import ua.khai.slynko.library.constant.Constants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBUtils {
  private static final Logger LOG = Logger.getLogger(DBUtils.class);

  private DBUtils() {}

  /**
   * Closes a connection.
   *
   * @param con
   *            Connection to be closed.
   */
  public static void close(Connection con) {
    if (con != null) {
      try {
        con.close();
      } catch (SQLException ex) {
        LOG.error(Constants.Messages.ERR_CANNOT_CLOSE_CONNECTION, ex);
      }
    }
  }

  /**
   * Closes a statement object.
   */
  public static void close(Statement stmt) {
    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException ex) {
        LOG.error(Constants.Messages.ERR_CANNOT_CLOSE_STATEMENT, ex);
      }
    }
  }

  /**
   * Closes a result set object.
   */
  public static void close(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException ex) {
        LOG.error(Constants.Messages.ERR_CANNOT_CLOSE_RESULTSET, ex);
      }
    }
  }

  /**
   * Closes resources.
   */
  public static void close(Connection con, Statement stmt, ResultSet rs) {
    close(rs);
    close(stmt);
    close(con);
  }

  /**
   * Rollbacks a connection.
   *
   * @param con
   *            Connection to be rollbacked.
   */
  public static void rollback(Connection con) {
    if (con != null) {
      try {
        con.rollback();
      } catch (SQLException ex) {
        LOG.error("Cannot rollback transaction", ex);
      }
    }
  }
}
