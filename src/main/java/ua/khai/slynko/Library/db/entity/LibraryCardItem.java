package ua.khai.slynko.Library.db.entity;

import java.util.Date;

/**
 * Library card item entity.
 * 
 * @author O.Slynko
 * 
 */
public class LibraryCardItem extends Entity {

	private static final long serialVersionUID = 4716395168539434663L;

	private Integer libraryCardId;

	private Integer catalogItemId;

	private Date dateFrom;

	private Date dateTo;

	private Integer penaltySize;

	private Integer statusId;

	public Integer getLibraryCardId() {
		return libraryCardId;
	}

	public void setLibraryCardId(Integer libraryCardId) {
		this.libraryCardId = libraryCardId;
	}

	public Integer getCatalogItemId() {
		return catalogItemId;
	}

	public void setCatalogItemId(Integer catalogItemId) {
		this.catalogItemId = catalogItemId;
	}

	public Date getDateFrom() {
		if (dateFrom != null) {
			return new Date(dateFrom.getTime());
		} else {
			return null;
		}
	}

	public void setDateFrom(Date dateFrom) {
		if (dateFrom != null) {
			this.dateFrom = new Date(dateFrom.getTime());
		}
	}

	public Date getDateTo() {
		if (dateTo != null) {
			return new Date(dateTo.getTime());
		} else {
			return null;
		}
	}

	public void setDateTo(Date dateTo) {
		if (dateTo != null) {
			this.dateTo = new Date(dateTo.getTime());
		}
	}

	public Integer getPenaltySize() {
		return penaltySize;
	}

	public void setPenaltySize(Integer penaltySize) {
		this.penaltySize = penaltySize;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	@Override
	public String toString() {
		return "LibraryCardItem [libraryCardId=" + libraryCardId + ", catalogItemId=" + catalogItemId + ", dateFrom="
				+ dateFrom + ", dateTo=" + dateTo + ", penaltySize=" + penaltySize + ", statusId=" + statusId + "]";
	}

}