package com.dylan.lang.http;

import org.apache.commons.httpclient.Header;

public class HttpResponse {
	private int status = 404;
	private String response;
	private Header[] headers;
	
	public Header[] getHeaders() {
		return headers;
	}
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
}
