package in.mittaluday.query_engine;

public class Result {

	String title;
	String description;
	String url;

	public Result() {

	}

	public Result(String URL, String title) {
		this.setUrl(URL);
		this.setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}