package com.phamousapps.trendalert.data;

public class Venue {

	String id;
	String name;
	FsContact contact;
	FsLocation location;
	String canonicalUrl;
	FsCategory[] categories;
	boolean verified;
	boolean restricted;
	String url;
	FsLikes likes;
	// FsSpecials[] specials; // TODO Why is this an array?
	FsHereNow hereNow;
	FsVenuePage venuePage;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public FsContact getContact() {
		return contact;
	}

	public FsLocation getLocation() {
		return location;
	}

	public String getCanonicalUrl() {
		return canonicalUrl;
	}

	public FsCategory[] getCategories() {
		return categories;
	}

	public boolean isVerified() {
		return verified;
	}

	public boolean isRestricted() {
		return restricted;
	}

	public String getUrl() {
		return url;
	}

	public FsLikes getLikes() {
		return likes;
	}

//	public FsSpecials[] getSpecials() {
//		return specials;
//	}

	public FsHereNow getHereNow() {
		return hereNow;
	}

	public FsVenuePage getVenuePage() {
		return venuePage;
	}

}
