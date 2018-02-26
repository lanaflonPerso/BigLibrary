package ua.khai.slynko.library.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ua.khai.slynko.library.db.bean.CatalogItemRequestBean;
import ua.khai.slynko.library.db.bean.UserCatalogItemBean;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.db.entity.LibraryCardItem;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.exception.Messages;

/**
 * DB manager. Works with Apache Derby DB. Only the required DAO methods are
 * defined!
 * 
 * @author O.Slynko
 * 
 */
public final class DBManager {
	private static final Logger LOG = Logger.getLogger(DBManager.class);

	// //////////////////////////////////////////////////////////
	// singleton
	// //////////////////////////////////////////////////////////

	private static DBManager instance;

	/**
	 * Get instance method
	 * 
	 * @return instance of DBManager
	 * @throws DBException
	 */
	public static synchronized DBManager getInstance() throws DBException {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	private DBManager() throws DBException {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			// LIBRARYDB - the name of data source
			ds = (DataSource) envContext.lookup("jdbc/LIBRARYDB");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
		}
	}

	private DataSource ds;

	// //////////////////////////////////////////////////////////
	// SQL queries
	// //////////////////////////////////////////////////////////

	// -- users
	private static final String SQL_CREATE_USER = "INSERT INTO users VALUES(DEFAULT, ?, ?, ?, ?, ?, DEFAULT, DEFAULT, ?)";

	private static final String SQL_REMOVE_USER_BY_ID = "DELETE FROM users WHERE id=?";

	private static final String SQL_FIND_USER_BY_ROLE = "SELECT * FROM users WHERE role_id=?";

	private static final String SQL_FIND_USER_BY_ROLE_AND_FIRST_NAME = "SELECT * FROM users WHERE role_id=? AND first_name LIKE ?";

	private static final String SQL_FIND_USER_BY_ROLE_AND_LAST_NAME = "SELECT * FROM users WHERE role_id=? AND last_name LIKE ?";

	private static final String SQL_FIND_USER_BY_ROLE_AND_FIRST_NAME_AND_LAST_NAME = "SELECT * FROM users WHERE role_id=? AND first_name LIKE ? AND last_name LIKE ?";

	private static final String SQL_SET_USERS_LOCALE = "UPDATE users SET locale_id=? WHERE id=?";

	private static final String SQL_FIND_USER_BY_LOGIN_TO_UPDATE = "SELECT * FROM users WHERE login=? AND id!=?";

	private static final String SQL_FIND_USER_BY_EMAIL_TO_UPDATE = "SELECT * FROM users WHERE email=? AND id!=?";

	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";

	private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email=?";

	private static final String SQL_UPDATE_USER = "UPDATE users SET password=?, login=?, first_name=?, last_name=?, email=? "
			+ "	WHERE id=?";

	private static final String SQL_UPDATE_USER_PASSWORD_BY_EMAIL = "UPDATE users SET password=? WHERE email=?";

	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id=?";

	// -- library_cards
	private static final String SQL_CREATE_USER_LIBRARY_CARD = "INSERT INTO library_cards VALUES(DEFAULT, ?)";

	// -- catalog_items
	private static final String SQL_FIND_ALL_CATALOG_ITEMS = "SELECT * FROM catalog_items";

	private static final String SQL_FIND_CATALOG_ITEMS_BY_AUTHOR = "SELECT * FROM catalog_items WHERE author LIKE ?";

	private static final String SQL_FIND_CATALOG_ITEMS_BY_TITLE = "SELECT * FROM catalog_items WHERE title LIKE ?";

	private static final String SQL_FIND_CATALOG_ITEMS_BY_AUTHOR_AND_TITLE = "SELECT * FROM catalog_items WHERE author LIKE ? AND title LIKE ?";

	private static final String SQL_CREATE_LIBRARY_CARD_ITEM_REQUEST = "INSERT INTO library_card_items VALUES(DEFAULT, ?, ?, NULL, NULL, NULL, DEFAULT)";

	private static final String SQL_INCREMENT_CATALOG_ITEM_INSTANCES_BY_LIBRARY_CARD_ITEM_ID = "UPDATE  catalog_items ci SET instances_number = (SELECT instances_number + 1 FROM catalog_items WHERE id = ci.id) WHERE ci.id = (SELECT catalog_item_id FROM library_card_items WHERE id = ?)";
	
	private static final String SQL_INCREMENT_BOOKS_HAS_TAKEN_BY_EMAIL = "UPDATE  users SET books_has_taken = (SELECT books_has_taken + 1 FROM users WHERE email = ?)";

	private static final String SQL_DECREMENT_CATALOG_ITEM_INSTANCES_BY_LIBRARY_CARD_ITEM_ID = "UPDATE  catalog_items ci SET instances_number = (SELECT instances_number - 1 FROM catalog_items WHERE id = ci.id) WHERE ci.id = (SELECT catalog_item_id FROM library_card_items WHERE id = ?)";

	private static final String SQL_REMOVE_CATALOG_ITEM_BY_ID = "DELETE FROM catalog_items WHERE id=?";

	private static final String SQL_UPDATE_CATALOG_ITEM = "UPDATE catalog_items SET title=?, author=?, edition=?, publication_year=?, instances_number=? "
			+ "WHERE id=?";

	private static final String SQL_CREATE_CATALOG_ITEM = "INSERT INTO catalog_items VALUES(DEFAULT, ?, ?, ?, ?, ?)";

	// -- library_cards
	private static final String SQL_FIND_USER_LIBRARY_CARD_ID = "SELECT library_cards.id FROM library_cards WHERE user_id=?";

	// -- library_card_items
	private static final String SQL_REMOVE_LIBRARY_CARD_ITEM_BY_ID = "DELETE FROM library_card_items WHERE id=?";

	private static final String SQL_UPDATE_LIBRARY_CARD_ITEM_STATUS_ID = "UPDATE library_card_items SET status_id=? WHERE id=?";

	private static final String SQL_GET_LIBRARY_CARD_BY_LIBRARY_CARD_ITEM_ID_AND_CATALOG_ITEM_ID = "SELECT * FROM library_card_items WHERE subscription_id=? AND catalog_id=?";

	// -- users_blocked
	private static final String SQL_CREATE_BLOCKED_USER = "INSERT INTO users_blocked VALUES(DEFAULT, ?)";

	private static final String SQL_REMOVE_BLOCKED_USER = "DELETE FROM users_blocked WHERE user_id=?";

	private static final String SQL_IS_USER_BLOCKED = "SELECT * FROM users_blocked WHERE user_id=?";

	// -- other
	private static final String SQL_GET_CATALOG_ITEM_IDS_BY_USER_ID_AND_STATUS = "SELECT lci.catalog_item_id FROM library_card_items lci, library_cards lc, users u WHERE lci.library_card_id=lc.id AND u.id=lc.user_id AND u.id=? AND status_id=?";

	private static final String SQL_GET_CATALOG_ITEM_BEANS_BY_STATUS = "SELECT lci.id, ci.title, ci.author, u.first_name, u.last_name, u.email "
			+ "FROM library_card_items lci, catalog_items ci, statuses s, library_cards lc, users u "
			+ "WHERE lci.status_id=s.id AND lci.catalog_item_id=ci.id AND lci.library_card_id=lc.id AND lc.user_id=u.id AND s.id=?";

	private static final String SQL_GET_CATALOG_ITEM_BEANS = "SELECT lci.id, ci.title, ci.author,  lci.date_to, lci.penalty_size "
			+ "FROM library_card_items lci, catalog_items ci, statuses s, library_cards lc, users u WHERE lci.status_id=s.id AND lci.catalog_item_id=ci.id AND lci.library_card_id=lc.id AND lc.user_id=u.id AND u.id=? AND s.id=?";

	private static final String SQL_FIND_CATALOG_ITEM_INSTANCES_NUMBER_BY_LIBRARY_CARD_ID = "SELECT ci.instances_number "
			+ "FROM library_card_items lci, catalog_items ci " + "WHERE lci.catalog_item_id=ci.id AND lci.id=?";

	private static final String SQL_UPDATE_CATALOG_ITEM_REQUEST_BY_ID = "UPDATE library_card_items SET date_from=?, date_to=?, penalty_size=?, status_id=? "
			+ "WHERE id=?";

	/**
	 * Returns a DB connection from the Pool Connections. Before using this
	 * method you must configure the Date Source and the Connections Pool in
	 * your WEB_APP_ROOT/META-INF/context.xml file.
	 * 
	 * @return DB connection.
	 */
	public Connection getConnection() throws DBException {
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
		}
		return con;
	}

	// //////////////////////////////////////////////////////////s
	// Methods to obtain beans
	// //////////////////////////////////////////////////////////
	/**
	 * Returns catalog item bean list.
	 * 
	 * @return List of catalog items.
	 */
	public List<UserCatalogItemBean> getUserCatalogItemBeansByStatusId(Long userId, Integer statusId)
			throws DBException {
		List<UserCatalogItemBean> orderUserBeanList = new ArrayList<UserCatalogItemBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
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
		List<CatalogItemRequestBean> catalogItemRequestsList = new ArrayList<CatalogItemRequestBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
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
		List<String> itemIdsList = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_GET_CATALOG_ITEM_IDS_BY_USER_ID_AND_STATUS);
			int k = 1;
			pstmt.setLong(k++, userId);
			pstmt.setInt(k++, statusId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				itemIdsList.add(rs.getString(Fields.LIBRARY_CARD_ITEM_CATALOG_ITEM_ID));
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

	public List<User> findUsers(Integer roleId, String firstName, String lastName) throws DBException	{
		List<User> users = null;
		if (StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName)) {
			users = findUsersByRole(roleId);
		} else if (StringUtils.isEmpty(firstName) && !StringUtils.isEmpty(lastName)) {
			users = findUsersByRoleAndLastName(roleId, lastName);
		} else if (!StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName)) {
			users = findUsersByRoleAndFirstName(roleId, firstName);
		} else if (!StringUtils.isEmpty(firstName) && !StringUtils.isEmpty(lastName)) {
			users = findUsersByRoleAndFirstNameAndLastName(roleId, firstName, lastName);
		}
		return users;
	}

	/**
	 * Returns users by role.
	 * 
	 * @return List of users.
	 */
	public List<User> findUsersByRole(Integer roleId) throws DBException {
		List<User> usersList = new ArrayList<User>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_ROLE);
			int k = 1;
			pstmt.setInt(k++, roleId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usersList.add(extractUser(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return usersList;
	}

	/**
	 * Returns users by role and first name.
	 * 
	 * @return List of users.
	 */
	public List<User> findUsersByRoleAndFirstName(Integer roleId, String firstName) throws DBException {
		List<User> usersList = new ArrayList<User>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_ROLE_AND_FIRST_NAME);
			int k = 1;
			pstmt.setInt(k++, roleId);
			pstmt.setString(k++, "%" + firstName + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usersList.add(extractUser(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return usersList;
	}

	/**
	 * Returns users by role and last name.
	 * 
	 * @return List of users.
	 */
	public List<User> findUsersByRoleAndLastName(Integer roleId, String lastName) throws DBException {
		List<User> usersList = new ArrayList<User>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_ROLE_AND_LAST_NAME);
			int k = 1;
			pstmt.setInt(k++, roleId);
			pstmt.setString(k++, "%" + lastName + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usersList.add(extractUser(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return usersList;
	}

	/**
	 * Returns users by role, first name and last name.
	 * 
	 * @return List of users.
	 */
	public List<User> findUsersByRoleAndFirstNameAndLastName(Integer roleId, String firstName, String lastName)
			throws DBException {
		List<User> usersList = new ArrayList<User>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_ROLE_AND_FIRST_NAME_AND_LAST_NAME);
			int k = 1;
			pstmt.setInt(k++, roleId);
			pstmt.setString(k++, "%" + firstName + "%");
			pstmt.setString(k++, "%" + lastName + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usersList.add(extractUser(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return usersList;
	}

	/**
	 * Returns all catalog items.
	 * 
	 * @return List of catalog item entities.
	 */
	public List<CatalogItem> findCatalogItems() throws DBException {
		List<CatalogItem> catalogItemsList = new ArrayList<CatalogItem>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
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
		List<CatalogItem> catalogItemsList = new ArrayList<CatalogItem>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
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
		List<CatalogItem> catalogItemsList = new ArrayList<CatalogItem>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
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
		List<CatalogItem> catalogItemsList = new ArrayList<CatalogItem>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
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
	 * Find libraryCard by library_card_id and catalog_item_id.
	 * 
	 * @param itemId
	 *            itemId to create.
	 * @throws DBException
	 */
	public LibraryCardItem findLibraryCardItem(Long libraryCardId, Integer catalogItemId) throws DBException {
		LibraryCardItem libraryCardItem = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
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
	 * Returns a user with the given identifier.
	 * 
	 * @param id
	 *            User identifier.
	 * @return User entity.
	 * @throws DBException
	 */
	public User findUser(long id) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_ID);
			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
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
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_CATALOG_ITEM_INSTANCES_NUMBER_BY_LIBRARY_CARD_ID);
			pstmt.setLong(1, itemId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				instancesNumber = rs.getInt(Fields.CATALOG_ITEM_INSTANCES_NUMBER);
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
	 * Returns a user with the given login.
	 * 
	 * @param login
	 *            User login.
	 * @return User entity.
	 * @throws DBException
	 */
	public User findUserByLogin(String login) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			pstmt.setString(1, login);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
	}

	/**
	 * Returns a user with the given email.
	 * 
	 * @param login
	 *            User login.
	 * @return User entity.
	 * @throws DBException
	 */
	public User findUserByEmail(String email) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_EMAIL);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
	}

	/**
	 * Returns a user with the given login except the current user.
	 * 
	 * @param login
	 *            User login.
	 * @return User entity.
	 * @throws DBException
	 */
	public User findUserByLoginToUpdate(String login, Long userId) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN_TO_UPDATE);
			int k = 1;
			pstmt.setString(k++, login);
			pstmt.setLong(k++, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
	}

	/**
	 * Returns a user with the given email except the current user.
	 * 
	 * @param login
	 *            User login.
	 * @return User entity.
	 * @throws DBException
	 */
	public User findUserByEmailToUpdate(String login, Long userId) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_EMAIL_TO_UPDATE);
			int k = 1;
			pstmt.setString(k++, login);
			pstmt.setLong(k++, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_EMAIL, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
	}

	/**
	 * Update user.
	 * 
	 * @param user
	 *            user to update.
	 * @throws DBException
	 */
	public void updateUser(User user) throws DBException {
		Connection con = null;
		try {
			con = getConnection();
			updateUser(con, user);
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			close(con);
		}
	}

	/**
	 * Update user password by email.
	 * 
	 * @param password
	 *            password to update
	 * @param email
	 *            email to find
	 * @throws DBException
	 */
	public void updateUserPasswordByEmail(String password, String email) throws DBException {
		Connection con = null;
		try {
			con = getConnection();
			updateUserPasswordByEmail(con, password, email);
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER_PASSWORD, ex);
		} finally {
			close(con);
		}
	}

	/**
	 * Block user.
	 * 
	 * @param user
	 *            user to update.
	 * @throws DBException
	 */
	public void blockUser(Long userId) throws DBException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_CREATE_BLOCKED_USER);
			pstmt.setLong(1, userId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_BLOCK_USER, ex);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	/**
	 * Block user.
	 * 
	 * @param user
	 *            user to update.
	 * @throws DBException
	 */
	public void unblockUser(Long userId) throws DBException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_REMOVE_BLOCKED_USER);
			pstmt.setLong(1, userId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UNBLOCK_USER, ex);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	/**
	 * Is user blocked.
	 * 
	 * @param user
	 *            user to update.
	 * @throws DBException
	 */
	public boolean isUserBlocked(Long userId) throws DBException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isBlocked = false;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_IS_USER_BLOCKED);
			int k = 1;
			pstmt.setLong(k, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isBlocked = true;
			}
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_CHECK_IF_USER_IS_BLOCKED, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return isBlocked;
	}

	/**
	 * Create reader.
	 * 
	 * @param reader
	 *            user to create.
	 * @throws DBException
	 */
	public void createReader(User user) throws DBException {
		Connection con = null;
		try {
			con = getConnection();
			Integer userId = createUser(con, user);
			createUserLibraryCard(con, userId);
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			System.out.println(ex);
			throw new DBException(Messages.ERR_CANNOT_CREATE_USER, ex);
		} finally {
			close(con);
		}
	}

	/**
	 * Create user.
	 * 
	 * @param user
	 *            user to create.
	 * @throws DBException
	 */
	public void createUser(User user) throws DBException {
		Connection con = null;
		try {
			con = getConnection();
			createUser(con, user);
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_CREATE_USER, ex);
		} finally {
			close(con);
		}
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
			con = getConnection();
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
			con = getConnection();
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
			con = getConnection();
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
			con = getConnection();
			libraryCardId = findUserLibraryCardId(con, userId);
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

	/**
	 * Cancel book request (remove library card item).
	 * 
	 * @param itemId
	 *            itemId to remove request.
	 * @throws DBException
	 */
	public void removeLibraryCardItemById(List<String> itemIds) throws DBException {
		Connection con = null;
		try {
			con = getConnection();
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
	 * @param itemId
	 *            itemId to create.
	 * @throws DBException
	 */
	public void updateLibraryCardsItemIds(List<String> itemIds, Integer statusId) throws DBException {
		Connection con = null;
		try {
			con = getConnection();
			for (String s : itemIds) {
				updateLibraryCardsItemIds(con, statusId, s);
				incrementCatalogItemInstancesByLibraryCardItemId(con, Long.parseLong(s));
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
			con = getConnection();
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
	 * Remove user by id
	 * 
	 * @param userId
	 *            userId to remove.
	 * @throws DBException
	 */
	public void removeUserById(Long userId) throws DBException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_REMOVE_USER_BY_ID);
			pstmt.setLong(1, userId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_REMOVE_USER, ex);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	/**
	 * Set user's locale
	 * 
	 * @param localeId
	 *            locale to set.
	 * @throws DBException
	 */
	public void setUserLocale(Integer localeId, Long userId) throws DBException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_SET_USERS_LOCALE);
			int k = 1;
			pstmt.setInt(k++, localeId);
			pstmt.setLong(k++, userId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_SET_LOCALE, ex);
		} finally {
			close(pstmt);
			close(con);
		}
	}

	// //////////////////////////////////////////////////////////
	// Entity access methods (for transactions)
	// //////////////////////////////////////////////////////////
	/**
	 * Find user library card id.
	 * 
	 * @param userId
	 *            userId to find.
	 * @throws DBException
	 * @throws SQLException
	 */
	private Integer findUserLibraryCardId(Connection con, Long userId) throws SQLException {
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
	 * Update (increment) catalog item instances by id .
	 * 
	 * @param itemId
	 *            itemId to remove.
	 * @throws SQLException
	 */
	private void incrementCatalogItemInstancesByLibraryCardItemId(Connection con, Long itemId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(SQL_INCREMENT_CATALOG_ITEM_INSTANCES_BY_LIBRARY_CARD_ITEM_ID);
			pstmt.setLong(1, itemId);
			pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
	}
	
	private void incrementBooksHasTakenNumberByEmail(Connection con, String userEmail) throws SQLException {
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
	private void decrementCatalogItemInstancesByLibraryCardItemId(Connection con, Long itemId) throws SQLException {
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
	 * Update user.
	 * 
	 * @param user
	 *            user to update.
	 * @throws SQLException
	 */
	private void updateUser(Connection con, User user) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(SQL_UPDATE_USER);
			int k = 1;
			pstmt.setString(k++, user.getPassword());
			pstmt.setString(k++, user.getLogin());
			pstmt.setString(k++, user.getFirstName());
			pstmt.setString(k++, user.getLastName());
			pstmt.setString(k++, user.getEmail());
			pstmt.setLong(k, user.getId());
			pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
	}

	/**
	 * Update user password by email.
	 * 
	 * @param password
	 *            password to update
	 * @param email
	 *            email to find
	 * @throws SQLException
	 */
	private void updateUserPasswordByEmail(Connection con, String password, String email) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(SQL_UPDATE_USER_PASSWORD_BY_EMAIL);
			int k = 1;
			pstmt.setString(k++, password);
			pstmt.setString(k++, email);
			pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
	}

	/**
	 * Create user.
	 * 
	 * @param user
	 *            user to update.
	 * @throws SQLException
	 */
	private Integer createUser(Connection con, User user) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Integer userId = null;
		try {
			pstmt = con.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setString(k++, user.getLogin());
			pstmt.setString(k++, user.getPassword());
			pstmt.setString(k++, user.getFirstName());
			pstmt.setString(k++, user.getLastName());
			pstmt.setString(k++, user.getEmail());
			pstmt.setInt(k++, user.getRoleId());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				userId = rs.getInt(1);
			}
		} finally {
			close(rs);
			close(pstmt);
		}
		return userId;
	}

	/**
	 * Create user's library card.
	 * 
	 * @param user
	 *            user to update.
	 * @throws SQLException
	 */
	private void createUserLibraryCard(Connection con, Integer userId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(SQL_CREATE_USER_LIBRARY_CARD);
			pstmt.setInt(1, userId);
			pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
	}

	// //////////////////////////////////////////////////////////
	// DB util methods
	// //////////////////////////////////////////////////////////

	/**
	 * Closes a connection.
	 * 
	 * @param con
	 *            Connection to be closed.
	 */
	private void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, ex);
			}
		}
	}

	/**
	 * Closes a statement object.
	 */
	private void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
				LOG.error(Messages.ERR_CANNOT_CLOSE_STATEMENT, ex);
			}
		}
	}

	/**
	 * Closes a result set object.
	 */
	private void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				LOG.error(Messages.ERR_CANNOT_CLOSE_RESULTSET, ex);
			}
		}
	}

	/**
	 * Closes resources.
	 */
	private void close(Connection con, Statement stmt, ResultSet rs) {
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
	private void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException ex) {
				LOG.error("Cannot rollback transaction", ex);
			}
		}
	}

	// //////////////////////////////////////////////////////////
	// Other methods
	// //////////////////////////////////////////////////////////
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
		bean.setId(rs.getLong(Fields.USER_CATALOG_ITEM_BEAN_LIBRARY_CARD_ITEMS_ID));
		bean.setTitle(rs.getString(Fields.USER_CATALOG_ITEM_BEAN_TITLE));
		bean.setAuthor(rs.getString(Fields.USER_CATALOG_ITEM_BEAN_AUTHOR));
		bean.setDateTo(rs.getDate(Fields.USER_CATALOG_ITEM_BEAN_DATE_TO));
		bean.setPenaltySize(rs.getInt(Fields.USER_CATALOG_ITEM_BEAN_PENALTY_SIZE));
		return bean;
	}

	/**
	 * Extracts a user entity from the result set.
	 * 
	 * @param rs
	 *            Result set from which a user entity will be extracted.
	 * @return User entity
	 */
	private User extractUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getLong(Fields.ENTITY_ID));
		user.setLogin(rs.getString(Fields.USER_LOGIN));
		user.setPassword(rs.getString(Fields.USER_PASSWORD));
		user.setFirstName(rs.getString(Fields.USER_FIRST_NAME));
		user.setLastName(rs.getString(Fields.USER_LAST_NAME));
		user.setEmail(rs.getString(Fields.USER_EMAIL));
		user.setLocale(Locale.values()[rs.getInt(Fields.USER_LOCALE_ID) - 1].toString());
		user.setRoleId(rs.getInt(Fields.USER_ROLE_ID));
		user.setBooksHasTaken(rs.getInt("books_has_taken"));
		return user;
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
		libraryCardItem.setId(rs.getLong(Fields.ENTITY_ID));
		libraryCardItem.setLibraryCardId(rs.getInt(Fields.LIBRARY_CARD_ITEM_LIBRARY_CARD_ID));
		libraryCardItem.setCatalogItemId(rs.getInt(Fields.LIBRARY_CARD_ITEM_CATALOG_ITEM_ID));
		libraryCardItem.setDateFrom(rs.getDate(Fields.LIBRARY_CARD_ITEM_DATE_FROM));
		libraryCardItem.setDateTo(rs.getDate(Fields.LIBRARY_CARD_ITEM_DATE_TO));
		libraryCardItem.setPenaltySize(rs.getInt(Fields.LIBRARY_CARD_ITEM_PENALTY_SIZE));
		libraryCardItem.setStatusId(rs.getInt(Fields.LIBRARY_CARD_ITEM_STATUS_ID));
		return libraryCardItem;
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
		catalogIremRequestBean.setId(rs.getLong(Fields.CATALOG_ITEM_REQUEST_BEAN_LIBRARY_CARD_ITEMS_ID));
		catalogIremRequestBean.setTitle(rs.getString(Fields.CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_TITLE));
		catalogIremRequestBean.setAuthor(rs.getString(Fields.CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_AUTHOR));
		catalogIremRequestBean.setFirstName(rs.getString(Fields.CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_FIRST_NAME));
		catalogIremRequestBean.setLastName(rs.getString(Fields.CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_LAST_NAME));
		catalogIremRequestBean.setUserEmail(rs.getString(Fields.CATALOG_ITEM_REQUEST_BEAN_USER_EMAIL));
		
		return catalogIremRequestBean;
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
		catalogItem.setId(rs.getLong(Fields.ENTITY_ID));
		catalogItem.setTitle(rs.getString(Fields.CATALOG_ITEM_TITLE));
		catalogItem.setAuthor(rs.getString(Fields.CATALOG_ITEM_AUTHOR));
		catalogItem.setEdition(rs.getString(Fields.CATALOG_ITEM_EDITION));
		catalogItem.setPublicationYear(rs.getInt(Fields.CATALOG_ITEM_PUBLICATION_YEAR));
		catalogItem.setInstancesNumber(rs.getInt(Fields.CATALOG_ITEM_INSTANCES_NUMBER));
		return catalogItem;
	}

}