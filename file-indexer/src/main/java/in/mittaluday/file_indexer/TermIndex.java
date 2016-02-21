package in.mittaluday.file_indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mittaluday.data_processor.FileTokenizer;

public class TermIndex implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		// TODO Auto-generated constructor stub
		index = new HashMap();
	}

	public void addTerms(File file) throws FileNotFoundException {
		FileTokenizer ft = new FileTokenizer(file);
		ft.tokenizeFile();
		corpus++;
		Map<String,List<Integer>> tokenPositionMap = ft.getTokenPostion();
		for (String token : tokenPositionMap.keySet()) {
			if(index.containsKey(token)){
				index.get(token).add(new Postings(ft.getSubdomainURL(),tokenPositionMap.get(token)));
			}
			else
			{
				index.put(token,new ArrayList<Postings>() );
				index.get(token).add(new Postings(ft.getSubdomainURL(),tokenPositionMap.get(token)));
			}
		}
		
		
	}

}
