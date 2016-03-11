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

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import in.mittaluday.file_indexer.MongoApp;
import in.mittaluday.file_indexer.Postings;

public class QueryProcessorMongo {
	Map<String, List<Postings>> index;
	HashMap<String, Double> cumulativePageScoreMap;

	public QueryProcessorMongo() throws ClassNotFoundException, IOException {
		index = in.mittaluday.file_indexer.App.getIndex();
		cumulativePageScoreMap = new HashMap<String, Double>();
	}

	public ArrayList<String> queryIndex(String query) {
		if (query == null || query.isEmpty()) {
			return new ArrayList<String>();
		}
		String[] queryTerms = query.split(" ");
		findMatchingPages(queryTerms);
		findMatchingPagesInAnchorText(queryTerms);
		ArrayList<String> results = rankResults();
		return results;
	}

	private void findMatchingPages(String[] queryTerms) {
		for (String term : queryTerms) {
			List<Document> postings = MongoApp.db.getCollection("index").find(new Document("term", term))
					.into(new ArrayList<Document>());
			if (postings != null) {
				for (Document p : postings) {
					addPageScoreForTerm(p.getString("document_name"), p.getDouble("tfidf"));
					String title = p.getString("title");
					if(title.contains(term)){
						addPageScoreForTerm(p.getString("document_name"), 1.0);
					}
				}
			}
		}
	}
	

	private void findMatchingPagesInAnchorText(String[] queryTerms) {
		for (String term : queryTerms) {
			List<Document> postings = MongoApp.db.getCollection("anchorindex").find(new Document("term", term))
					.into(new ArrayList<Document>());
			if (postings != null) {
				for (Document p : postings) {
					addPageScoreForTerm(p.getString("document_name"), p.getDouble("tfidf"));
				}
			}
		}		
	}
	
	private void findMatchingPagesBasedOnCosine(String[] queryTerms) {
		//Assuming tf for every query term as 1 i.e. every term occurs only once in the query
		double [] queryTermIdf = new double[queryTerms.length];
		List<Document> postings = MongoApp.db.getCollection("statistics").find().into(new ArrayList<Document>());
		long corpusSize = postings.get(0).getLong("corpussize");
		for (int i=0;i<queryTerms.length;i++) {
			double idf = ((ArrayList<Postings>)index.get(queryTerms[i])).size();
			queryTermIdf[i] = Math.log10(corpusSize/idf);
		}	
		// cant proceed, dont have euclidean length for every document 		
	}

	private void addPageScoreForTerm(String documentName, double tfidf) {
		if (cumulativePageScoreMap.containsKey(documentName)) {
			cumulativePageScoreMap.put(documentName, cumulativePageScoreMap.get(documentName) + tfidf);
		} else {
			cumulativePageScoreMap.put(documentName, tfidf);
		}
	}

	private ArrayList<String> rankResults() {
		cumulativePageScoreMap = sortMap(cumulativePageScoreMap);
		ArrayList<String> pages = new ArrayList<String>(cumulativePageScoreMap.keySet());
		if (pages.size() > 9) {
			return new ArrayList<String>(pages.subList(0, 9));
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
