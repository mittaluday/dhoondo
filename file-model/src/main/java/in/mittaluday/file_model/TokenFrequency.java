package in.mittaluday.file_model;

/**
 * Token in a document/file
 * 
 * @author udaymittal
 *
 */
public class TokenFrequency {
	Integer id;
	String content;
	Integer frequency;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}


	public TokenFrequency(String content, Integer frequency){
		this.content = content;
		this.frequency = frequency;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return content;
	}
	
	@Override
	public boolean equals(Object obj) {
		TokenFrequency tobj = (TokenFrequency) obj;
		if(this.content.equals(tobj.content)){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return content.hashCode();
	}
	
}
