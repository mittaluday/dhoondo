package in.mittaluday.file_indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mittaluday.data_processor.FileTokenizer;
import in.mittaluday.data_processor.StreamTokeniser;

@SuppressWarnings("serial")
public class TermIndex implements Serializable {
	/**
	 * 
	 */
	static Map<String, List<Postings>> index;
	static long corpus;

	public static Map<String, List<Postings>> getIndex() {
		return index;
	}

	public static void setIndex(Map<String, List<Postings>> index) {
		TermIndex.index = index;
	}

	public static long getCorpus() {
		return corpus;
	}

	public static void setCorpus(long corpus) {
		TermIndex.corpus = corpus;
	}

	public TermIndex() {
		index = new HashMap<String, List<Postings>>();
	}

	/**
	 * File addition to index
	 * @param file
	 * @throws FileNotFoundException
	 */
	public void addTerms(File file) throws FileNotFoundException {
		FileTokenizer ft = new FileTokenizer(file);
		ft.tokenizeFile();
		long documentLength = ft.getListOfTokens().size();
		String title = ft.getTitle();
		HashMap<String, Integer> tokenCount = ft.getTokenCount();
		double docVectorMagnitude = calculateDocVectorMagnitude(tokenCount);
		corpus++;
		if (corpus % 5000 == 0) {
			System.out.println(corpus + " Files indexed");
		}
		Map<String,List<Integer>> tokenPositionMap = ft.getTokenPosition();
		for (String token : tokenPositionMap.keySet()) {
			if(index.containsKey(token)){
				index.get(token).add(new Postings(ft.getSubdomainURL(),tokenPositionMap.get(token), title, documentLength, docVectorMagnitude));
			}
			else
			{
				index.put(token,new ArrayList<Postings>() );
				index.get(token).add(new Postings(ft.getSubdomainURL(),tokenPositionMap.get(token), title, documentLength, docVectorMagnitude));
			}
		}
	}
	
	/**
	 * Takes the term count of every token, calculates tf = 1 + Math.log(term count)
	 * docVectorMagnitude = Math.sqrt(tf1*tf1 + tf2*tf2 + tf3*tf3...)
	 * @param tokenCount
	 * @return docVectorMagnitude in double
	 */
	
	private double calculateDocVectorMagnitude(HashMap<String, Integer> tokenCount) {
		double docVectorMagnitude = 0;
		double tempSummation = 0;
		for(Map.Entry<String, Integer> entry: tokenCount.entrySet()){
			double termTf = 1 + Math.log(entry.getValue());
			tempSummation += termTf * termTf;			
		}
		docVectorMagnitude = Math.sqrt(tempSummation);		
		return docVectorMagnitude;
	}

	/**
	 * Anchor text term addition
	 * @param URL
	 * @param stream
	 * @throws FileNotFoundException
	 */
	public void addTerms(String URL,String stream) throws FileNotFoundException {
		corpus++;
		StreamTokeniser ft=new StreamTokeniser(stream);
		ft.tokenizeString();
		long documentLength = ft.getListOfTokens().size();
		Map<String,List<Integer>> tokenPositionMap = ft.getTokenPosition();
		for (String token : tokenPositionMap.keySet()){
			if(index.containsKey(token)){
				index.get(token).add(new Postings(URL,tokenPositionMap.get(token), documentLength));
			}
			else
			{
				index.put(token,new ArrayList<Postings>() );
				index.get(token).add(new Postings(URL,tokenPositionMap.get(token), documentLength));
			}
		}	
	}

}
