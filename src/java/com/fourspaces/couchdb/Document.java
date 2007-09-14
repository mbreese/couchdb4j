package com.fourspaces.couchdb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Document {
	Log log = LogFactory.getLog(Document.class);
	protected Database database=null;
	protected JSONObject object;
	
	boolean loaded = false;
	
	public Document () {
		this.object = new JSONObject();
	}
	public Document (JSONObject obj) {
		this.object = obj;
		loaded=true;
	}
	
	public Document (Database database) {
		this.database=database;
		this.object = new JSONObject();
	}
	
	public Document (String id, String rev, Database database) {
		setId(id);
		setRev(rev);
		this.database=database;
		this.object = new JSONObject();
	}
	
	public void load(JSONObject object2) {
		if (!loaded) {
			object.putAll(object2);
			loaded=true;
		}
	}
	public String getId() {
		return object.optString("_id");
	}
	public void setId(String id)  {
		object.put("_id",id);
	}
	public String getRev()  {
		return object.optString("_rev");
	}
	public void setRev(String rev)  {
		object.put("_rev", rev);
	}
	
	public String[] getRevisions() {
		String[] revs = null;
		if (object.has("_revisions")) {
			JSONArray ar = object.getJSONArray("_revisions");
			revs = new String[ar.size()];
			for (int i=0 ; i< ar.size(); i++) {
				revs[i]=ar.getString(i);
			}
		}
		return revs;
	}
	
//	public JSONObject getValue()  {
//		if (!object.has("value") && database!=null && getId()!=null&& !getId().equals("")) {
//			log.info("Retriving document value for: "+getId());
//			Document doc = database.getDocument(getId());
//			setValue(doc.getValue());
//		} else if (!object.has("value")) {
//			object.put("value", new JSONObject());
//		}
//		return object.getJSONObject("value");
//	}
//	public void setValue(JSONObject value)  {
//		object.put("value", value);
//	}
	
	public View getView(String name) {
		View view = null;
		if (object.has("_view_"+name)) {
			view = new View(database,this,name);
		}
		return view;
	}
	
	public View addView(String viewName, String function) {
		View view = new View(database,this,viewName, "\""+function+"\"");
		object.put("_view_"+viewName, "\""+function+"\"");
		return view;
	}
	
	public void deleteView(String viewName) {
		object.remove("_view_"+viewName);
	}
	
	public void setDatabase(Database database) {
		this.database=database;
	}
	
	public JSONObject getJSONObject() {
		if (!loaded && database!=null && getId()!=null && !getId().equals("")) {
			Document doc = database.getDocument(getId());
			log.info("Loading: "+doc.getJSONObject());
			load(doc.getJSONObject());
		}
		return object;
	}
	
	public String toString() {
		return object.toString();
	}
	
	/*
	 * Delegate methods to the JSON Object.
	 */	
	public JSONObject accumulate(String arg0, boolean arg1) {
		return getJSONObject().accumulate(arg0, arg1);
	}
	public JSONObject accumulate(String arg0, double arg1) {
		return getJSONObject().accumulate(arg0, arg1);
	}
	public JSONObject accumulate(String arg0, int arg1) {
		return getJSONObject().accumulate(arg0, arg1);
	}
	public JSONObject accumulate(String arg0, long arg1) {
		return getJSONObject().accumulate(arg0, arg1);
	}
	public JSONObject accumulate(String arg0, Object arg1) {
		return getJSONObject().accumulate(arg0, arg1);
	}
	public void accumulateAll(Map arg0) {
		object.accumulateAll(arg0);
	}
	public void clear() {
		object.clear();
	}
	public boolean containsKey(Object arg0) {
		return getJSONObject().containsKey(arg0);
	}
	public boolean containsValue(Object arg0) {
		return getJSONObject().containsValue(arg0);
	}
	public JSONObject element(String arg0, boolean arg1) {
		return getJSONObject().element(arg0, arg1);
	}
	public JSONObject element(String arg0, Collection arg1) {
		return getJSONObject().element(arg0, arg1);
	}
	public JSONObject element(String arg0, double arg1) {
		return getJSONObject().element(arg0, arg1);
	}
	public JSONObject element(String arg0, int arg1) {
		return getJSONObject().element(arg0, arg1);
	}
	public JSONObject element(String arg0, long arg1) {
		return getJSONObject().element(arg0, arg1);
	}
	public JSONObject element(String arg0, Map arg1) {
		return getJSONObject().element(arg0, arg1);
	}
	public JSONObject element(String arg0, Object arg1) {
		return getJSONObject().element(arg0, arg1);
	}
	public JSONObject elementOpt(String arg0, Object arg1) {
		return getJSONObject().elementOpt(arg0, arg1);
	}
	public Set entrySet() {
		return getJSONObject().entrySet();
	}
	public Object get(Object arg0) {
		return getJSONObject().get(arg0);
	}
	public Object get(String arg0) {
		return getJSONObject().get(arg0);
	}
	public boolean getBoolean(String arg0) {
		return getJSONObject().getBoolean(arg0);
	}
	public double getDouble(String arg0) {
		return getJSONObject().getDouble(arg0);
	}
	public int getInt(String arg0) {
		return getJSONObject().getInt(arg0);
	}
	public JSONArray getJSONArray(String arg0) {
		return getJSONObject().getJSONArray(arg0);
	}
	public JSONObject getJSONObject(String arg0) {
		return getJSONObject().getJSONObject(arg0);
	}
	public long getLong(String arg0) {
		return getJSONObject().getLong(arg0);
	}
	public String getString(String arg0) {
		return getJSONObject().getString(arg0);
	}
	public boolean has(String arg0) {
		return getJSONObject().has(arg0);
	}
	public Iterator keys() {
		return getJSONObject().keys();
	}
	public Set keySet() {
		return getJSONObject().keySet();
	}
	public JSONArray names() {
		return getJSONObject().names();
	}
	public Object opt(String arg0) {
		return getJSONObject().opt(arg0);
	}
	public boolean optBoolean(String arg0, boolean arg1) {
		return getJSONObject().optBoolean(arg0, arg1);
	}
	public boolean optBoolean(String arg0) {
		return getJSONObject().optBoolean(arg0);
	}
	public double optDouble(String arg0, double arg1) {
		return getJSONObject().optDouble(arg0, arg1);
	}
	public double optDouble(String arg0) {
		return getJSONObject().optDouble(arg0);
	}
	public int optInt(String arg0, int arg1) {
		return getJSONObject().optInt(arg0, arg1);
	}
	public int optInt(String arg0) {
		return getJSONObject().optInt(arg0);
	}
	public JSONArray optJSONArray(String arg0) {
		return getJSONObject().optJSONArray(arg0);
	}
	public JSONObject optJSONObject(String arg0) {
		return getJSONObject().optJSONObject(arg0);
	}
	public long optLong(String arg0, long arg1) {
		return getJSONObject().optLong(arg0, arg1);
	}
	public long optLong(String arg0) {
		return getJSONObject().optLong(arg0);
	}
	public String optString(String arg0, String arg1) {
		return getJSONObject().optString(arg0, arg1);
	}
	public String optString(String arg0) {
		return getJSONObject().optString(arg0);
	}
	public Object put(Object arg0, Object arg1) {
		return getJSONObject().put(arg0, arg1);
	}
	public void putAll(Map arg0) {
		object.putAll(arg0);
	}
	public Object remove(Object arg0) {
		return getJSONObject().remove(arg0);
	}
	public Object remove(String arg0) {
		return getJSONObject().remove(arg0);
	}
	public int size() {
		return getJSONObject().size();
	}
	public Collection values() {
		return getJSONObject().values();
	}
}
