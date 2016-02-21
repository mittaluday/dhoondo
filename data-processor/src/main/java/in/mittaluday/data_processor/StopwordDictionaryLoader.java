package in.mittaluday.data_processor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class StopwordDictionaryLoader {


	public static HashSet<String> createAnagramDictionary(String filename) throws IOException {

		System.out.println("Creating hashmap");
		HashSet<String> stopworddict = new HashSet<String>();
		File temp = new File(filename);
		Scanner scanner = new Scanner(temp);
		while (scanner.hasNext()) {
			String word = scanner.nextLine().toLowerCase();
			if (!stopworddict.contains(word)) {
				stopworddict.add(word);
			} 

		}
	//	System.out.println("Created hashmap. Processed " + String.valueOf(wordCount) + " words.");
		return stopworddict;

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HashSet<String> stopworddict;

		stopworddict = createAnagramDictionary("stopwords.txt");		
		String stopwordDictFilePath = "stopwords.ser";
		File stopwordDictFile = new File(stopwordDictFilePath);
		if(!stopwordDictFile.exists()) {
		    stopwordDictFile.createNewFile();
		}
		FileOutputStream f = new FileOutputStream(stopwordDictFile, false);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(stopworddict);
		s.close();
	}

}
