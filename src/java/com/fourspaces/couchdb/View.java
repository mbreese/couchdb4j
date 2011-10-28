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

/**
 * The View is the mechanism for performing Querys on a CouchDB instance.
 * The view can be named or ad-hoc (see AdHocView). (Currently [14 Sept 2007] named view aren't working in the 
 * mainline CouchDB code... but this _should_ work.)
 *<p>
 * The View object exists mainly to apply filtering to the view.  Otherwise, views can be 
 * called directly from the database object by using their names (or given an ad-hoc query).
 * 
 * @author mbreese
 *
 */
public class View {
	protected String key;
	protected String startKey;
	protected String endKey;
	protected Integer limit;
	protected Boolean update;
	protected Boolean reverse;
	protected String skip;
    protected Boolean group;
	protected Boolean includeDocs;
	
	protected String name;
	protected Document document;
	protected String function;
	
	/**
	 * Build a view given a document and a name
	 * 
	 * @param doc
	 * @param name
	 */
	public View(Document doc, String name) {
		this.document=doc;
		this.name=name;
	}
	
	/**
	 * Build a view given only a fullname ex: ("_add_docs", "_temp_view")
	 * @param fullname
	 */
	public View(String fullname) {
		this.name=fullname;
		this.document=null;
	}
	
	/**
	 * Builds a new view for a document, a given name, and the function definition.
	 * This <i>does not actually add it to the document</i>.  That is handled by
	 * Document.addView()
	 * <p>
	 * This constructor should only be called by Document.addView();
	 * 
	 * @param doc
	 * @param name
	 * @param function
	 */
	View(Document doc, String name, String function) {
		this.name=name;
		this.document=doc;
		this.function=function;
	}
	
	/**
	 * Based upon settings, builds the queryString to add to the URL for this view.
	 * 
	 * 
	 * @return
	 */
	public String getQueryString() {
		String queryString = "";
		if (key!=null) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="key="+key;
		}
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
		if (limit!=null) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="limit="+limit;
		}
		if (update!=null && update.booleanValue()) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="update=true";
		}
		if (includeDocs!=null && includeDocs.booleanValue()) {
			if (!queryString.equals("")) { queryString+="&"; }
			queryString+="include_docs=true";
		}
		if (reverse!=null && reverse.booleanValue()) {
			if (!queryString.equals("")) { queryString+="&"; }
		 			queryString+="descending=true";
		}
        if (group!=null && group.booleanValue()) {
        	if (!queryString.equals("")) { queryString+="&"; }
        		queryString+="group=true";		
        }
		return queryString.equals("") ? null : queryString;
		                 
	}
	
	/**
	 * The number of entries to return
	 * @param count
         * @deprecated CouchDB 0.9 uses limit instead
	 */
	public void setCount(Integer count) {
		//this.count = count;
		setLimit(count);
	}

        public void setKey(String key) {
          this.key = key;
        }

        public void setLimit(Integer limit) {
          this.limit = limit;
        }

        public void setGroup(Boolean group) {
          this.group = group;
        }
  
	/**
	 * Stop listing at this key
	 * @param endKey
	 */
	public void setEndKey(String endKey) {
		this.endKey = endKey;
	}
	/**
	 * Reverse the listing
	 * @param reverse
     * @deprecated CouchDB 0.9 uses "descending" instead
	 */
	public void setReverse(Boolean reverse) {
		this.reverse = reverse;
	}

    public void setDescending(Boolean descending) {
        this.reverse = descending;
    }
	/**
	 * Skip listing these keys (not sure if this works, or the format)
	 * @param skip
	 */
	public void setSkip(String skip) {
		this.skip = skip;
	}
	/**
	 * Start listing at this key
	 * @param startKey
	 */
	public void setStartKey(String startKey) {
		this.startKey = startKey;
	}
	/**
	 * Not sure... might be for batch updates, but not sure.
	 * @param update
	 */
	public void setUpdate(Boolean update) {
		this.update = update;
	}

    public void setWithDocs(Boolean withDocs) {
		this.includeDocs = withDocs;
	}
	
	/**
	 * The name for this view (w/o doc id)
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * the full name for this view (w/ doc id, if avail)
	 * in the form of : 
	 * "docid:name"
	 * or 
	 * "name"
	 * @return
	 */
	public String getFullName() {
		return (document==null) ? name: document.getViewDocumentId()+"/"+name;
	}

	/**
	 * The function definition for this view, if it is available.
	 * @return
	 */
	public String getFunction() {
		return function;
	}
	
}
