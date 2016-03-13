package in.mittaluday.file_indexer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;


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
//					System.out.println("during mongo db add title :" + p.getDocumentTitle());
					Document doc =new Document("term", term);
					doc.append("document_name", p.getDocumentName());
					doc.append("tfidf",p.getTfidf());
					doc.append("title", p.getDocumentTitle());
					doc.append("positions",p.getPositions());
					doc.append("documentlength", p.getDocumentLength());
					doc.append("docVectorMagnitude", p.getDocVectorMagnitude());
					documents.add(doc);
				}
			}	
			db.getCollection("index").insertMany(documents);
		}		
	}
	
	private static void makeAnchorTextCollection() throws ClassNotFoundException, IOException {
		Map<String, List<Postings>> index =new HashMap<String, List<Postings>>();
		index=App.getAnchorTextIndex();
		for (String term : index.keySet()) {
			System.out.println(term);
			List<Document> documents =new ArrayList<Document>();
			ArrayList<Postings> postings = (ArrayList<Postings>) index.get(term);
			if (postings != null) {
				for (Postings p : postings) {
					Document doc =new Document("term", term);
					doc.append("document_name", p.getDocumentName());
					doc.append("tfidf",p.getTfidf());
					doc.append("documentlength", p.getDocumentLength());
					documents.add(doc);
				}
			}	
			db.getCollection("anchorindex").insertMany(documents);
		}
	}

	private static void addCorpusToCollection() {
		long n = TermIndex.getCorpus();
		boolean hasStatistics = false;
		MongoIterable<String> collectionNames = db.listCollectionNames();
		Iterator<String> it = collectionNames.iterator();
		while(it.hasNext()){
			String collectionName = it.next();
			if(collectionName.equals("statistics")){
				hasStatistics = true;
			}
		}
		if(!hasStatistics){
			db.createCollection("statistics");
		}
		db.getCollection("statistics").drop();
		db.getCollection("statistics").insertOne(new Document("corpussize", n));
	}
	
	private static void makeURLFilenameCollection() {
		Map<String, String> urlToFileName = AnchorTextProcessor.getUrlToFileName();
		List<Document> documents =new ArrayList<Document>();
		int counter=0;
		for(Map.Entry<String, String>entry: urlToFileName.entrySet()){
			counter++;
			if(counter%2000 == 0){
				System.out.println("url: " + entry.getKey() + " filename: " + entry.getValue());
			}
			Document d = new Document();
			d.append("url", entry.getKey());
			d.append("filename", entry.getValue());
			documents.add(d);
		}
		db.getCollection("urltofilename").insertMany(documents);
	}	

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		db.getCollection("index").drop();
		db.getCollection("anchorindex").drop();
		db.getCollection("statistics").drop();
		db.getCollection("urltofilename").drop();
		makeCollection();
		System.out.println("collection done");
		makeAnchorTextCollection();
		System.out.println("anchor text collection done");
		makeURLFilenameCollection();
		System.out.println("URL Filename collection done");
		addCorpusToCollection();
	}

}
