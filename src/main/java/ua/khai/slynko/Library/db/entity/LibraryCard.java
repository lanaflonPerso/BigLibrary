package ua.khai.slynko.Library.db.entity;

/**
 * Library card entity.
 * 
 * @author O.Slynko
 * 
 */
public class LibraryCard extends Entity {

	private static final long serialVersionUID = 5692708766041889396L;

	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Subscription [userId=" + userId + "]";
	}

}
