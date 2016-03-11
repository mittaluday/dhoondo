package in.mittaluday.query_engine;

public class Result {
	
	private String URL;
	private String title;
	private String text;
	
	public Result(String URL, String title){
		this.setURL(URL);
		this.setTitle(title);
	}
	
	private String getURL() {
		return URL;
	}

	private void setURL(String uRL) {
		URL = uRL;
	}

	private String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	private String getText() {
		return text;
	}

	private void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode(){
		return URL.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj){
			return true;
		}
		if(!(obj instanceof Result)){
			return false;
		}
		
		Result r = (Result)obj;
		return URL.equals(r.getURL());        
    } 
	

}
