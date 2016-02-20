package in.mittaluday.file_indexer;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
    	String CRAWL_FOLDER = "../crawler-data";
        String DUMP_FOLDER ="/dump";
    	TermIndex index = new TermIndex();
		File dumpFileDirectory = new File(CRAWL_FOLDER + DUMP_FOLDER);
		for( File file:dumpFileDirectory.listFiles()){
			index.addTerms(file);
		}
		
		for(String term: index.getIndex().keySet()){
			for(Postings p : index.getIndex().get(term)){
				p.setTfidf(p.getPositions().size(),
						index.getIndex().get(term).size(), index.getCorpus());
			}
		}
		
		for(String term : index.getIndex().keySet()){
			System.out.println(term + index.getIndex().get(term));
		}
    }
    
    
}
