package ua.khai.slynko.Library.db;

/**
 * Holder for fields names of DB tables and beans.
 * 
 * @author O.Slynko
 * 
 */
public final class Fields {

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