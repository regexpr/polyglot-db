/*
 * Copyright 2018 Tim Niehoff, Hyeon Ung Kim.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.polygdbp;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
public class MongoAPITest {
  
  public MongoAPITest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }

  /**
   * Test of getCurrentMongoCollection method, of class MongoAPI.
   */
  @Ignore
  @Test
  public void testGetCurrentMongoCollection() {
    System.out.println("getCurrentMongoCollection");
    //MongoAPI instance = new MongoAPI();
    MongoCollection<Document> expResult = null;
   // MongoCollection<Document> result = instance.getCurrentMongoCollection();
    //assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setCurrentMongoCollection method, of class MongoAPI.
   */
  @Ignore
  @Test
  public void testSetCurrentMongoCollection() {
    System.out.println("setCurrentMongoCollection");
    String currentMongoCollection = "";
    //MongoAPI instance = new MongoAPI();
    //instance.setCurrentMongoCollection(currentMongoCollection);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
