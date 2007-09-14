package com.fourspaces.couchdb.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.View;

public class DocumentTest {
	Session sess = TestSession.getTestSession();
	Database foo;
	
	@Before public void createTestDB() {
		foo=sess.createDatabase("foo");
		foo.saveDocument(new Document(),"foo");
		foo.saveDocument(new Document());
		foo.saveDocument(new Document());
		foo.saveDocument(new Document());
		foo.saveDocument(new Document());

	}
	
	
	@Test
	public void update() {
		JSONObject obj = new JSONObject();
		obj.put("foo","bar");
		obj.accumulate("array", "ar1");
		obj.accumulate("array", "ar2");
		obj.accumulate("array", "ar3");
		Document doc = new Document();
		doc.load(obj);
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
	
	@Test public void get() {		
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
		View one = new View(foo,"_all_docs");
		one.setCount(1);
		assertEquals(foo.view(one).getResults().size(),1 );
	}

	
	@After
	public void deleteAll() {
		sess.deleteDatabase("foo");
	}

}
