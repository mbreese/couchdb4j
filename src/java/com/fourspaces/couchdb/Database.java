package com.fourspaces.couchdb;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Database {
	Log log = LogFactory.getLog(Database.class);
	private final String name;
	private int documentCount;
	private int updateSeq;
	
	private Session session;
	
	public Database(JSONObject json, Session session) {
		name = json.getString("db_name");
		documentCount = json.getInt("doc_count");
		updateSeq = json.getInt("update_seq");
		
		this.session = session;
	}
	public String getName() {
		return name;
	}
	public int getDocumentCount() {
		return documentCount;
	}
	public int getUpdateSeq() {
		return updateSeq;
	}

	public ViewResults getAllDocuments() {
		return view(new View(this,null,"_all_docs"));
	}

	public ViewResults view(View view) {
		CouchResponse resp = session.get(name+"/"+view.getFullName(), view.getQueryString());		
		if (resp.isOk()) {
			ViewResults results = new ViewResults(resp.getBodyAsJSON());
			results.setDatabase(this);
			return results;
		}
		return null;
	}
	
	public ViewResults view(String name) {
		return view(new View(this,name));
	}
	
	public ViewResults adhoc(String function) {
		return adhoc(new AdHocView(this,function));
	}
	
	public ViewResults adhoc(View view) {
		CouchResponse resp = session.post(name+"/"+view.getFullName(), view.getFunction(), view.getQueryString());
		if (resp.isOk()) {
			ViewResults results = new ViewResults(resp.getBodyAsJSON());
			results.setDatabase(this);
			return results;
		} else {
			log.warn("Error executing view - "+resp.getErrorId()+" "+resp.getErrorReason()+" - "+ resp.getStatusCode());
		}
		return null;
	}
	
	public void saveDocument(Document doc, String docId) {
		CouchResponse resp;
		if (docId==null || docId.equals("")) {
			resp= session.post(name+"/",doc.getJSONObject().toString());
		} else {
			resp= session.put(name+"/"+docId,doc.getJSONObject().toString());
		}
		
		if (resp.isOk()) {
			try {
				if (doc.getId()==null || doc.getId().equals("")) {
					doc.setId(resp.getBodyAsJSON().getString("_id"));
				}
				doc.setRev(resp.getBodyAsJSON().getString("_rev"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			doc.setDatabase(this);
		} else {
			log.warn("Error adding document - "+resp.getErrorId()+" "+resp.getErrorReason());
		}
	}
	public void saveDocument(Document doc) {
		saveDocument(doc,doc.getId());
	}
	
	public Document getDocument(String id) {
		return getDocument(id,null,false);
	}
	public Document getDocumentWithRevisions(String id) {
		return getDocument(id,null,true);
	}
	public Document getDocument(String id, String revision, boolean showRevisions) {
		CouchResponse resp;
		Document doc = null;
		if (revision!=null && showRevisions) {
			resp=session.get(name+"/"+id,"rev="+revision+"&full=true");
		} else if (revision!=null && !showRevisions) {
			resp=session.get(name+"/"+id,"rev="+revision);
		} else if (revision==null && showRevisions) {
			resp=session.get(name+"/"+id,"full=true");
		} else {
			resp=session.get(name+"/"+id);
		}
		if (resp.isOk()) {
			doc = new Document(resp.getBodyAsJSON());
			doc.setDatabase(this);
		} else {
			log.warn("Error getting document - "+resp.getErrorId()+" "+resp.getErrorReason());
		}
		return doc;
	}
	public boolean deleteDocument(Document d) {
		return session.delete(name+"/"+d.getId()).isOk();
	}
}
