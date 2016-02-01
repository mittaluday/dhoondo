package in.mittaluday.file_model;

/**
 * Token in a document/file
 * 
 * @author udaymittal
 *
 */
public class Token {
	String content;
	
	public Token(String content){
		this.content = content;
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
		Token tobj = (Token) obj;
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
