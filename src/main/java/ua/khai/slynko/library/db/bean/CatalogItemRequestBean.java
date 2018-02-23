package ua.khai.slynko.library.db.bean;

import ua.khai.slynko.library.db.entity.Entity;

/**
 * Provide records for virtual table:
 * 
 * <pre>
 * |library_card_items.id|catalog_items.title|catalog_items.author|
 * </pre>
 * 
 * @author O.Slynko
 * 
 */
public class CatalogItemRequestBean extends Entity {

	private static final long serialVersionUID = -5654982557199337483L;

	private String title;

	private String author;

	private String firstName;

	private String lastName;
	
	private String userEmail;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	@Override
	public String toString() {
		return "CatalogItemRequestBean [title=" + title + ", author=" + author + ", firstName=" + firstName
				+ ", lastName=" + lastName + "]";
	}

}
