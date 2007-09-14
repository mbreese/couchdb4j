package com.fourspaces.couchdb.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;

public class ViewTest {
	Session sess = TestSession.getTestSession();
	Database foo;
	
	@Before public void createTestDB() {
		sess.createDatabase("foo");
		foo=sess.getDatabase("foo");
		Document d = new Document();
		d.put("foo","bar");
		foo.saveDocument(d);
		System.out.println("known id:"+d.getId());
		System.out.println(foo.getDocument(d.getId()));
		
		foo.saveDocument(new Document());
		foo.saveDocument(new Document());
		foo.saveDocument(new Document());
		foo.saveDocument(new Document());
	}
	
	@After public void removeTestDB() {
		sess.deleteDatabase("foo");
	}

	@Test public void adhoc() {

		int all = foo.getAllDocuments().getResults().size();
		int adhoc = foo.adhoc("function (doc) {return doc}").getResults().size();
		assertEquals(all,adhoc);
		
		
	}
	
	@Test public void adhoc2() {
		int adhoc = foo.adhoc("function (doc){ if (doc.foo=='bar'){ return doc}}").getResults().size();
		assertEquals(1,adhoc);
	}
	
	@Test
	public void addNamed() {
		Document d = new Document();
		d.put("foo","bar");

		System.out.println("Saving d");
		foo.saveDocument(d);
		
		Document d2 = new Document(foo);
		//d2.put("foo","baz");
		d2.addView("all_documents", "function (doc){ return doc; }");
		d2.addView("testview", "function (doc){ if (doc.foo=='bar'){ return doc; }}");
		System.out.println("Saving d2 - "+d2.getId()+" - "+d2.toString());
		foo.saveDocument(d2);
		System.out.println("Saved d2  - "+d2.getId()+" - "+d2.toString());
		Document d2_2 = foo.getDocument(d2.getId());
		System.out.println("Saved d2_2 - "+d2_2.toString());
		
		assertNotNull(d2_2.getView("testview"));
		assertEquals(2,foo.view(d2.getView("testview")).getResults().size());
		
		foo.deleteDocument(d);
		foo.deleteDocument(d2);
		
		
	}
}