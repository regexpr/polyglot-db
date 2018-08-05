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

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

/**
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
public class MongoQueryBuilderTest {
  
  private String[] query = new String[] {
      "db.collection.function({key:value})",
      "db.collection.function([{$operator1:{key1,value1,key2,value2},[{$operator2:{$operator3:{$operator4:{key3,value3}}},{key4,value4},{$operator5:[{key5,value5},{key6,value6}]}}])",
      "db.business.aggregate({$group:{_id:null,\"average_stars\":{$avg:\"$stars\"}}})",
      "db.review.aggregate([{$match:{$and:[{\"user_id\":\"nOTl4aPC4tKHK35T3bNauQ\"},{\"stars\":{$gt:4}}]}},{$lookup:{from:\"business\",localField:\"business_id\",foreignField:\"business_id\",as:\"business\"}},{$addFields:{\"business\":\"$business\"}},{$project:{\"business.name\":1,\"business.business_id\":1}}])"
  };
  
  public MongoQueryBuilderTest() {
  }

  /**
   * Test of buildMongoQuery method, of class MongoQueryBuilder.
   */
  @Test
  public void testBuildMongoQuery() {
    ArrayList<ArrayList<Document>> assertAppend = new ArrayList<ArrayList<Document>>();
    assertAppend.add(new ArrayList<Document>() {{
      add(new Document("key", "value"));
    }});
    assertAppend.add(new ArrayList<Document>() {{ 
      add(new Document("$operator1", new Document("key1", "value1").append("key2", "value2")));
      add(new Document("$operator2", new Document("$operator3", new Document("$operator4", new Document("key3","value3"))).append("key4", "value4").append("$operator5", Arrays.asList(new Document("key5","value5").append("key6", "value6")))));
    }});

    
    for(int i = 0; i < 1; i++) {
      MongoQueryBuilder mqb = new MongoQueryBuilder(query[i]);
      mqb.buildMongoQuery();
      assertEquals(assertAppend.get(i), mqb.getMongoQuery());
    }
  }

  /**
   * Test of getMongoQuery method, of class MongoQueryBuilder.
   */
  @Test
  public void testGetMongoQuery() {
  }

  /**
   * Test of cleanToken method, of class MongoQueryBuilder.
   */
  @Test
  public void testCleanToken() {
    String[][] canItClean = new String[][] {
        {"{key", "value}"},
        {"[{$operator1", "{key1", "value1", "key2", "value2}","[{$operator2", "{$operator3", "{$operator4", "{key3", "value3}}}", "{key4", "value4}", "{$operator5", "[{key5", "value5}", "{key6", "value6}]}}]"},
        { "{$group","{_id", "null","\"average_stars\"", "{$avg" ,"\"$stars\"}}}"},
        {"[{$match", "{$and", "[{\"user_id\"", "\"nOTl4aPC4tKHK35T3bNauQ\"}", "{\"stars\"", "{$gt", "4}}]}}", "{$lookup", "{from", "\"business\"", "localField", "\"business_id\"", "foreignField", "\"business_id\"", "as", "\"business\"}}", "{$addFields", "{\"business\"", "\"$business\"}}", "{$project", "{\"business.name\"", "1", "\"business.business_id\"", "1}}]"}
      };
    String[][] yesItCanClean = new String[][] {
      {"key", "value"},
      {"$operator1", "key1", "value1", "key2", "value2","$operator2", "$operator3", "$operator4", "key3", "value3", "key4", "value4", "$operator5", "key5", "value5", "key6", "value6"},
      { "$group","_id", "null","average_stars", "$avg" ,"$stars"},
      {"$match", "$and", "user_id", "nOTl4aPC4tKHK35T3bNauQ", "stars", "$gt", "4", "$lookup", "from", "business", "localField", "business_id", "foreignField", "business_id", "as", "business", "$addFields", "business", "$business", "$project", "business.name", "1", "business.business_id", "1"}
    };
    
    for(int i = 0; i< query.length; i++) {
      MongoQueryBuilder mqb = new MongoQueryBuilder(query[i]);
      
      for(int j = 0; j<canItClean[i].length; j++) {
        assertEquals(yesItCanClean[i][j],mqb.cleanToken(canItClean[i][j]));
      }
    }
  }

  /**
   * Test of countBrackets method, of class MongoQueryBuilder.
   */
  @Test
  public void testCountBrackets() {
  }

  /**
   * Test of updateGlobalBracketCounter method, of class MongoQueryBuilder.
   */
  @Test
  public void testUpdateGlobalBracketCounter() {
  }

  /**
   * Test of evalBracketCounter method, of class MongoQueryBuilder.
   */
  @Test
  public void testEvalBracketCounter() {
  }

  

  /**
   * Test of buildDocument method, of class MongoQueryBuilder.
   */
  @Test
  public void testBuildDocument() {

  }

  /**
   * Test of documentsAsList method, of class MongoQueryBuilder.
   */
  @Test
  public void testDocumentsAsList() {
  }

  /**
   * Test of appendedDocument method, of class MongoQueryBuilder.
   */
  @Test
  public void testAppendedDocument() {
    
  }

  /**
   * Test of cutQuery method, of class MongoQueryBuilder.
   */
  @Test
  public void testCutQuery() {
    String[] assertInner = new String[] {
        "{key:value})",
        "[{$operator1:{key1,value1,key2,value2},[{$operator2:{$operator3:{$operator4:{key3,value3}}},{key4,value4},{$operator5:[{key5,value5},{key6,value6}]}}])",
        "{$group:{_id:null,\"average_stars\":{$avg:\"$stars\"}}})",
        "[{$match:{$and:[{\"user_id\":\"nOTl4aPC4tKHK35T3bNauQ\"},{\"stars\":{$gt:4}}]}},{$lookup:{from:\"business\",localField:\"business_id\",foreignField:\"business_id\",as:\"business\"}},{$addFields:{\"business\":\"$business\"}},{$project:{\"business.name\":1,\"business.business_id\":1}}])"
        
    };
    String[][] assertFirstPart = new String[][] {
      {"db", "collection", "function"},
      {"db", "collection", "function"},
      {"db", "business", "aggregate"},
      {"db", "review", "aggregate"}
      
    };
    
    for(int i = 0; i< query.length; i++) {
      MongoQueryBuilder mqb = new MongoQueryBuilder(query[i]);
      mqb.cutQuery();
      mqb.setTokens();
      
      String innerPart = mqb.getInner();
      String[] firstPart = mqb.getFirstPart();
      
      assertEquals(innerPart, assertInner[i]);
      
      for(int j = 0; j<assertFirstPart[i].length; j++) {
        assertEquals(firstPart[j], assertFirstPart[i][j]);
      }
    }

  }

  /**
   * Test of setTokens method, of class MongoQueryBuilder.
   */
  @Test
  public void testSetTokens() {
    
    String[][] assertTokens = new String[][] {
      {"{key", "value}"},
      {"[{$operator1", "{key1", "value1", "key2", "value2}","[{$operator2", "{$operator3", "{$operator4", "{key3", "value3}}}", "{key4", "value4}", "{$operator5", "[{key5", "value5}", "{key6", "value6}]}}]"},
      { "{$group","{_id", "null","\"average_stars\"", "{$avg" ,"\"$stars\"}}}"},
      {"[{$match", "{$and", "[{\"user_id\"", "\"nOTl4aPC4tKHK35T3bNauQ\"}", "{\"stars\"", "{$gt", "4}}]}}", "{$lookup", "{from", "\"business\"", "localField", "\"business_id\"", "foreignField", "\"business_id\"", "as", "\"business\"}}", "{$addFields", "{\"business\"", "\"$business\"}}", "{$project", "{\"business.name\"", "1", "\"business.business_id\"", "1}}]"}
    };
    
    for(int i = 0; i< query.length; i++) {
      MongoQueryBuilder mqb = new MongoQueryBuilder(query[i]);
      mqb.cutQuery();
      mqb.setTokens();
      
      String[] tokens = mqb.getTokens();
      
      for(int j = 0; j < tokens.length; j++) {
        assertEquals(tokens[j], assertTokens[i][j]);
        
      }
      
    }
    
  }

  /**
   * Test of getFirstPart method, of class MongoQueryBuilder.
   */
  @Test
  public void testGetFirstPart() {
  }

  /**
   * Test of isInteger method, of class MongoQueryBuilder.
   */
  @Test
  public void testIsInteger_String() {
    String[] doesItKnow = new String[]{
        "1566648","121215a548", "-485", "454+958"  
      };
    boolean[] itKnows = new boolean[] {
        true, false, true, false
    };
    
    MongoQueryBuilder mqb = new MongoQueryBuilder(query[1]);
    
    for(int i=0; i<doesItKnow.length; i++) {
      assertEquals(itKnows[i], mqb.isInteger(doesItKnow[i]));
    }
    
  }

  /**
   * Test of isInteger method, of class MongoQueryBuilder.
   */
  @Test
  public void testIsInteger_String_int() {

    
    
  }

  /**
   * Test of getInner method, of class MongoQueryBuilder.
   */
  @Test
  public void testGetInner() {
  }

  /**
   * Test of getTokens method, of class MongoQueryBuilder.
   */
  @Test
  public void testGetTokens() {
  }
  
}
