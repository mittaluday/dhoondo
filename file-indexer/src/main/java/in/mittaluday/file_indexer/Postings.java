package in.mittaluday.file_indexer;

import java.util.List;

public class Postings {
	
	private String documentName;
	private double tfidf;
	public double getTfidf() {
		return tfidf;
	}

	public void setTfidf(long tf,long df,long corpus) {
		
		this.tfidf = (1+ Math.log(tf)) * (Math.log(Math.abs(corpus)/df));
	}

	private List<Integer> positions;
	
	public Postings(String name,List<Integer> positions) {
		// TODO Auto-generated constructor stub
		this.documentName=name;
		this.positions=positions;
	}
}


