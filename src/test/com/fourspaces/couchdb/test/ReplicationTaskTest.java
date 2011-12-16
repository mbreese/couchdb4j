/**
 * Copyright (c) 2011 Cummings Engineering Consultants, Inc. 
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
		final String task = "e9db21: testDb -> http://10.11.12.13:5984/testDb/";
		final String status = "MR Processed source update #594";
		final String pid = "<0.201.0>";
		
		ReplicationTask repTask = new ReplicationTask(task, status, pid);
		
		assertTrue(repTask.loadDetailsFromTask());
		
		assertEquals(status, repTask.getStatus());
		assertEquals(pid, repTask.getPid());
		assertNotNull(repTask.getSource());
		assertFalse(repTask.getSource().isRemote());
		assertEquals("testDb", repTask.getSource().getReplicatedEntity());
		
		assertNotNull(repTask.getDestination());
		assertTrue(repTask.getDestination().isRemote());
		assertEquals("10.11.12.13", repTask.getDestination().getServer());
		assertEquals("5984", Integer.toString(repTask.getDestination().getPort()));
		assertEquals("testDb", repTask.getDestination().getReplicatedEntity());
		
	}

}
