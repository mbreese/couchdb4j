package com.fourspaces.couchdb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Session {
	Log log = LogFactory.getLog(Session.class);
	protected final String host;
	protected final int port;
	protected final boolean secure;
	
	protected CouchResponse lastResponse;
	
	protected HttpClient httpClient = new HttpClient();

	public Session(String host, int port) {
		this.host=host;
		this.port=port;
		this.secure=false;
	}
	public Session(String host, int port, boolean secure) {
		this.host=host;
		this.port=port;
		this.secure=secure;
	}
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	public List<String> getDatabaseNames() {
		CouchResponse resp = get("_all_dbs");
		JSONArray ar = resp.getBodyAsJSONArray();
		
		List<String> dbs = new ArrayList<String>(ar.size());
		for (int i=0 ; i< ar.size(); i++) {
			dbs.add(ar.getString(i));
		}
		return dbs;	
	}
	public Database getDatabase(String name) {
		CouchResponse resp = get(name);
		if (resp.isOk()) {
			return new Database(resp.getBodyAsJSON(),this);
		} else {
			log.warn("Error getting database: "+name);
		}
		return null;
	}
	public Database createDatabase(String name) {
		String dbname = name.toLowerCase().replaceAll("[^a-z0-9_$()+\\-/]", "_");
		if (!dbname.endsWith("/")) {
			dbname+="/";
		}
		CouchResponse resp = put(dbname);
		if (resp.isOk()) {
			return getDatabase(dbname);
		} else {
			log.warn("Error creating database: "+name);
			return null;
		}
	}
	public boolean deleteDatabase(String name) {
		return delete(name).isOk();		
	}
	
	protected String buildUrl(String url) {
		return (secure) ? "https://"+host+":"+port+"/"+url : "http://"+host+":"+port+"/"+url;
	}
	
	CouchResponse delete(String url) {
		DeleteMethod del = new DeleteMethod(buildUrl(url));
		return http(del);
	}
	
	CouchResponse post(String url) {
		return post(url,null,null);
	}
	CouchResponse post(String url, String content) {
		return post(url,content,null);
	}
	CouchResponse post(String url, String content, String queryString) {
		PostMethod post = new PostMethod(buildUrl(url));
		if (content!=null) {
			RequestEntity entity;
			try {
				entity = new StringRequestEntity(content, "application/json","UTF-8");
				post.setRequestEntity(entity);
			} catch (UnsupportedEncodingException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		if (queryString!=null) {
			post.setQueryString(queryString);
		}
		return http(post);
	}
	
	CouchResponse put(String url) {
		return put(url,null);
	}
	CouchResponse put(String url, String content) {
		PutMethod put = new PutMethod(buildUrl(url));
		if (content!=null) {
			RequestEntity entity;
			try {
				entity = new StringRequestEntity(content, "application/json","UTF-8");
				put.setRequestEntity(entity);
			} catch (UnsupportedEncodingException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		return http(put);
	}
	
	CouchResponse get(String url) {
		GetMethod get = new GetMethod(buildUrl(url));
		return http(get);
	}
	CouchResponse get(String url, NameValuePair[] queryParams) {
		GetMethod get = new GetMethod(buildUrl(url));
		get.setQueryString(queryParams);
		return http(get);
	}
	CouchResponse get(String url, String queryString) {
		GetMethod get = new GetMethod(buildUrl(url));
		get.setQueryString(queryString);
		return http(get);
	}
	
	protected CouchResponse http(HttpMethod method) {
		try {
			httpClient.executeMethod(method);
			lastResponse = new CouchResponse(method);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			  method.releaseConnection();
		}
		return lastResponse;	
	}

	public String getLastErrorId() {
		return lastResponse.getErrorId();
	}
	public String getLastErrorReason() {
		return lastResponse.getErrorReason();
	}
}
