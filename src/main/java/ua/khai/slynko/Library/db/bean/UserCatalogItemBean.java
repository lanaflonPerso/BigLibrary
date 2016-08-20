package ua.khai.slynko.Library.db.bean;

import java.util.Date;

import ua.khai.slynko.Library.db.entity.Entity;

/**
 * Provide records for virtual table:
 * 
 * <pre>
 * |library_card_items.id|catalog_items.title|catalog_items.author|library_card_items.date_to|library_card_items.penalty_size|
 * </pre>
 * 
 * @author O.Slynko
 * 
 */
public class UserCatalogItemBean extends Entity {

	private static final long serialVersionUID = -5654982557199337483L;

	private String title;

	private String author;

	private Date dateTo;

	private int penaltySize;

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

	public Date getDateTo() {
		return new Date(dateTo.getTime());
	}

	public void setDateTo(Date dateTo) {
		if (dateTo != null) {
			this.dateTo = new Date(dateTo.getTime());
		}
	}

	public int getPenaltySize() {
		return penaltySize;
	}

	public void setPenaltySize(int penaltySize) {
		this.penaltySize = penaltySize;
	}

	@Override
	public String toString() {
		return "UserCatalogItemBean [libraryCardItemsId=" + getId() + ", title=" + title + ", author=" + author
				+ ", dateTo=" + dateTo + ", penaltySize=" + penaltySize + "]";
	}
}
