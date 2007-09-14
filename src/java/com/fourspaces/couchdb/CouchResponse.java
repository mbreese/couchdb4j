/*
   Copyright 2007 Fourspaces Consulting, LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

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

/**
 * The CouchResponse parses the HTTP response returned by the CouchDB server.
 * This is almost never called directly by the user, but indirectly through
 * the Session and Database objects.
 * <p>
 * Given a CouchDB response, it will determine if the request was successful 
 * (status 200,201,202), or was an error.  If there was an error, it parses the returned json error
 * message.
 * 
 * @author mbreese
 *
 */
public class CouchResponse {
	Log log = LogFactory.getLog(CouchResponse.class);
	
	private String body;
	private String path;
	private Header[] headers;
	private int statusCode;
	private String methodName;
	
	boolean ok = false;

	private JSONObject error;
	
	/**
	 * C-tor parses the method results to build the CouchResponse object.
	 * First, it reads the body (hence the IOException) from the method
	 * Next, it checks the status codes to determine if the request was successful.
	 * If there was an error, it parses the error codes.
	 * @param method
	 * @throws IOException
	 */
	CouchResponse(HttpMethod method) throws IOException {
		methodName=method.getName();
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
		path = method.getPath();
		if (method.getQueryString()!=null && !method.getQueryString().equals("")) {
			path += "?"+method.getQueryString();
		}
		headers = method.getResponseHeaders();
		statusCode=method.getStatusCode();
		
		if (
				(methodName.equals("GET") && statusCode==404) || 
				(methodName.equals("PUT") && statusCode==409) ||
				(methodName.equals("POST") && statusCode==404) ||
				(methodName.equals("DELETE") && statusCode==404) 
			){
			
				error = JSONObject.fromObject(body).getJSONObject("error");
		} else if (
				(methodName.equals("PUT") && statusCode==201) ||
				(methodName.equals("POST") && statusCode==201) ||
				(methodName.equals("DELETE") && statusCode==202) 
			) {
				ok = JSONObject.fromObject(body).getBoolean("ok");
			
		} else if ((method.getName().equals("GET") || method.getName().equals("POST")) && statusCode==200) {
			ok=true;
		}
		log.debug(toString());
	}

	@Override
	/**
	 * A better toString for this object... can be very verbose though.
	 */
	public String toString() {
		return "["+methodName+"] "+path+" ["+statusCode+"] "+" => "+body;
	}
	
	/**
	 * Retrieves the body of the request as a JSONArray object. (such as listing database names)
	 * @return
	 */
	public JSONArray getBodyAsJSONArray() {		
		return JSONArray.fromObject(body);
	}

	/**
	 * Was the request successful?
	 * @return
	 */
	public boolean isOk() {
		return ok;
	}
	
	/**
	 * What was the error id?
	 * @return
	 */
	public String getErrorId() {
		if (error!=null) {
			return error.getString("id");
		}
		return null;
	}
	
	/**
	 * what was the error reason given?
	 * @return
	 */
	public String getErrorReason() {
		if (error!=null) {
			return error.getString("reason");
		}
		return null;
	}

	/**
	 * Returns the body of the response as a JSON Object (such as for a document)
	 * @return
	 */
	public JSONObject getBodyAsJSON() {
		if (body==null) {
			return null;
		}
		return JSONObject.fromObject(body);
	}

	/**
	 * Retrieves a specific header from the response (not really used anymore)
	 * @param key
	 * @return
	 */
	public String getHeader(String key) {
		for (Header h: headers) {
			if (h.getName().equals(key)) {
				return h.getValue();
			}
		}
		return null;
	}
}
