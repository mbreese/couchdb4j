package com.fourspaces.couchdb;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ViewResults extends Document {
	Log log = LogFactory.getLog(ViewResults.class);

	public ViewResults(JSONObject obj) {
		super(obj);
	}
	
	public List<Document> getResults() {
		JSONArray ar = getJSONObject().getJSONArray("rows");
		List<Document> docs = new ArrayList<Document>(ar.size());
		for (int i=0 ; i< ar.size(); i++) {
			log.info(ar.getString(i));
			if (ar.get(i)!=null && !ar.getString(i).equals("null")) {
				Document d = new Document(ar.getJSONObject(i));
				d.setDatabase(database);
				docs.add(d);
			}
		}
		return docs;	

	}
}
