package Model;

public class tvShows {	
	private String tvShow_name;
	private int tv_id;
	private String category;
	
	
	public String getTvshow_name() {
		return tvShow_name;
	}
	
	public void setTvshow_name(String tvshow_name) {
		this.tvShow_name = tvshow_name;
	}
	public int getTv_id() {
		return tv_id;
	}
	public void setTv_id(int tv_id) {
		this.tv_id = tv_id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "tvshows [tvshow_name=" + tvShow_name + ", tv_id=" + tv_id + ", category=" + category + "]";
	}
	
	
	
	
	
	
}