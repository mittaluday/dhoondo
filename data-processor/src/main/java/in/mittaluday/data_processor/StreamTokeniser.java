package in.mittaluday.data_processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StreamTokeniser {

	String stream;
	private ArrayList<String> listOfTokens;
	private HashMap<String, List<Integer>> tokenPosition;
	private static final String specialCharacters = ":;,~!@#$%^&*()_+=\\.\"<>?/`\\W";
	private static final String delimeters = "[ " + specialCharacters + "]+";
	
	public StreamTokeniser(){
		listOfTokens = new ArrayList<String>();
		tokenPosition = new HashMap<String, List<Integer>>();
	 }
	public StreamTokeniser(String s){
		stream=s;
		listOfTokens = new ArrayList<String>(); 
		tokenPosition = new HashMap<String, List<Integer>>();
	 }
	public ArrayList<String> getListOfTokens() {
		return listOfTokens;
	}
	public HashMap<String, List<Integer>> getTokenPosition() {
		return tokenPosition;
	}

	public void tokenizeString() {
		String tokens[] = stream.split(delimeters);
		int positionCounter = 0;
		for (int i = 0; i < tokens.length; i++) {
			String trimmedToken = tokens[i].trim();
			if (trimmedToken.length() > 0) {
				listOfTokens.add(trimmedToken.toLowerCase());
				addTokenPosition(trimmedToken.toLowerCase(), positionCounter++);
			}
		}
	}
	private void addTokenPosition(String token, int position) {
		if (tokenPosition.containsKey(token)) {
			tokenPosition.get(token).add(position);
		} else {
			tokenPosition.put(token, new ArrayList<Integer>());
			tokenPosition.get(token).add(position);
		}
	}
}
