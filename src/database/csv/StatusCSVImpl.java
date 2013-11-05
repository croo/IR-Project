package database.csv;

import java.util.Date;

import logic.Classification;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

public class StatusCSVImpl implements Status {

	private static final long serialVersionUID = 1L;
	private Date createdAt;
	private String text;
	private User user;
	private Classification label;

	@Override
	public int compareTo(Status o) {
		return createdAt.compareTo(o.getCreatedAt());
	}

	public void setUser(String userName) {
		user = new UserCSVImpl(userName);
	}
	
	@Override
	public User getUser() {
		return user;
	}
	
	public void setDate(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTruncated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getInReplyToStatusId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getInReplyToUserId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getInReplyToScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeoLocation getGeoLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Place getPlace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFavorited() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRetweet() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Status getRetweetedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long[] getContributors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getRetweetCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isRetweetedByMe() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getCurrentUserRetweetId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isPossiblySensitive() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public RateLimitStatus getRateLimitStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAccessLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserMentionEntity[] getUserMentionEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URLEntity[] getURLEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashtagEntity[] getHashtagEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MediaEntity[] getMediaEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	public Classification getLabel() {
		return label;
	}

	public void setLabel(Classification label) {
		this.label = label;
	}


}
