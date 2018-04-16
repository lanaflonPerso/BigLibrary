package ua.khai.slynko.library.db.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.db.ConnectionManager;
import ua.khai.slynko.library.db.entity.Locale;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.DBException;
import ua.khai.slynko.library.exception.Messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ua.khai.slynko.library.constant.Constants.Query.SQL_CREATE_BLOCKED_USER;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_CREATE_USER;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_BY_EMAIL;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_BY_EMAIL_TO_UPDATE;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_BY_ID;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_BY_LOGIN;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_BY_LOGIN_TO_UPDATE;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_BY_ROLE;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_BY_ROLE_AND_FIRST_NAME;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_BY_ROLE_AND_FIRST_NAME_AND_LAST_NAME;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_FIND_USER_BY_ROLE_AND_LAST_NAME;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_IS_USER_BLOCKED;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_REMOVE_BLOCKED_USER;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_REMOVE_USER_BY_ID;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_SET_USERS_LOCALE;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_UPDATE_USER;
import static ua.khai.slynko.library.constant.Constants.Query.SQL_UPDATE_USER_PASSWORD_BY_EMAIL;
import static ua.khai.slynko.library.db.DBUtils.close;
import static ua.khai.slynko.library.db.DBUtils.rollback;

public class UserDao {

  private static final Logger LOG = Logger.getLogger(UserDao.class);

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
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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

  /**
   * Returns users by role.
   *
   * @return List of users.
   */
  public List<User> findUsersByRole(Integer roleId) throws DBException {
    List<User> usersList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
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
    List<User> usersList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
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
    List<User> usersList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
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
    List<User> usersList = new ArrayList<>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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

  public void toggleUserBlockStatus(Long userId) throws DBException	{
    if (isUserBlocked(userId)) {
      unblockUser(userId);
    } else {
      blockUser(userId);
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
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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
      con = ConnectionManager.getInstance().getConnection();
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
   * Create reader.
   *
   * @param reader
   *            user to create.
   * @throws DBException
   */
  public void createReader(User user) throws DBException {
    Connection con = null;
    try {
      con = ConnectionManager.getInstance().getConnection();
      Integer userId = createUser(con, user);
      new LibraryCardDao().createUserLibraryCard(con, userId); //TODO: LibraryCardDao
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
      con = ConnectionManager.getInstance().getConnection();
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
   * Extracts a user entity from the result set.
   *
   * @param rs
   *            Result set from which a user entity will be extracted.
   * @return User entity
   */
  private User extractUser(ResultSet rs) throws SQLException {
    User user = new User();
    user.setId(rs.getLong(Constants.Fields.ENTITY_ID));
    user.setLogin(rs.getString(Constants.Fields.USER_LOGIN));
    user.setPassword(rs.getString(Constants.Fields.USER_PASSWORD));
    user.setFirstName(rs.getString(Constants.Fields.USER_FIRST_NAME));
    user.setLastName(rs.getString(Constants.Fields.USER_LAST_NAME));
    user.setEmail(rs.getString(Constants.Fields.USER_EMAIL));
    user.setLocale(Locale.values()[rs.getInt(Constants.Fields.USER_LOCALE_ID) - 1].toString());
    user.setRoleId(rs.getInt(Constants.Fields.USER_ROLE_ID));
    user.setBooksHasTaken(rs.getInt("books_has_taken"));
    return user;
  }
}
