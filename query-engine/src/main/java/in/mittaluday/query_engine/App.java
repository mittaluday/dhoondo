package in.mittaluday.query_engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class App 
{
    public static void main( String[] args ) throws ClassNotFoundException, IOException
    {
    	QueryProcessor qp = new QueryProcessor();
    	String queryString = "data";
    	ArrayList<String> results = qp.queryIndex(queryString);
    	if(results.isEmpty()){
    		System.out.println("No results to show");
    	}
    	for(String s : results){
    		System.out.println(s);
    	}
    }
}
