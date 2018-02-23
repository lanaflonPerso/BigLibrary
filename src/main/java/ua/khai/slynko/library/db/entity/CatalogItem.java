package ua.khai.slynko.library.db.entity;

/**
 * Catalog item entity.
 * 
 * @author O.Slynko
 * 
 */
public class CatalogItem extends Entity {

	private static final long serialVersionUID = 4716395168539434663L;

	private String title;

	private String author;

	private String edition;

	private Integer publicationYear;

	private Integer instancesNumber;

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

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public Integer getInstancesNumber() {
		return instancesNumber;
	}

	public void setInstancesNumber(Integer instancesNumber) {
		this.instancesNumber = instancesNumber;
	}

	@Override
	public String toString() {
		return "CatalogItem [id= " + getId() + ", title=" + title + ", author=" + author + ", edition=" + edition
				+ ", publicationYear=" + publicationYear + ", instancesNumber=" + instancesNumber + "]";
	}

}