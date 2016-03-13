package in.mittaluday.data_processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import in.mittaluday.file_repo.FileIndexRepository;
import in.mittaluday.file_repo.ThreeGramFrequencyRepository;
import in.mittaluday.file_repo.TokenFrequencyRepository;

public class CrawledDataProcessor {
	
	
	Logger logger = LoggerFactory.getLogger(CrawledDataProcessor.class);

	private int uniquePagesCrawled;
	private FileTokenizer fileTokenizer;
	private FileIndexRepository fileIndexRepo;
	private ThreeGramFrequencyRepository threeGramRepo;
	private TokenFrequencyRepository tokenRepo;
	
	public CrawledDataProcessor(){
		fileTokenizer = new FileTokenizer();
		uniquePagesCrawled = 0;		
		fileIndexRepo = new FileIndexRepository();
		threeGramRepo = new ThreeGramFrequencyRepository();
		tokenRepo = new TokenFrequencyRepository();

	}
	
	
	public void startDataProcessing() throws IOException, ClassNotFoundException{
		Properties prop = getConfigurationProperties();
		
		
		File dumpFileDirectory = new File(prop.getProperty("CRAWL_FOLDER") + prop.getProperty("DUMP_FOLDER"));
		for( File file:dumpFileDirectory.listFiles()){
			fileTokenizer.initialize(file);
			try {
				fileTokenizer.tokenizeFile();
				fileTokenizer.computeThreeGrams(fileTokenizer.getListOfTokens());
				TokenSingleton.getInstance().addTokenList(fileTokenizer.getListOfTokens());
				ThreeGramSingleton.getInstance().addThreeGramList(fileTokenizer.getListOfThreeGrams());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}			
			uniquePagesCrawled++;
			saveFileMetaToRepository();
		}
		saveTokenAggregatesToRepository();
		saveThreeGramAggregatesToRepository();
	}
	
	public int getUniquePagesCrawled(){
		return uniquePagesCrawled;
	}
	
	private Properties getConfigurationProperties() throws IOException{
		InputStream inputStream = null;
		Properties prop = new Properties();
		try {
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return prop;
	}

	private void saveFileMetaToRepository(){
		fileIndexRepo.addFileIndex(fileTokenizer.getFile().getName(), new Date(), new Date(), 
				fileTokenizer.getListOfTokens().size(), fileTokenizer.getSubdomain(), fileTokenizer.getTitle());
	}
	
	private void saveTokenAggregatesToRepository(){
		HashMap<String, Integer> tokenAggregates = TokenSingleton.getTokenFrequencySingleton();
		int counter =0;
		for (String token : tokenAggregates.keySet()) {
			try{
				tokenRepo.addTokenFrequency(token, tokenAggregates.get(token));
			}
			catch (Exception e){
				logger.error(e.getStackTrace().toString());
				logger.error("Token lost");
			}
			if(counter%10000 == 0){
				logger.info("Saved "+ String.valueOf(counter) + "tokens");
			}
			counter+=1;
		}
	}
	
	private void saveThreeGramAggregatesToRepository(){
		HashMap<String, Integer> threeGramAggregates = ThreeGramSingleton.getThreeGramFrequencySingleton();
		int counter = 0;
		for(String threeGram : threeGramAggregates.keySet()){
			try{
				threeGramRepo.addThreeGramFrequency(threeGram, threeGramAggregates.get(threeGram));
			}
			catch (Exception e){
				logger.error(e.getStackTrace().toString());
				logger.error("Threegram lost");
			}
			if(counter%100 == 0){
				logger.info("Saved "+ String.valueOf(counter) + "tokens");
			}
			counter+=1;
		}
	}

}
