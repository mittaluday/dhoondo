package in.mittaluday.file_model;

import java.util.List;

/**
 * A Three Gram contains a list of 
 * 3 Tokens
 * 
 * @author udaymittal
 *
 */
public class ThreeGramFrequencies{
	List<TokenFrequency> tokenList;

	public ThreeGramFrequencies(List<TokenFrequency> tokenList) {
		this.tokenList = tokenList;
	}

	public List<TokenFrequency> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<TokenFrequency> tokenList) {
		this.tokenList = tokenList;
	}
	
	@Override
	public String toString() {
		return tokenList.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		ThreeGramFrequencies tobj = (ThreeGramFrequencies) obj;
		List<TokenFrequency> argThreeGramTokens = tobj.getTokenList();
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
		for (TokenFrequency token : tokenList) {
			hashcode += token.getContent().hashCode();
		}
		return hashcode;
	}
}
