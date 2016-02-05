package in.mittaluday.data_processor;

import java.util.HashMap;
import java.util.List;

public class TokenSingleton {
 
	private static HashMap<String,Integer> tokenFrequency;
	
	private static TokenSingleton tokensingleton = new TokenSingleton( );
	
	private TokenSingleton()
	{
		tokenFrequency=new HashMap<String, Integer>();
	}
	public static TokenSingleton getInstance()
	{
		return tokensingleton;
	}
	
	public static HashMap<String, Integer> getTokenFrequencySingleton(){
		return tokenFrequency;
	}
	
	public void addTokenList(List<String> tokenList)
	{
		for (int i = 0; i < tokenList.size(); i++) {
			if (tokenFrequency.get(tokenList.get(i)) != null) {
				if (tokenFrequency.containsKey(tokenList.get(i))) {
					tokenFrequency.put(tokenList.get(i), tokenFrequency.get(tokenList.get(i))+1);
				}
			} else {
				tokenFrequency.put(tokenList.get(i),1);
			}
		}
	}
	 
}
