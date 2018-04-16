package ua.khai.slynko.library.db.dao;

import org.apache.log4j.Logger;
import ua.khai.slynko.library.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.khai.slynko.library.constant.Constants.Query.SQL_CREATE_USER_LIBRARY_CARD;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_LIBRARY_CARD_ID;
import static ua.khai.slynko.library.db.DBUtils.close;

public class LibraryCardDao
{
  private static final Logger LOG = Logger.getLogger(LibraryCardDao.class);


  /**
   * Create user's library card.
   *
   * @param user
   *            user to update.
   * @throws SQLException
   */
  public void createUserLibraryCard(Connection con, Integer userId) throws SQLException {
    PreparedStatement pstmt = null;
    try {
      pstmt = con.prepareStatement(SQL_CREATE_USER_LIBRARY_CARD);
      pstmt.setInt(1, userId);
      pstmt.executeUpdate();
    } finally {
      close(pstmt);
    }
  }

  /**
   * Find user library card id.
   *
   * @param userId
   *            userId to find.
   * @throws DBException
   * @throws SQLException
   */
  public Integer findUserLibraryCardId(Connection con, Long userId) throws SQLException {
    Integer libraryCardId = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = con.prepareStatement(SQL_FIND_USER_LIBRARY_CARD_ID);
      pstmt.setLong(1, userId);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        libraryCardId = rs.getInt("id");
      }
    } finally {
      close(rs);
      close(pstmt);
    }
    return libraryCardId;
  }


}
