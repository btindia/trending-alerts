package com.phamousapps.trendalert.utils;

public class FsSettings {

	public static final String CLIENT_ID = "5Z121CYZ10RRSWXMTUJ0WQN1GUYCW1WLWSNAHNVGZ5PXQ325";
	public static final String CLIENT_SECRET = "CZT3ZGYLMX01SPWF1HULLPRECEEBWMORE0INTHT1FSGYIED5";
	
	public static final String URL_TRENDING = "https://api.foursquare.com/v2/venues/trending";
	public static final String URL_SEARCH = "https://api.foursquare.com/v2/venues/search";
	
	public static String URL_TRENDING_AUTH;
	public static String URL_SEARCH_AUTH;

	static{
		StringBuilder builder = new StringBuilder();
		builder.append("?client_id=").append(CLIENT_ID);
		builder.append("&client_secret=").append(CLIENT_SECRET);
		builder.append("&v=20130101");
		String auth = builder.toString();
		
		builder = new StringBuilder();
		builder.append(URL_TRENDING).append(auth);
		URL_TRENDING_AUTH = builder.toString();
		
		builder = new StringBuilder();
		builder.append(URL_SEARCH).append(auth);
		URL_SEARCH_AUTH = builder.toString();
	}
}
