package in.mittaluday.file_indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	String CRAWL_FOLDER = "/Users/udaymittal/dumpnirvan";
        String DUMP_FOLDER ="";
    	TermIndex index = new TermIndex();
    	int counter =0;
		File dumpFileDirectory = new File(CRAWL_FOLDER + DUMP_FOLDER);
		for( File file:dumpFileDirectory.listFiles()){
			counter+=1;
			index.addTerms(file);
			if(counter%5000 == 0){
				System.out.println(counter + " Files indexed");
			}
		}
		
		
		
	calculateTfidf(index);
		
	serializeIndex(index);
	
//	showIndex(index);
    }
    
    public static void calculateTfidf(TermIndex index){
    	int counter = 0;
    	System.out.println("Total Terms: " + String.valueOf(index.getIndex().keySet().size()));
    	for(String term: index.getIndex().keySet()){
    		counter+=1;
    		if(counter %10000 ==0){
    			System.out.println(counter + " tfidf calculated for these many terms");
    		}
			for(Postings p : index.getIndex().get(term)){
				p.setTfidf(p.getPositions().size(),
						index.getIndex().get(term).size(), index.getCorpus());
			}
		}
    }
    
    public static void serializeIndex(TermIndex index) throws IOException{
    	String indexFilePath = "index.ser";
    	System.out.println("TermObject Size: " + index.getIndex().size());
    	File indexFile = new File(indexFilePath);
    	if (!indexFile.exists())
    	{
    	    indexFile.createNewFile();
    	}
    	FileOutputStream f = new FileOutputStream(indexFile, false);
    	ObjectOutputStream s = new ObjectOutputStream(f);
    	s.writeObject(index);
    	s.close();
    } 
    
    public static void showIndex(TermIndex index){
    	int counter = 0;
    	for(String term : index.getIndex().keySet()){
    		counter +=1;
			System.out.println(term + index.getIndex().get(term));
			if(counter == 50){
				break;
			}
		}
    }
    
}
