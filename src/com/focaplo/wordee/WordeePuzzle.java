package com.focaplo.wordee;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class WordeePuzzle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long puzzleId;

	@Persistent
	private String authorName;
	@Persistent
	private String puzzleName;
	@Persistent(defaultFetchGroup="true")
	private Text puzzleData;
	@Persistent
	private Date uploadedDate;
	@Persistent
	private long downloadCount;
	@Persistent
	private double rate;




	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public long getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(long downloadCount) {
		this.downloadCount = downloadCount;
	}
	public Long getPuzzleId() {
		return puzzleId;
	}
	public void setPuzzleId(Long puzzleId) {
		this.puzzleId = puzzleId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getPuzzleName() {
		return puzzleName;
	}
	public void setPuzzleName(String puzzleName) {
		this.puzzleName = puzzleName;
	}

	public Text getPuzzleData() {
		return puzzleData;
	}
	public void setPuzzleData(Text puzzleData) {
		this.puzzleData = puzzleData;
	}
	public Date getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	
}
