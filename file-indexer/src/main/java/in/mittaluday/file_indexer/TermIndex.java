package in.mittaluday.file_indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mittaluday.data_processor.FileTokenizer;

public class TermIndex {
	static Map<String, List<Postings>> index;
	static long corpus;

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
