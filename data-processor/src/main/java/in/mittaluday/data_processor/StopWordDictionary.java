package in.mittaluday.data_processor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Scanner;

public class StopWordDictionary
{
    private static Scanner scanner;

    public static HashSet<String> createStopWordDictionary(String filename) throws IOException
    {
	HashSet<String> stopWorddict = new HashSet<String>();
	File temp = new File(filename);
	scanner = new Scanner(temp);
	while (scanner.hasNext())
	{
	    String word = scanner.nextLine().toLowerCase();
	    stopWorddict.add(word);
	}
	return stopWorddict;

    }

    public static void main(String[] args) throws IOException
    {
	// TODO Auto-generated method stub
	HashSet<String> stopWorddict;

	stopWorddict = createStopWordDictionary("/home/purvi/Desktop/info-retrieval/EnglishStopWords");

	String stopWordDictFilePath = "/home/purvi/Desktop/info-retrieval/stopwords.ser";
	File stopWordDictFile = new File(stopWordDictFilePath);
	if (!stopWordDictFile.exists())
	{
	    stopWordDictFile.createNewFile();
	}
	FileOutputStream f = new FileOutputStream(stopWordDictFile, false);
	ObjectOutputStream s = new ObjectOutputStream(f);
	s.writeObject(stopWorddict);
	s.close();
    }
}