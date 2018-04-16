package ua.khai.slynko.library.db.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.ConnectionManager;
import ua.khai.slynko.library.db.entity.Status;
import ua.khai.slynko.library.db.bean.CatalogItemRequestBean;
import ua.khai.slynko.library.db.bean.UserCatalogItemBean;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.exception.Messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ua.khai.slynko.library.constant.Constants.Query.SQL_CREATE_CATALOG_ITEM;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_CREATE_LIBRARY_CARD_ITEM_REQUEST;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_DECREMENT_CATALOG_ITEM_INSTANCES_BY_LIBRARY_CARD_ITEM_ID;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_ALL_CATALOG_ITEMS;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_CATALOG_ITEMS_BY_AUTHOR;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_CATALOG_ITEMS_BY_AUTHOR_AND_TITLE;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_CATALOG_ITEMS_BY_TITLE;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_CATALOG_ITEM_INSTANCES_NUMBER_BY_LIBRARY_CARD_ID;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_GET_CATALOG_ITEM_BEANS;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_GET_CATALOG_ITEM_BEANS_BY_STATUS;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_GET_CATALOG_ITEM_IDS_BY_USER_ID_AND_STATUS;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_INCREMENT_BOOKS_HAS_TAKEN_BY_EMAIL;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_INCREMENT_CATALOG_ITEM_INSTANCES_BY_LIBRARY_CARD_ITEM_ID;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_REMOVE_CATALOG_ITEM_BY_ID;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_UPDATE_CATALOG_ITEM;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_UPDATE_CATALOG_ITEM_REQUEST_BY_ID;
import static ua.khai.slynko.library.db.DBUtils.close;
import static ua.khai.slynko.library.db.DBUtils.rollback;

public class CatalogItemDao {
  private static final Logger LOG = Logger.getLogger(CatalogItemDao.class);


  /**
   * Returns all catalog items.
   *
   * @return List of catalog item entities.
   */
  public List<CatalogItem> findCatalogItems() throws DBException
  {
    List<CatalogItem> catalogItemsList = new ArrayList<>();
    Statement stmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(SQL_FIND_ALL_CATALOG_ITEMS);
      while (rs.next()) {
        catalogItemsList.add(extractCatalogItem(rs));
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      LOG.error(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEMS, ex);
      throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEMS, ex);
    } finally {
      close(con, stmt, rs);
    }
    return catalogItemsList;
  }


  /**
   * Returns catalog items by author.
   *
   * @return List of catalog item entities.
   */
  public List<CatalogItem> findCatalogItemsByAuthor(String author) throws DBException {
    List<CatalogItem> catalogItemsList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_FIND_CATALOG_ITEMS_BY_AUTHOR);
      pstmt.setString(1, "%" + author + "%");
      rs = pstmt.executeQuery();
      while (rs.next()) {
        catalogItemsList.add(extractCatalogItem(rs));
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      LOG.error(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEMS, ex);
      throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEMS, ex);
    } finally {
      close(con, pstmt, rs);
    }
    return catalogItemsList;
  }

  /**
   * Returns catalog items by title.
   *
   * @return List of catalog item entities.
   */
  public List<CatalogItem> findCatalogItemsByTitle(String title) throws DBException {
    List<CatalogItem> catalogItemsList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_FIND_CATALOG_ITEMS_BY_TITLE);
      pstmt.setString(1, "%" + title + "%");
      rs = pstmt.executeQuery();
      while (rs.next()) {
        catalogItemsList.add(extractCatalogItem(rs));
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      LOG.error(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEMS, ex);
      throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEMS, ex);
    } finally {
      close(con, pstmt, rs);
    }
    return catalogItemsList;
  }

  /**
   * Returns catalog items by author and title.
   *
   * @return List of catalog item entities.
   */
  public List<CatalogItem> findCatalogItemsByAuthorAndTitle(String author, String title) throws DBException {
    List<CatalogItem> catalogItemsList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_FIND_CATALOG_ITEMS_BY_AUTHOR_AND_TITLE);
      int k = 1;
      pstmt.setString(k++, "%" + author + "%");
      pstmt.setString(k++, "%" + title + "%");
      rs = pstmt.executeQuery();
      while (rs.next()) {
        catalogItemsList.add(extractCatalogItem(rs));
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      LOG.error(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEMS, ex);
      throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEMS, ex);
    } finally {
      close(con, pstmt, rs);
    }
    return catalogItemsList;
  }

  /**
   * Create subscription request.
   *
   * @param itemId
   *            itemId to create.
   * @throws SQLException
   */
  private void sendCatalogItemRequest(Connection con, Integer libraryCardId, String itemId) throws SQLException {
    PreparedStatement pstmt = null;
    try {
      pstmt = con.prepareStatement(SQL_CREATE_LIBRARY_CARD_ITEM_REQUEST);
      int k = 1;
      pstmt.setInt(k++, libraryCardId);
      pstmt.setInt(k++, Integer.parseInt(itemId));
      pstmt.executeUpdate();
    } finally {
      close(pstmt);
    }
  }


  /**
   * Update (increment) catalog item instances by id .
   *
   * @param itemId
   *            itemId to remove.
   * @throws SQLException
   */
  public void incrementCatalogItemInstancesByLibraryCardItemId(Connection con, Long itemId) throws SQLException {
    PreparedStatement pstmt = null;
    try {
      pstmt = con.prepareStatement(SQL_INCREMENT_CATALOG_ITEM_INSTANCES_BY_LIBRARY_CARD_ITEM_ID);
      pstmt.setLong(1, itemId);
      pstmt.executeUpdate();
    } finally {
      close(pstmt);
    }
  }


  public void incrementBooksHasTakenNumberByEmail(Connection con, String userEmail) throws SQLException {
    PreparedStatement pstmt = null;
    try {
      LOG.debug("userEmail --> " + userEmail);
      pstmt = con.prepareStatement(SQL_INCREMENT_BOOKS_HAS_TAKEN_BY_EMAIL);
      pstmt.setString(1, userEmail);
      pstmt.executeUpdate();
    } finally {
      close(pstmt);
    }
  }

  /**
   * Update (decrement) catalog item instances by id .
   *
   * @param itemId
   *            itemId to remove.
   * @throws SQLException
   */
  public void decrementCatalogItemInstancesByLibraryCardItemId(Connection con, Long itemId) throws SQLException {
    PreparedStatement pstmt = null;
    try {
      pstmt = con.prepareStatement(SQL_DECREMENT_CATALOG_ITEM_INSTANCES_BY_LIBRARY_CARD_ITEM_ID);
      pstmt.setLong(1, itemId);
      pstmt.executeUpdate();
    } finally {
      close(pstmt);
    }
  }

  /**
   * Remove catalog item by id
   *
   * @param itemId
   *            itemId to remove.
   * @throws DBException
   */
  public void removeCatalogItem(Long itemId) throws DBException {
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_REMOVE_CATALOG_ITEM_BY_ID);
      pstmt.setLong(1, itemId);
      pstmt.executeUpdate();
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      throw new DBException(Messages.ERR_CANNOT_REMOVE_CATALOG_ITEM, ex);
    } finally {
      close(pstmt);
      close(con);
    }
  }

  /**
   * Update catalog item.
   *
   * @param catalogItem
   *            catalog item to update.
   * @throws DBException
   */
  public void updateCatalogItem(CatalogItem catalogItem) throws DBException {
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_UPDATE_CATALOG_ITEM);
      int k = 1;
      pstmt.setString(k++, catalogItem.getTitle());
      pstmt.setString(k++, catalogItem.getAuthor());
      pstmt.setString(k++, catalogItem.getEdition());
      pstmt.setInt(k++, catalogItem.getPublicationYear());
      pstmt.setInt(k++, catalogItem.getInstancesNumber());
      pstmt.setLong(k++, catalogItem.getId());
      pstmt.executeUpdate();
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      throw new DBException(Messages.ERR_CANNOT_UPDATE_CATALOG_ITEM, ex);
    } finally {
      close(pstmt);
      close(con);
    }
  }

  /**
   * Create catalog item.
   *
   * @param catalogItem
   *            catalog item to update.
   * @throws DBException
   */
  public void createCatalogItem(CatalogItem catalogItem) throws DBException {
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_CREATE_CATALOG_ITEM);
      int k = 1;
      pstmt.setString(k++, catalogItem.getTitle());
      pstmt.setString(k++, catalogItem.getAuthor());
      pstmt.setString(k++, catalogItem.getEdition());
      pstmt.setInt(k++, catalogItem.getPublicationYear());
      pstmt.setInt(k++, catalogItem.getInstancesNumber());
      pstmt.executeUpdate();
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      throw new DBException(Messages.ERR_CANNOT_CREATE_CATALOG_ITEM, ex);
    } finally {
      close(pstmt);
      close(con);
    }
  }

  /**
   * Returns catalog item bean list.
   *
   * @return List of catalog items.
   */
  public List<UserCatalogItemBean> getUserCatalogItemBeansByStatusId(Long userId, Integer statusId)
      throws DBException {
    List<UserCatalogItemBean> orderUserBeanList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_GET_CATALOG_ITEM_BEANS);
      int k = 1;
      pstmt.setLong(k++, userId);
      pstmt.setInt(k++, statusId);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        orderUserBeanList.add(extractUserCatalogItemBean(rs));
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_CATALOG_ITEM_BEANS, ex);
      throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_CATALOG_ITEM_BEANS, ex);
    } finally {
      close(con, pstmt, rs);
    }
    return orderUserBeanList;
  }

  /**
   * Returns list book requests.
   *
   * @param statusId
   *            Status identifier.
   * @return List of order entities.
   */
  public List<CatalogItemRequestBean> findCatalogItemRequests(int statusId) throws DBException {
    List<CatalogItemRequestBean> catalogItemRequestsList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_GET_CATALOG_ITEM_BEANS_BY_STATUS);
      pstmt.setInt(1, statusId);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        catalogItemRequestsList.add(extractCatalogItemRequestBean(rs));
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEM_BEANS_BY_STATUS_ID, ex);
    } finally {
      close(con, pstmt, rs);
    }
    return catalogItemRequestsList;
  }

  // //////////////////////////////////////////////////////////
  // Entity access methods
  // //////////////////////////////////////////////////////////
  /**
   * Returns requested catalog item ids.
   *
   * @return List of catalog item ids entities.
   */
  public List<String> findCatalogItemIdsByUserIdAndStatusId(Long userId, Integer statusId) throws DBException {
    List<String> itemIdsList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_GET_CATALOG_ITEM_IDS_BY_USER_ID_AND_STATUS);
      int k = 1;
      pstmt.setLong(k++, userId);
      pstmt.setInt(k++, statusId);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        itemIdsList.add(rs.getString(Constants.Fields.LIBRARY_CARD_ITEM_CATALOG_ITEM_ID));
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      LOG.error(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEM_IDS, ex);
      throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEM_IDS, ex);
    } finally {
      close(con, pstmt, rs);
    }
    return itemIdsList;
  }

  /**
   * Returns a number of catalog item with the given identifier.
   *
   * @param id
   *            User identifier.
   * @return User entity.
   * @throws DBException
   */
  public int getCatalogItemInstancesByLibraryCardId(long itemId) throws DBException {
    int instancesNumber = 0;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_FIND_CATALOG_ITEM_INSTANCES_NUMBER_BY_LIBRARY_CARD_ID);
      pstmt.setLong(1, itemId);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        instancesNumber = rs.getInt(Constants.Fields.CATALOG_ITEM_INSTANCES_NUMBER);
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATALOG_ITEMS, ex);
    } finally {
      close(con, pstmt, rs);
    }
    return instancesNumber;
  }

  /**
   * Update catalog item request.
   *
   * @throws DBException
   */
  public void updateCatalogItemRequestDateFromDateToPenaltySizeById(Date dateFrom, Date dateTo, Integer penaltySize,
      Integer statusId, Long id, boolean doDecrement, String userEmail) throws DBException {
    Connection con = null;
    PreparedStatement pstmt = null;
    java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
    java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());
    try {
      con = ConnectionManager.getInstance().getConnection();
      pstmt = con.prepareStatement(SQL_UPDATE_CATALOG_ITEM_REQUEST_BY_ID);
      int k = 1;
      pstmt.setDate(k++, sqlDateFrom);
      pstmt.setDate(k++, sqlDateTo);
      pstmt.setInt(k++, penaltySize);
      pstmt.setInt(k++, statusId);
      pstmt.setLong(k++, id);
      pstmt.executeUpdate();
      if (doDecrement) {
        decrementCatalogItemInstancesByLibraryCardItemId(con, id);
      }
      incrementBooksHasTakenNumberByEmail(con, userEmail);
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      throw new DBException(Messages.ERR_CANNOT_CREATE_USER, ex);
    } finally {
      close(pstmt);
      close(con);
    }
  }


  /**
   * Create subscription request.
   *
   * @param itemId
   *            itemId to create.
   * @throws DBException
   */
  public void sendCatalogItemRequest(Long userId, List<String> itemId) throws DBException {
    Connection con = null;
    Integer libraryCardId = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      libraryCardId = new LibraryCardDao().findUserLibraryCardId(con, userId);
      for (String s : itemId) {
        sendCatalogItemRequest(con, libraryCardId, s);
      }
      con.commit();
    } catch (SQLException ex) {
      rollback(con);
      throw new DBException(Messages.ERR_CANNOT_CREATE_CATALOG_ITEM_REQUEST, ex);
    } finally {
      close(con);
    }
  }


  public List<CatalogItem> getListCatalogItems(String author, String title) throws DBException {
    List<CatalogItem> catalogItems = null;
    if (StringUtils.isEmpty(author) && StringUtils.isEmpty(title)) {
      catalogItems = findCatalogItems();
    } else if (StringUtils.isEmpty(author) && !StringUtils.isEmpty(title)) {
      catalogItems = findCatalogItemsByTitle(title);
    } else if (!StringUtils.isEmpty(author) && (StringUtils.isEmpty(title))) {
      catalogItems = findCatalogItemsByAuthor(author);
    } else if (!StringUtils.isEmpty(author) && !StringUtils.isEmpty(title)) {
      catalogItems = findCatalogItemsByAuthorAndTitle(author, title);
    }
    return catalogItems;
  }

  public List<UserCatalogItemBean> findCatalogItemsBy(Long userId, String criteria) throws DBException {
    List<UserCatalogItemBean> catalogItemBeanList = new ArrayList<>();
    ConnectionManager dbManager = ConnectionManager.getInstance();
    if (criteria == null || criteria.equals("notConfirmed")) {
      catalogItemBeanList = getUserCatalogItemBeansByStatusId(userId, Status.NOT_CONFIRMED.getValue());
    } else if (criteria.equals("libraryCard")) {
      catalogItemBeanList = getUserCatalogItemBeansByStatusId(userId, Status.LIBRARY_CARD.getValue());
    }
    return catalogItemBeanList;
  }

  /**
   * Extracts a catalog item from the result set.
   *
   * @param rs
   *            Result set from which a catalog item entity will be extracted.
   * @return CatalogItem item entity.
   */
  private CatalogItem extractCatalogItem(ResultSet rs) throws SQLException {
    CatalogItem catalogItem = new CatalogItem();
    catalogItem.setId(rs.getLong(Constants.Fields.ENTITY_ID));
    catalogItem.setTitle(rs.getString(Constants.Fields.CATALOG_ITEM_TITLE));
    catalogItem.setAuthor(rs.getString(Constants.Fields.CATALOG_ITEM_AUTHOR));
    catalogItem.setEdition(rs.getString(Constants.Fields.CATALOG_ITEM_EDITION));
    catalogItem.setPublicationYear(rs.getInt(Constants.Fields.CATALOG_ITEM_PUBLICATION_YEAR));
    catalogItem.setInstancesNumber(rs.getInt(Constants.Fields.CATALOG_ITEM_INSTANCES_NUMBER));
    return catalogItem;
  }

  /**
   * Extracts a user catalog item bean from the result set.
   *
   * @param rs
   *            Result set from which a user catalog item bean will be
   *            extracted.
   * @return UserCatalogItemBean object
   */

  private UserCatalogItemBean extractUserCatalogItemBean(ResultSet rs) throws SQLException {
    UserCatalogItemBean bean = new UserCatalogItemBean();
    bean.setId(rs.getLong(Constants.Fields.USER_CATALOG_ITEM_BEAN_LIBRARY_CARD_ITEMS_ID));
    bean.setTitle(rs.getString(Constants.Fields.USER_CATALOG_ITEM_BEAN_TITLE));
    bean.setAuthor(rs.getString(Constants.Fields.USER_CATALOG_ITEM_BEAN_AUTHOR));
    bean.setDateTo(rs.getDate(Constants.Fields.USER_CATALOG_ITEM_BEAN_DATE_TO));
    bean.setPenaltySize(rs.getInt(Constants.Fields.USER_CATALOG_ITEM_BEAN_PENALTY_SIZE));
    return bean;
  }

  /**
   * Extracts catalog item request bean from the result set.
   *
   * @param rs
   *            Result set from which an catalog item request bean will be
   *            extracted.
   * @return
   */
  private CatalogItemRequestBean extractCatalogItemRequestBean(ResultSet rs) throws SQLException {
    CatalogItemRequestBean catalogIremRequestBean = new CatalogItemRequestBean();
    catalogIremRequestBean.setId(rs.getLong(Constants.Fields.CATALOG_ITEM_REQUEST_BEAN_LIBRARY_CARD_ITEMS_ID));
    catalogIremRequestBean.setTitle(rs.getString(Constants.Fields.CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_TITLE));
    catalogIremRequestBean.setAuthor(rs.getString(Constants.Fields.CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_AUTHOR));
    catalogIremRequestBean.setFirstName(rs.getString(Constants.Fields.CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_FIRST_NAME));
    catalogIremRequestBean.setLastName(rs.getString(Constants.Fields.CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_LAST_NAME));
    catalogIremRequestBean.setUserEmail(rs.getString(Constants.Fields.CATALOG_ITEM_REQUEST_BEAN_USER_EMAIL));

    return catalogIremRequestBean;
  }

}
