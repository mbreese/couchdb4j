package com.fourspaces.couchdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CouchResponse {
	Log log = LogFactory.getLog(CouchResponse.class);
	
	private String body;
	private Header[] headers;
	private int statusCode;
//	private String statusMessage;
	private String methodName;
	
	boolean ok = false;

	private JSONObject error;
	
	public CouchResponse(HttpMethod method) throws IOException {
		this.methodName=method.getName();
		String line = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		while ((line=reader.readLine())!=null) {
			if (body==null) {
				body = "";
			} else {
				body +="\n";
			}
			body+=line;
		}
		log.info("["+method.getName()+"] "+method.getPath()+((method.getQueryString()!=null) ?"?"+method.getQueryString():"")+" ["+method.getStatusCode()+"] "+" => "+body);
		this.headers = method.getResponseHeaders();
		this.statusCode=method.getStatusCode();
//		this.statusMessage=method.getStatusText();
		
		if (
				(methodName.equals("GET") && this.statusCode==404) || 
				(methodName.equals("PUT") && this.statusCode==409) ||
				(methodName.equals("POST") && this.statusCode==404) ||
				(methodName.equals("DELETE") && this.statusCode==404) 
			){
			
				error = JSONObject.fromObject(body).getJSONObject("error");
		} else if (
				(methodName.equals("PUT") && this.statusCode==201) ||
				(methodName.equals("POST") && this.statusCode==201) ||
				(methodName.equals("DELETE") && this.statusCode==202) 
			) {
				ok = JSONObject.fromObject(body).getBoolean("ok");
			
		} else if ((method.getName().equals("GET") || method.getName().equals("POST")) && this.statusCode==200) {
			ok=true;
		}
	}

	public JSONArray getBodyAsJSONArray() {		
		return JSONArray.fromObject(body);
	}

	public boolean isOk() {
		return ok;
	}
	
	public String getErrorId() {
		if (error!=null) {
			return error.getString("id");
		}
		return null;
	}
	public String getErrorReason() {
		if (error!=null) {
			return error.getString("reason");
		}
		return null;
	}

	public JSONObject getBodyAsJSON() {
		if (body==null) {
			return null;
		}
		return JSONObject.fromObject(body);
	}

	public String getHeader(String key) {
		for (Header h: headers) {
			if (h.getName().equals(key)) {
				return h.getValue();
			}
		}
		return null;
	}

	public int getStatusCode() {
		return this.statusCode;
	}
}
