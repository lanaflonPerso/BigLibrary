package ua.khai.slynko.Library.db.entity;

/**
 * User entity.
 * 
 * @author O.Slynko
 * 
 */
public class User extends Entity {

	private static final long serialVersionUID = -6889036256149495388L;

	private String login;

	private String password;

	private String firstName;

	private String lastName;

	private String email;

	private String locale;

	private Integer booksHasTaken; 
	
	public Integer getBooksHasTaken() {
		return booksHasTaken;
	}

	public void setBooksHasTaken(Integer booksHasTaken) {
		this.booksHasTaken = booksHasTaken;
	}

	private int roleId;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "User [login=" + login + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", locale=" + locale + ", roleId=" + roleId + "]";
	}

}
