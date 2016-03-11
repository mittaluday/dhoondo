package in.mittaluday.query_engine;

import java.io.IOException;
import java.util.ArrayList;

public class App 
{
    public static void main( String[] args ) throws ClassNotFoundException, IOException
    {
    	QueryProcessorMongo qp = new QueryProcessorMongo();
    	String queryString = "Crista Lopes";
    	ArrayList<String> results = qp.queryIndex(queryString.toLowerCase());
    	if(results.isEmpty()){
    		System.out.println("No results to show");
    	}
    	for(String s : results){
    		System.out.println(s);
    	}
    }
}