package ua.khai.slynko.library.db.dao;

import org.apache.log4j.Logger;
import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.ConnectionManager;
import ua.khai.slynko.library.db.entity.LibraryCardItem;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.exception.Messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ua.khai.slynko.library.constant.Constants.Query.SQL_GET_LIBRARY_CARD_BY_LIBRARY_CARD_ITEM_ID_AND_CATALOG_ITEM_ID;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_REMOVE_LIBRARY_CARD_ITEM_BY_ID;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_UPDATE_LIBRARY_CARD_ITEM_STATUS_ID;
import static ua.khai.slynko.library.db.DBUtils.close;
import static ua.khai.slynko.library.db.DBUtils.rollback;

public class LibraryCardItemDao {
  private static final Logger LOG = Logger.getLogger(LibraryCardItemDao.class);

  /**
   * Remove library card item by id .
   *
   * @param itemId
   *            itemId to remove.
   * @throws SQLException
   */
  private void removeLibraryCardItemById(Connection con, String itemId) throws SQLException {
    PreparedStatement pstmt = null;
    try {
      pstmt = con.prepareStatement(SQL_REMOVE_LIBRARY_CARD_ITEM_BY_ID);
      pstmt.setInt(1, Integer.parseInt(itemId));
      pstmt.executeUpdate();
    } finally {
      close(pstmt);
    }
  }

  /**
   * Update library card item by id .
   *
   * @param itemId
   *            itemId to remove.
   * @throws SQLException
   */
  private void updateLibraryCardsItemIds(Connection con, Integer statusId, String itemId) throws SQLException {
    PreparedStatement pstmt = null;
    try {
      pstmt = con.prepareStatement(SQL_UPDATE_LIBRARY_CARD_ITEM_STATUS_ID);
      int k = 1;
      pstmt.setInt(k++, statusId);
      pstmt.setInt(k++, Integer.parseInt(itemId));
      pstmt.executeUpdate();
    } finally {
      close(pstmt);
    }
  }

  /**
   * Find libraryCard by library_card_id and catalog_item_id.
   *
   * @throws DBException
   */
  public LibraryCardItem findLibraryCardItem(Long libraryCardId, Integer catalogItemId) throws DBException {
    LibraryCardItem libraryCardItem = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_GET_LIBRARY_CARD_BY_LIBRARY_CARD_ITEM_ID_AND_CATALOG_ITEM_ID);
      int k = 1;
      pstmt.setLong(k++, libraryCardId);
      pstmt.setInt(k++, catalogItemId);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        libraryCardItem = extractLibraryCardItem(rs);
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      LOG.error(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEM_BEANS, ex);
      throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEM_BEANS, ex);
    } finally {
      close(con, pstmt, rs);
    }
    return libraryCardItem;
  }

  /**
   * Cancel book request (remove library card item).
   *
   * @param itemIds
   *            itemIds to remove request.
   * @throws DBException
   */
  public void removeLibraryCardItemById(List<String> itemIds) throws DBException {
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      for (String s : itemIds) {
        removeLibraryCardItemById(con, s);
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      throw new DBException(Messages.ERR_CANNOT_REMOVE_LIBRARY_CARD_ITEM, ex);
    } finally {
      close(con);
    }
  }

  /**
   * Update library card item id.
   *
   * @param itemIds
   *            itemId to create.
   * @throws DBException
   */
  public void updateLibraryCardsItemIds(List<String> itemIds, Integer statusId) throws DBException {
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      for (String s : itemIds) {
        updateLibraryCardsItemIds(con, statusId, s);
        new CatalogItemDao().incrementCatalogItemInstancesByLibraryCardItemId(con, Long.parseLong(s));
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      throw new DBException(Messages.ERR_CANNOT_REMOVE_UPDATE_CARD_ITEM, ex);
    } finally {
      close(con);
    }
  }



  /**
   * Extracts a library card item entity from the result set.
   *
   * @param rs
   *            Result set from which a user entity will be extracted.
   * @return User entity
   */
  private LibraryCardItem extractLibraryCardItem(ResultSet rs) throws SQLException {
    LibraryCardItem libraryCardItem = new LibraryCardItem();
    libraryCardItem.setId(rs.getLong(Constants.Fields.ENTITY_ID));
    libraryCardItem.setLibraryCardId(rs.getInt(Constants.Fields.LIBRARY_CARD_ITEM_LIBRARY_CARD_ID));
    libraryCardItem.setCatalogItemId(rs.getInt(Constants.Fields.LIBRARY_CARD_ITEM_CATALOG_ITEM_ID));
    libraryCardItem.setDateFrom(rs.getDate(Constants.Fields.LIBRARY_CARD_ITEM_DATE_FROM));
    libraryCardItem.setDateTo(rs.getDate(Constants.Fields.LIBRARY_CARD_ITEM_DATE_TO));
    libraryCardItem.setPenaltySize(rs.getInt(Constants.Fields.LIBRARY_CARD_ITEM_PENALTY_SIZE));
    libraryCardItem.setStatusId(rs.getInt(Constants.Fields.LIBRARY_CARD_ITEM_STATUS_ID));
    return libraryCardItem;
  }
}
