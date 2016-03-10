package in.mittaluday.file_indexer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoApp {
	public static MongoClient mongoClient=new MongoClient();;
	public static MongoDatabase db=mongoClient.getDatabase("test_ir") ;
	

	public static void makeCollection() throws ClassNotFoundException, IOException{
		Map<String, List<Postings>> index =new HashMap<String, List<Postings>>();
		index=App.getIndex();
		for (String term : index.keySet()) {
			System.out.println(term);
			List<Document> documents =new ArrayList<Document>();
			ArrayList<Postings> postings = (ArrayList<Postings>) index.get(term);
			if (postings != null) {
				for (Postings p : postings) {
					Document doc =new Document("term", term);
					doc.append("document_name", p.getDocumentName());
					doc.append("tfidf",p.getTfidf());
					doc.append("title", p.getDocumentTitle());
//					List<Integer> positions = p.getPositions();
//					int [] posArray = new int[positions.size()];
//					for(int i=0;i<positions.size();i++){
//						posArray[i]=positions.get(i);
//					}
					doc.append("positions",p.getPositions());
					documents.add(doc);
				}
			}	
			db.getCollection("index").insertMany(documents);
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
      makeCollection();

	}

}
