/**
 * Copyright (c) 2011 Cummings Engineering Consultants, Inc. All Rights Reserved
 */
package com.fourspaces.couchdb.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fourspaces.couchdb.ReplicationTask;

/**
 * @author anthony.payne
 *
 */
public class ReplicationTaskTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.fourspaces.couchdb.ReplicationTask#loadDetailsFromTask()}.
	 */
	@Test
	public void testLoadDetailsFromTask() {
		final String task = "e9db21: shard_1_entity_db -> http://192.168.4.20:5984/shard_1_entity_db/";
		final String status = "MR Processed source update #594";
		final String pid = "<0.201.0>";
		
		ReplicationTask repTask = new ReplicationTask(task, status, pid);
		
		assertTrue(repTask.loadDetailsFromTask());
		
		assertEquals(status, repTask.getStatus());
		assertEquals(pid, repTask.getPid());
		assertNotNull(repTask.getSource());
		assertFalse(repTask.getSource().isRemote());
		assertEquals("shard_1_entity_db", repTask.getSource().getReplicatedEntity());
		
		assertNotNull(repTask.getDestination());
		assertTrue(repTask.getDestination().isRemote());
		assertEquals("192.168.4.20", repTask.getDestination().getServer());
		assertEquals("5984", Integer.toString(repTask.getDestination().getPort()));
		assertEquals("shard_1_entity_db", repTask.getDestination().getReplicatedEntity());
		
	}

}
