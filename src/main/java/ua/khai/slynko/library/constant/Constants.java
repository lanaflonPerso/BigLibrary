package ua.khai.slynko.library.constant;

public final class Constants {
    private Constants() {
    }

    public static final String TRUE = "true";
    public static final int STRING_MAX_LENGTH = 50;
    public static final int STRING_MIN_LENGTH = 1;
    public static final int PASSWORD_MAX_LENGTH = 20;
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int EMAIL_MAX_LENGTH = 30;
    public static final int EMAIL_MIN_LENGTH = 5;
    public static final int LOGIN_MAX_LENGTH = 20;
    public static final int LOGIN_MIN_LENGTH = 3;

    public static final String EMAIL_PATTERN = "^[a-zA-Z][a-zA-Z0-9\\.-]*\\@[a-zA-Z]+\\.[a-zA-Z]+$";
    public static final String DATE_PATTERN = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";

    /**
     * Path holder (jsp pages, controller commands).
     *
     * @author O.Slynko
     *
     */
    public static final class Path {
        private Path() {
        }

        public static final String PAGE_LOGIN_REDERECT = "login";
        public static final String PAGE_HOME_REDERECT = "home";
        // pages
        public static final String PAGE_LOGIN = "/login.jsp";
        public static final String PAGE_ERROR_PAGE = "/WEB-INF/jsp/error_page.jsp";
        public static final String PAGE_CHECK_PASSWORD = "/WEB-INF/jsp/check_password.jsp";
        public static final String PAGE_LIST_CATALOG = "/WEB-INF/jsp/reader/list_catalog.jsp";
        public static final String PAGE_LIST_CATALOG_ITEMS = "/WEB-INF/jsp/reader/list_catalog_items.jsp";
        public static final String PAGE_LIST_READERS_REQUESTS = "/WEB-INF/jsp/librarian/list_readers_requests.jsp";
        public static final String PAGE_CONFIRM_REQUEST_FORM = "/WEB-INF/jsp/librarian/confirm_request_form.jsp";
        public static final String PAGE_LIST_USERS = "/WEB-INF/jsp/admin/list_users.jsp";
        public static final String PAGE_LIST_ADMIN_CATALOG = "/WEB-INF/jsp/admin/list_admin_catalog.jsp";
        public static final String PAGE_USER_DETAILS = "/WEB-INF/jsp/admin/user_details.jsp";
        public static final String PAGE_MODIFY_BOOK = "/WEB-INF/jsp/admin/modify_book_form.jsp";
        public static final String PAGE_ADD_LIBRARIAN = "/WEB-INF/jsp/admin/add_librarian.jsp";
        public static final String PAGE_ADD_BOOK = "/WEB-INF/jsp/admin/add_book_form.jsp";
        public static final String PAGE_SETTINGS = "/WEB-INF/jsp/settings.jsp";
        public static final String PAGE_SIGN_UP = "/WEB-INF/jsp/signUp/signUpForm.jsp";
        public static final String PAGE_UPDATE_PROFILE = "/WEB-INF/jsp/update_profile.jsp";


        // commands
        public static final String COMMAND_LOGOUT = "/controller?command=logout";
        public static final String COMMAND_LOGIN = "/controller?command=login";
        public static final String COMMAND_LIST_ADMIN_CATALOG = "/controller?command=listAdminCatalog";
        public static final String COMMAND_LIST_READERS = "/controller?command=listReaders";
        public static final String COMMAND_LIST_LIBRARIANS = "/controller?command=listLibrarians";
        public static final String COMMAND_SETTINGS = "/controller?command=viewSettings";
        public static final String COMMAND_LIST_CATALOG = "/controller?command=listCatalog";
        public static final String COMMAND_READERS_REQUESTS = "/controller?command=listReadersRequests";
        public static final String COMMAND_LIST_PERSONAL_AREA = "/controller?command=listPersonalArea";
        public static final String COMMAND_LIST_PERSONAL_AREA_LIBRARY_CARD = "/controller?command=listPersonalArea&bookStatus=libraryCard";

    }

    public final class Query {
        private Query() {}

        // -- users
        public static final String SQL_CREATE_USER = "INSERT INTO users VALUES(DEFAULT, ?, ?, ?, ?, ?, DEFAULT, DEFAULT, ?)";
        public static final String SQL_REMOVE_USER_BY_ID = "DELETE FROM users WHERE id=?";
        public static final String SQL_FIND_USER_BY_ROLE = "SELECT * FROM users WHERE role_id=?";
        public static final String SQL_FIND_USER_BY_ROLE_AND_FIRST_NAME = "SELECT * FROM users WHERE role_id=? AND first_name LIKE ?";
        public static final String SQL_FIND_USER_BY_ROLE_AND_LAST_NAME = "SELECT * FROM users WHERE role_id=? AND last_name LIKE ?";
        public static final String SQL_FIND_USER_BY_ROLE_AND_FIRST_NAME_AND_LAST_NAME = "SELECT * FROM users WHERE role_id=? AND first_name LIKE ? AND last_name LIKE ?";
        public static final String SQL_SET_USERS_LOCALE = "UPDATE users SET locale_id=? WHERE id=?";
        public static final String SQL_FIND_USER_BY_LOGIN_TO_UPDATE = "SELECT * FROM users WHERE login=? AND id!=?";
        public static final String SQL_FIND_USER_BY_EMAIL_TO_UPDATE = "SELECT * FROM users WHERE email=? AND id!=?";
        public static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
        public static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email=?";
        public static final String SQL_UPDATE_USER = "UPDATE users SET password=?, login=?, first_name=?, last_name=?, email=? "
            + "	WHERE id=?";
        public static final String SQL_UPDATE_USER_PASSWORD_BY_EMAIL = "UPDATE users SET password=? WHERE email=?";
        public static final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id=?";
        public static final String SQL_CREATE_BLOCKED_USER = "INSERT INTO users_blocked VALUES(DEFAULT, ?)";
        public static final String SQL_REMOVE_BLOCKED_USER = "DELETE FROM users_blocked WHERE user_id=?";
        public static final String SQL_IS_USER_BLOCKED = "SELECT * FROM users_blocked WHERE user_id=?";

        // -- library_cards
        public static final String SQL_CREATE_USER_LIBRARY_CARD = "INSERT INTO library_cards VALUES(DEFAULT, ?)";
        public static final String SQL_FIND_USER_LIBRARY_CARD_ID = "SELECT library_cards.id FROM library_cards WHERE user_id=?";

        // -- catalog_items
        public static final String SQL_FIND_ALL_CATALOG_ITEMS = "SELECT * FROM catalog_items";
        public static final String SQL_FIND_CATALOG_ITEMS_BY_AUTHOR = "SELECT * FROM catalog_items WHERE author LIKE ?";
        public static final String SQL_FIND_CATALOG_ITEMS_BY_TITLE = "SELECT * FROM catalog_items WHERE title LIKE ?";
        public static final String SQL_FIND_CATALOG_ITEMS_BY_AUTHOR_AND_TITLE = "SELECT * FROM catalog_items WHERE author LIKE ? AND title LIKE ?";
        public static final String SQL_CREATE_LIBRARY_CARD_ITEM_REQUEST = "INSERT INTO library_card_items VALUES(DEFAULT, ?, ?, NULL, NULL, NULL, DEFAULT)";
        public static final String SQL_INCREMENT_CATALOG_ITEM_INSTANCES_BY_LIBRARY_CARD_ITEM_ID = "UPDATE  catalog_items ci SET instances_number = (SELECT instances_number + 1 FROM catalog_items WHERE id = ci.id) WHERE ci.id = (SELECT catalog_item_id FROM library_card_items WHERE id = ?)";
        public static final String SQL_INCREMENT_BOOKS_HAS_TAKEN_BY_EMAIL = "UPDATE  users SET books_has_taken = (SELECT books_has_taken + 1 FROM users WHERE email = ?)";
        public static final String SQL_DECREMENT_CATALOG_ITEM_INSTANCES_BY_LIBRARY_CARD_ITEM_ID = "UPDATE  catalog_items ci SET instances_number = (SELECT instances_number - 1 FROM catalog_items WHERE id = ci.id) WHERE ci.id = (SELECT catalog_item_id FROM library_card_items WHERE id = ?)";
        public static final String SQL_REMOVE_CATALOG_ITEM_BY_ID = "DELETE FROM catalog_items WHERE id=?";
        public static final String SQL_UPDATE_CATALOG_ITEM = "UPDATE catalog_items SET title=?, author=?, edition=?, publication_year=?, instances_number=? "
            + "WHERE id=?";
        public static final String SQL_CREATE_CATALOG_ITEM = "INSERT INTO catalog_items VALUES(DEFAULT, ?, ?, ?, ?, ?)";

        // -- library_card_items
        public static final String SQL_REMOVE_LIBRARY_CARD_ITEM_BY_ID = "DELETE FROM library_card_items WHERE id=?";
        public static final String SQL_UPDATE_LIBRARY_CARD_ITEM_STATUS_ID = "UPDATE library_card_items SET status_id=? WHERE id=?";
        public static final String SQL_GET_LIBRARY_CARD_BY_LIBRARY_CARD_ITEM_ID_AND_CATALOG_ITEM_ID = "SELECT * FROM library_card_items WHERE subscription_id=? AND catalog_id=?";

        // -- other
        public static final String SQL_GET_CATALOG_ITEM_IDS_BY_USER_ID_AND_STATUS = "SELECT lci.catalog_item_id FROM library_card_items lci, library_cards lc, users u WHERE lci.library_card_id=lc.id AND u.id=lc.user_id AND u.id=? AND status_id=?";
        public static final String SQL_GET_CATALOG_ITEM_BEANS_BY_STATUS = "SELECT lci.id, ci.title, ci.author, u.first_name, u.last_name, u.email "
            + "FROM library_card_items lci, catalog_items ci, statuses s, library_cards lc, users u "
            + "WHERE lci.status_id=s.id AND lci.catalog_item_id=ci.id AND lci.library_card_id=lc.id AND lc.user_id=u.id AND s.id=?";
        public static final String SQL_GET_CATALOG_ITEM_BEANS = "SELECT lci.id, ci.title, ci.author,  lci.date_to, lci.penalty_size "
            + "FROM library_card_items lci, catalog_items ci, statuses s, library_cards lc, users u WHERE lci.status_id=s.id AND lci.catalog_item_id=ci.id AND lci.library_card_id=lc.id AND lc.user_id=u.id AND u.id=? AND s.id=?";
        public static final String SQL_FIND_CATALOG_ITEM_INSTANCES_NUMBER_BY_LIBRARY_CARD_ID = "SELECT ci.instances_number "
            + "FROM library_card_items lci, catalog_items ci " + "WHERE lci.catalog_item_id=ci.id AND lci.id=?";
        public static final String SQL_UPDATE_CATALOG_ITEM_REQUEST_BY_ID = "UPDATE library_card_items SET date_from=?, date_to=?, penalty_size=?, status_id=? "
            + "WHERE id=?";

    }

    /**
     * Holder for fields names of DB tables and beans.
     *
     * @author O.Slynko
     *
     */
    public static final class Fields {

        // entities
        public static final String ENTITY_ID = "id";

        public static final String USER_LOGIN = "login";
        public static final String USER_PASSWORD = "password";
        public static final String USER_FIRST_NAME = "first_name";
        public static final String USER_LAST_NAME = "last_name";
        public static final String USER_EMAIL = "email";
        public static final String USER_LOCALE_ID = "locale_id";
        public static final String USER_ROLE_ID = "role_id";

        public static final String SUBSCRIPTION_USER_ID = "user_id";

        public static final String CATALOG_ITEM_TITLE = "title";
        public static final String CATALOG_ITEM_AUTHOR = "author";
        public static final String CATALOG_ITEM_EDITION = "edition";
        public static final String CATALOG_ITEM_PUBLICATION_YEAR = "publication_year";
        public static final String CATALOG_ITEM_INSTANCES_NUMBER = "instances_number";

        public static final String LIBRARY_CARD_ITEM_LIBRARY_CARD_ID = "library_card_id";
        public static final String LIBRARY_CARD_ITEM_CATALOG_ITEM_ID = "catalog_item_id";
        public static final String LIBRARY_CARD_ITEM_DATE_FROM = "date_from";
        public static final String LIBRARY_CARD_ITEM_DATE_TO = "date_to";
        public static final String LIBRARY_CARD_ITEM_PENALTY_SIZE = "penalty_size";
        public static final String LIBRARY_CARD_ITEM_STATUS_ID = "status_id";

        // beans
        public static final String USER_CATALOG_ITEM_BEAN_LIBRARY_CARD_ITEMS_ID = "id";
        public static final String USER_CATALOG_ITEM_BEAN_TITLE = "title";
        public static final String USER_CATALOG_ITEM_BEAN_AUTHOR = "author";
        public static final String USER_CATALOG_ITEM_BEAN_DATE_TO = "date_to";
        public static final String USER_CATALOG_ITEM_BEAN_PENALTY_SIZE = "penalty_size";

        public static final String CATALOG_ITEM_REQUEST_BEAN_LIBRARY_CARD_ITEMS_ID = "id";
        public static final String CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_TITLE = "title";
        public static final String CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_AUTHOR = "author";
        public static final String CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_FIRST_NAME = "first_name";
        public static final String CATALOG_ITEM_REQUEST_BEAN_CATALOG_ITEM_LAST_NAME = "last_name";
        public static final String CATALOG_ITEM_REQUEST_BEAN_USER_EMAIL = "email";
    }
}
