package Lab_3;


public class Film {
	private String title;
	private Integer year, duration;
	
	public Film() {
		title = "";
		year = duration = 0;
	}
	public Film(String title, Integer year, Integer duration) {
		this.title = title;
		this.year = year;
		this.duration = duration; // минуты
	}
	
	public Film copy() {
		Film newFilm = new Film(this.title, this.year, this.duration);
		return newFilm;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

}
