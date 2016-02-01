package in.mittaluday.file_model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A Three Gram contains a list of 
 * 3 Tokens
 * 
 * @author udaymittal
 *
 */
@Entity
@Table
public class ThreeGram{
	List<Token> tokenList;

	public ThreeGram(List<Token> tokenList) {
		this.tokenList = tokenList;
	}

	public List<Token> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<Token> tokenList) {
		this.tokenList = tokenList;
	}
	
	@Override
	public String toString() {
		return tokenList.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		ThreeGram tobj = (ThreeGram) obj;
		List<Token> argThreeGramTokens = tobj.getTokenList();
		for(int i=0; i<tokenList.size(); i++){
			if(tokenList.get(i).getContent()
					.equals(argThreeGramTokens.get(i).getContent())){
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashcode = 0;
		for (Token token : tokenList) {
			hashcode += token.getContent().hashCode();
		}
		return hashcode;
	}
}
