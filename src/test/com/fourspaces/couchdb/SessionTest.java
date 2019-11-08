package com.fourspaces.couchdb;

import com.fourspaces.couchdb.test.TestHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.SocketTimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SessionTest
{

	@Before public void setup() {
	}
	
	@After public void tearDown() {
	}
	

	@Test
   public void testTimeout() {
      Session session = new Session("localhost", 5984);
      String reason = "test timeout";
      SocketTimeoutException exception = new SocketTimeoutException(reason);
      TestHttpClient httpClient = new TestHttpClient(exception);
      session.setHttpClient(httpClient);

      //url doesn't matter since we'll mock a timeout exception
      CouchResponse response = session.get("http://nowhere.example.com:5984/nosuchdb");

      assertFalse(response.isOk());
      assertEquals(response.getErrorId(), "exception");
      assertEquals(response.getErrorReason(), reason);
	}



}
