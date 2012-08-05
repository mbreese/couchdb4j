/**
 * Copyright (c) 2012 Raymond Wilson (http://www.bytefoundry.co.uk) 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
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

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.Update;
import com.fourspaces.couchdb.util.JSONUtils;

public class UpdateTest {
  Log log = LogFactory.getLog(getClass());
  
  Session sess = TestSession.getTestSession();
  Database foo;
  
  @Before 
  public void createTestDB() 
      throws Exception {
    foo = sess.createDatabase("update_test");
    
    // Create test design document
    Document design = new Document();
    
    design.put("_id", "_design/junit");
    
    JSONObject funcs = new JSONObject();   
    funcs.put("put", JSONUtils.stringSerializedFunction("function(doc,req){doc.Field1=req.query.field1; return [doc, '{\\\"ok\\\":\\\"true\\\"}'];}"));
    funcs.put("post", JSONUtils.stringSerializedFunction("function(doc,req){doc.Field2=req.form.field2; return [doc, '{\\\"ok\\\":\\\"true\\\"}'];}"));
    
    design.accumulate("updates", funcs);
    
    // System.err.println("UDFUNCS: " + design.toString());
    
    foo.saveDocument(design);
    
    // Create a document containing test data to process
    Document testDoc = new Document();
    
    testDoc.put("_id", "test_data");
    testDoc.put("Field1", "Default");
    testDoc.put("Field2", "Default");
    
    foo.saveDocument(testDoc);
  }
  
  @Test 
  public void testPUTUpdate() 
      throws Exception {
    Update putUpdate = new Update("junit/put", "test_data");
    putUpdate.addParameter("field1", "UpdatedByPUT");
        
    boolean result = foo.updateDocument(putUpdate);
    assertTrue(result);
    
    // Retrieve the field and make sure the value is correct
    Document testDoc = foo.getDocument("test_data");
    assertNotNull(testDoc);
    assertEquals("UpdatedByPUT", testDoc.getString("Field1"));
  }
  
  @Test 
  public void testPOSTUpdate() 
      throws Exception {    
    Update postUpdate = new Update("junit/post", "test_data");
    postUpdate.addParameter("field2", "UpdatedByPOST");
    postUpdate.setMethodPOST(true);
        
    boolean result = foo.updateDocument(postUpdate);
    assertTrue(result);
    
    // Retrieve the field and make sure the value is correct
    Document testDoc = foo.getDocument("test_data");
    assertNotNull(testDoc);
    assertEquals("UpdatedByPOST", testDoc.getString("Field2"));
  }
  
  @Test
  public void testAddUpdateHandler() 
      throws Exception {
    // Retrieve the test design document
    Document designDoc = foo.getDocument("_design/junit");
    assertNotNull(designDoc);
    
    // Add the new update handler
    designDoc.addUpdateHandler("test", "function(doc,req){doc.Field1='HANDLERTEST'; return [doc, '{\\\"ok\\\":\\\"true\\\"}'];}");
    foo.saveDocument(designDoc);
    
    // Request a new copy of the design document (NOTE: not calling refresh() as it doesn't overwrite
    // unsaved data
    Document designDocNew = foo.getDocument("_design/junit");
    assertNotNull(designDocNew);
    
    // Ensure the three update handlers exist
    JSONObject handlers = designDocNew.getJSONObject("updates");
    assertNotNull(handlers);    
    assertTrue(handlers.has("put"));
    assertTrue(handlers.has("post"));
    assertTrue(handlers.has("test"));
  }
  
  @After
  public void deleteAll() {
    sess.deleteDatabase("update_test");
  }
}
