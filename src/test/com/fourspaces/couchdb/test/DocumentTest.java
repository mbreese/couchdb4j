package com.fourspaces.couchdb.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.View;
import com.fourspaces.couchdb.ViewResults;

public class DocumentTest {
  Log log = LogFactory.getLog(getClass());
  
	Session sess = TestSession.getTestSession();
	Database foo;
	
	@Before public void createTestDB() throws Exception {
		foo=sess.createDatabase("foo");
		foo.saveDocument(new Document(),"foo");
		foo.saveDocument(new Document());
		foo.saveDocument(new Document());
		foo.saveDocument(new Document());
		foo.saveDocument(new Document());

	}
	
	
	@Test
	public void update() throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("foo","bar");
		obj.accumulate("array", "ar1");
		obj.accumulate("array", "ar2");
		obj.accumulate("array", "ar3");
		Document doc = new Document(obj);
		foo.saveDocument(doc,"foodoc");

		Document foodoc = foo.getDocumentWithRevisions("foodoc");

		System.out.println(foodoc.getRev());
		String oldId = foodoc.getId();
		String oldRev = foodoc.getRev();
		assertEquals(foodoc.getRevisions().length,1);

		foodoc.put("now", new Date());
		foo.saveDocument(foodoc);
		//System.out.println(foodoc.getRev());
		assertEquals(oldId,foodoc.getId());
		assertFalse(oldRev.equals(foodoc.getRev()));

		foodoc = foo.getDocumentWithRevisions("foodoc");
		//System.out.println(Arrays.toString(foodoc.getRevisions()));
		assertEquals(foodoc.getRevisions().length,2);
		
	}
	
	@Test public void get() throws Exception {	
		JSONObject obj = new JSONObject();
		obj.put("foo","bar");
		obj.accumulate("array", "ar1");
		obj.accumulate("array", "ar2");
		obj.accumulate("array", "ar3");
		Document doc = new Document(obj);
		foo.saveDocument(doc,"foodoc");
		
		assertNotNull(doc.getId());
		assertNotNull(doc.getRev());
		
		Document foodoc = foo.getDocument("foodoc");
		assertEquals(foodoc.get("foo"),"bar");
		
		foo.deleteDocument(foodoc);
		foodoc = foo.getDocument("foodoc");
		assertNull(foodoc);
		
	}

	@Test
	public void list1() {
	  
	  ViewResults vr = foo.getAllDocumentsWithCount(1);
		assertEquals(vr.getResults().size(),1 );
	}

	
	@After
	public void deleteAll() {
		sess.deleteDatabase("foo");
	}
	
	@Test public void bulkSave() throws Exception {
	  
	  Document[] docs = new Document[3];
	  docs[0] = new Document();
	  docs[1] = new Document();
	  docs[2] = new Document();
    
	  docs[0].accumulate("foo", "bar1" + System.currentTimeMillis());
	  docs[1].accumulate("foo", "bar2" + System.currentTimeMillis());
	  docs[2].accumulate("foo", "bar3" + System.currentTimeMillis());
	  
 	  foo.bulkSaveDocuments(docs);
 	  
 	  for (Document d : docs) {
 	    boolean deleted = foo.deleteDocument(d);
      assertEquals(deleted, true);
 	  }
 	  
 	 
	  
	  
	}
	  

}
