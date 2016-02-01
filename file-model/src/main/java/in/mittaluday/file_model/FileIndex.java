package in.mittaluday.file_model;

import java.util.Date;

public class FileIndex {
	private Integer id;
	private String fileName;
	private Date dateOfLastUpdate;
	private Date dateOfLastIndex;
	private Integer numberOfTokens;
	private String subdomain;
	
	public FileIndex(String fileName, Date dateOfLastUpdate, Date dateOfLastIndex,
			Integer numberOfTokens, String subdomain){
		this.fileName = fileName;
		this.dateOfLastUpdate = dateOfLastUpdate;
		this.dateOfLastIndex = dateOfLastIndex;
		this.numberOfTokens = numberOfTokens;
		this.subdomain = subdomain;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getDateOfLastUpdate() {
		return dateOfLastUpdate;
	}
	public void setDateOfLastUpdate(Date dateOfLastUpdate) {
		this.dateOfLastUpdate = dateOfLastUpdate;
	}
	public Date getDateOfLastIndex() {
		return dateOfLastIndex;
	}
	public void setDateOfLastIndex(Date dateOfLastIndex) {
		this.dateOfLastIndex = dateOfLastIndex;
	}
	public Integer getNumberOfTokens() {
		return numberOfTokens;
	}
	public void setNumberOfTokens(Integer numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}
	public String getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}
	
	

}
