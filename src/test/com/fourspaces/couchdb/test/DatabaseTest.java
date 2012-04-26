package com.fourspaces.couchdb.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fourspaces.couchdb.CouchDbRuntimeStatisticGroup;
import com.fourspaces.couchdb.Session;

public class DatabaseTest {
	Session sess = TestSession.getTestSession();
	
	@Before public void createTestDB() {
		sess.createDatabase("foo");
	}
	
	@After public void removeTestDB() {
		sess.deleteDatabase("foo");
	}
	
	@Test public void list() {
		assertTrue (sess.getDatabaseNames().size()>0);
	}
	
	@Test public void add() {
		int old = sess.getDatabaseNames().size();
		sess.createDatabase("foo2");
		assertEquals(sess.getDatabaseNames().size(),old+1);
		sess.deleteDatabase("foo2");
	}
	@Test public void delete() {
		sess.createDatabase("foobar");
		assertNotNull(sess.getDatabase("foobar"));
		sess.deleteDatabase("foobar");
		assertNull(sess.getDatabase("foobar"));
	}

	@Test public void dup() {
		int old = sess.getDatabaseNames().size();
		sess.createDatabase("foo2");
		assertEquals(sess.getDatabaseNames().size(),old+1);
		sess.createDatabase("foo2");
		assertEquals(sess.getDatabaseNames().size(),old+1);
		sess.deleteDatabase("foo2");
	}
	
	@Test public void compact() throws IOException {
		sess.createDatabase("foo3");
		try {
			String res = sess.getDatabase("foo3").compact();
			assertTrue(res.contains("{\"ok\":true}"));
		} finally {
			sess.deleteDatabase("foo3");
		}
	}
	
	@Test public void statistics() throws IOException {
		Map<String, Map<String, CouchDbRuntimeStatisticGroup>> runtimeStatistics = sess.getRuntimeStatistics();
		assertNotNull(runtimeStatistics);
		assertTrue(runtimeStatistics.size() > 0);
		assertTrue(runtimeStatistics.containsKey("couchdb"));
		assertTrue(runtimeStatistics.containsKey("httpd"));
		assertTrue(runtimeStatistics.containsKey("httpd_request_methods"));
		assertTrue(runtimeStatistics.containsKey("httpd_status_codes"));
		for(Map m : runtimeStatistics.values()) {
			assertNotNull(m);
			assertTrue(m.size() > 0);
		}
	}
}
