package Model;

public class tracker {

	private int userId;
	private int showId;
	private String title;
	public tracker(int userId, int showId, String title) {
		super();
		this.userId = userId;
		this.showId = showId;
		this.title = title;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getShowId() {
		return showId;
	}
	public void setShowId(int showId) {
		this.showId = showId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "tracker [userId=" + userId + ", showId=" + showId + ", title=" + title + "]";
	}
	
	
	
	
	
}
