/**
 * Copyright (c) 2012 Raymond Wilson (http://www.bytefoundry.co.uk) 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.fourspaces.couchdb;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/**
 * The 'Update' is a mechanism for executing document update handlers against existing documents. These
 * handlers are server-side functions that exist in your database's design documents that allow you to do things
 * such as providing a server-side last modified timestamp, updating individual fields in a document without 
 * first getting the latest revision, etc. See the following web page for more information:
 * <p/>
 * http://wiki.apache.org/couchdb/Document_Update_Handlers
 * 
 * @author rwilson
 *
 */
public class Update {
	protected String name;
	protected String docId;
	protected boolean usePOST;
	protected List<NameValuePair> params;
	
	/**
	 * Standard c-tor
	 * @param name The name should be in the format "[designDoc]/[update]" e.g "accounts/personal" (corresponding
	 *     to "_design/accounts/_update/personal/").
	 */
	public Update(String name) {
		this.name = name;		
		docId = null;
		usePOST = false;
		params = new ArrayList<NameValuePair>();
	}	
		
	/**
	 * Overloaded C-tor with ID of document to update specified.
	 * @param name
	 * @param docId
	 */
	public Update(String name, String docId) {
		this.name = name;
		this.docId = docId;
		usePOST = false;
		params = new ArrayList<NameValuePair>();
	}
	
	/**
	 * Overloaded c-tor with doc ID and whether or not to use POST method specified. Note the default
	 * update method is PUT with the data passed via a query string. 
	 * @param name
	 * @param docId
	 * @param usePOST
	 */
	public Update(String name, String docId, boolean usePOST) {
		this.name = name;
		this.docId = docId;
		this.usePOST = usePOST;
		params = new ArrayList<NameValuePair>();
	}
	
	/**
	 * The ID of the document to update. This is a requirement and the update will fail
	 * without it.
	 * @param docId
	 */
	public void setDocId(String docId) {
		this.docId = docId;
	}
	
	/**
	 * Get the ID of the document to update.
	 * @return
	 */
	public String getDocId() {
		return docId;
	}
	
	/**
	 * The name of design document and the update function in the format "[designDoc]/[updateFunc]" e.g. 
	 * "accounts/personal" (corresponding to "_design/accounts/_update/personal").
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the name of the update handler.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set whether or not to use POST rather than PUT. Note that your document update handler must be 
	 * written to use POST parameters (e.g. 'req.form.field' as opposed to 'req.query.field').
	 * @param usePOST
	 */
	public void setMethodPOST(boolean usePOST) {
		this.usePOST = usePOST;
	}
	
	/**
	 * Get whether or not to use the POST method.
	 * @return
	 */
	public boolean getMethodPOST() {
		return usePOST;
	}
	
	/**
	 * Add a key/value parameter to be passed to the document update handler function. Note that if 
	 * the key already exists, all instances of it will be removed prior to insertion.
	 * @param key
	 * @param value
	 */
	public void addParameter(String key, String value) {
		if ((key == null) || (key.equals(""))) {
			return;
		}
		
		removeParameter(key);
		
		params.add(new BasicNameValuePair(key, value));
	}
	
	/**
	 * Remove a key/value parameter from being passed to the document update handler function. If 
	 * multiple instances of the key exists they will all be removed.
	 * @param key
	 */
	public void removeParameter(String key) {
		if ((key == null) || (key.equals(""))) {
			return;
		}
		
		List<NameValuePair> toRemove = new ArrayList<NameValuePair>();
		
		for (NameValuePair p : params) {
			if (p.getName().equals(key)) {
				toRemove.add(p);
			}
		}		
		
		for (NameValuePair p : toRemove) {
			params.remove(p);
		}
	}
	
	/**
	 * Return the parameters as a query string. You're unlikely to call this yourself as it will be called 
	 * by the Database::updateDocment() method depending on whether or not it is using PUT or POST.
	 * @return
	 */
	public String getQueryString() {
		String qs = "";
		
		for (NameValuePair p : params) {
			qs += p.getName() + "=" + p.getValue() + "&"; 
		}
		
		// Strip the trailing ampersand
		if (qs.endsWith("&")) {
			qs = qs.substring(0, (qs.length() - 1));
		}
		
		return qs;
	}
	
	/**
	 * Return the parameters in a format compatible with the 'x-www-form-urlencoded' content type. This 
	 * is called by the Database::updateDocument() method to perform a POST update
	 * @return
	 */
	public String getURLFormEncodedString() {
		return URLEncodedUtils.format(params, "UTF-8");
	}
}