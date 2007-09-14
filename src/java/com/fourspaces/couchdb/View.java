package com.fourspaces.couchdb;


public class View {
	protected String startKey;
	protected String endKey;
	protected Integer count;
	protected Boolean update;
	protected Boolean reverse;
	protected String skip;
	
	protected String name;
	protected Document document;
	protected Database database;
	protected String function;
	
	public View(Database database, Document doc, String name) {
		this.database=database;
		this.name=name;
		this.document=doc;
	}
	
	public View(Database database, Document doc, String name, String function) {
		this.database=database;
		this.name=name;
		this.document=doc;
		this.function=function;
	}
	
	public View(Database database, String name) {
		this.database=database;
		this.name=name;
		this.document=null;
	}
	
	public String getQueryString() {
		String queryString = "";
		if (startKey!=null) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="startkey="+startKey;
		}
		if (endKey!=null) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="endkey="+endKey;
		}
		if (skip!=null) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="skip="+skip;
		}
		if (count!=null) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="count="+count;
		}
		if (update!=null && update.booleanValue()) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="update=true";
		}
		if (reverse!=null && reverse.booleanValue()) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="reverse=true";
		}
		return queryString.equals("") ? null : queryString;
	}
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getEndKey() {
		return endKey;
	}
	public void setEndKey(String endKey) {
		this.endKey = endKey;
	}
	public Boolean getReverse() {
		return reverse;
	}
	public void setReverse(Boolean reverse) {
		this.reverse = reverse;
	}
	public String getSkip() {
		return skip;
	}
	public void setSkip(String skip) {
		this.skip = skip;
	}
	public String getStartKey() {
		return startKey;
	}
	public void setStartKey(String startKey) {
		this.startKey = startKey;
	}
	public Boolean getUpdate() {
		return update;
	}
	public void setUpdate(Boolean update) {
		this.update = update;
	}

	public String getName() {
		return name;
	}
	public String getFullName() {
		if (document!=null) {
			return document.getId()+":"+name;
		}
		return name;
	}

	public String getFunction() {
		return function;
	}
	
}
