package in.mittaluday.query_engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import in.mittaluday.file_indexer.Postings;

public class QueryProcessor {
	
	Map<String, List<Postings>> index;
	HashMap<String, Double> cumulativePageScoreMap;
	
	public QueryProcessor() throws ClassNotFoundException, IOException{
		initialize();
	}
	
	private void initialize() throws ClassNotFoundException, IOException {
		index = in.mittaluday.file_indexer.App.createIndex();		
		cumulativePageScoreMap = new HashMap<String, Double>();
	}

	public ArrayList<String> queryIndex(String query) {
		if(query == null || query.isEmpty()){
			return new ArrayList<String>();
		}
		String [] queryTerms = query.split(" ");
		findMatchingPages(queryTerms);
		ArrayList<String> results = rankResults();
		return results;		
	}

	private void findMatchingPages(String[] queryTerms) {
		for(String term : queryTerms){
			ArrayList<Postings> postings = (ArrayList<Postings>) index.get(term);
			for (Postings p : postings){
				addPageScoreForTerm(p.getDocumentName(), p.getTfidf());	
			}
		}
	}

	private void addPageScoreForTerm(String documentName, double tfidf) {
		if(cumulativePageScoreMap.containsKey(documentName)){
			cumulativePageScoreMap.put(documentName, cumulativePageScoreMap.get(documentName)+tfidf);
		} else {
			cumulativePageScoreMap.put(documentName, tfidf);
		}		
	}

	private ArrayList<String> rankResults() {
		cumulativePageScoreMap = sortMap(cumulativePageScoreMap);
		ArrayList<String> pages = new ArrayList<String>(cumulativePageScoreMap.keySet());
		if(pages.size()>9){
			return new ArrayList<String> (pages.subList(0, 9));
		}
		return pages;		
	}
	
	public <K, V extends Comparable<? super V>> HashMap<K, V> sortMap(HashMap<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		HashMap<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
