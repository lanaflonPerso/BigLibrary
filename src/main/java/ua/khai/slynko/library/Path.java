package ua.khai.slynko.library;

/**
 * Path holder (jsp pages, controller commands).
 * 
 * @author O.Slynko
 * 
 */
public final class Path {
	
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