package in.mittaluday.crawler;

public class LinksCounter {
	private int incomingLinks;
	private int outgoingLinks;
	
	public LinksCounter() {
		incomingLinks = 0;
		outgoingLinks = 0;		
	}
	
	public int getIncomingLinks(){
		return incomingLinks;
	}
	
	public int getOutgoingLinks(){
		return outgoingLinks;
	}
	
	public void setIncomingLinks(int incomingLinks){
		this.incomingLinks = incomingLinks;
	}
	
	public void setOutgoingLinks(int outgoingLinks){
		this.outgoingLinks = outgoingLinks;
	}
	
	public void incrementIncomingLinks(int incrementBy){
		this.incomingLinks += incrementBy;
	}
	
	public void incrementOutgoingLinks(int incrementBy){
		this.outgoingLinks += incrementBy;
	}

}
