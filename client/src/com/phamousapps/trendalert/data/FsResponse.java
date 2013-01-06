package com.phamousapps.trendalert.data;

public class FsResponse {

	Meta meta;
	Response response;
	
	public static class Meta {
		int code;
		
		public int getMeta(){
			return code;
		}
	}
	
	public static class Response{
		Venue[] venues;

		public Venue[] getVenues() {
			return venues;
		}
	}

	public Meta getMeta() {
		return meta;
	}

	public Response getResponse() {
		return response;
	}
}
