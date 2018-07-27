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

import org.bson.Document;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.types.Type;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasse zum Erstellen von Mongo/Neo4j Queries. Diese werden dann an DatabaseService
 * weiter geleitet.
 */
public class QueryHandler {
  private static DatabaseService dbs;
  private static Driver driver;
  
  private Type type;
  
  
  //giving the QueryHandler the DatabaseService, where it can execute its queries

  /**
   *
   * @param databaseService
   */
  public QueryHandler(DatabaseService databaseService) {
    QueryHandler.dbs = databaseService;
    QueryHandler.driver = dbs.getNeo4jDriver();
  };
  
  
  //
  //The Mongo DB queries
  //

  /**
   *
   * @throws ParseException
   */
  
  public void convertMongoQuery() throws ParseException {
    JSONParser jsonParser = new JSONParser();
    Object jsonObject = null;
    
    String phrase ="db.review.aggregate([{$match : {$and: [{\"user_id\":\"nOTl4aPC4tKHK35T3bNauQ\"},{\"stars\": {$gt: 4}}]}}, {$lookup: {from:\"business\", localField: \"business_id\", foreignField: \"business_id\", as: \"business\"}}, {$addFields: {\"business\":\"$business\"}}, {$project:{\"business.name\":1, \"business.business_id\":1}}])";
    String delims1 = "[(\\[{}: ,\\])]+";
    String delims2 = ".";
    String delims3 = "\\(";
    
    String[] tokens2 = phrase.split(delims3);
    String[] tokens = phrase.split(delims1);
    String[] firstPart = phrase.split(delims2);
    
    String inner = (String) tokens2[1].subSequence(1, tokens2[1].length()-2);
    
    jsonObject = jsonParser.parse(inner);
    
//	    System.out.println(jsonObject["$match"]);
//
//        for(int i=1; i<tokens.length; i++) {
//        	switch(tokens[i]){
//        		case "$match":
//
//        			break;
//        	}
//        }
  }
  
//    public void checkTokens(String token) {
//    	Filters filter;
//    	switch(token) {
//    		case "$and": and()
//    		case "$gt":
//    		case "$gte":
//    		case "$lt":
//    		case "$lte":
//    		case "$ne":
//    		case "$in":
//    		case "$nin":
//    	}
//
//    }
  
  
  
  Block<Document> printBlock = new Block<Document>() {
    @Override
    public void apply(final Document document) {
      System.out.println(document.toJson());
    }
  };
  
  
  //gets a single Object from the MongoDB with corresponding key-value.

  /**
   *
   * @param collectionName
   * @param key
   * @param value
   * @return
   */
  public Document simpleMongoQueryFindOne(String collectionName, String key, String value) {
    MongoCollection<Document> collection = dbs.chooseMongoCollection(collectionName);
    Document result = collection.find(eq(key, value)).first();
    System.out.println(result);
    return result;
  };
  
  
  //gets all Objects from the MongoDB with corresponding key-value

  /**
   *
   * @param collectionName
   * @param key
   * @param value
   * @return
   */
  public FindIterable<Document> simpleMongoQueryFindAll(String collectionName, String key, String value) {
    MongoCollection<Document> collection = dbs.chooseMongoCollection(collectionName);
//		collection.find(eq(key, value)).forEach(printBlock);
FindIterable<Document> results = collection.find(eq(key, value));
return results;
  };
  
  
  //give me all business names and ids a <user> rated with minimum of <stars>

  /**
   *
   * @param uid
   * @param stars
   */
  @SuppressWarnings("unchecked")
  public void mongoAggregation2(String uid, int stars) {
    MongoCollection<Document> collection = dbs.chooseMongoCollection("review");
    collection.aggregate(Arrays.asList(
            Aggregates.match(and(
                    eq("user_id", uid),
                    gt("stars",stars)
            )),
            Aggregates.lookup("business", "business_id", "business_id", "business"),
            Aggregates.addFields(new Field("business", "$business")),
            Aggregates.project(fields(include("business.name", "business.business_id")))
    )).forEach(printBlock);
  };
  
  //
  //Neo4j Queries
  //

  /**
   *
   * @param query
   * @return
   */
  public List<Object> customNeo4jQuery(String query)	{
    try ( Session session = driver.session() ) {
      return session.readTransaction( new TransactionWork<List<Object>>() {
        @Override
        public List<Object> execute( Transaction tx ) {
          List<Object> output = new ArrayList<>();
          StatementResult result = tx.run( query );
          String string;
          if(result.peek().get(0).type().name() == "NODE") {
            while ( result.hasNext() ) {
              Record tempResult = result.next();
              for(int i=0; i<tempResult.size(); i++) {
                output.add(tempResult.get(0).asMap());
              }
            }
          }
          else {
            while ( result.hasNext() ) {
              Record tempResult = result.next();
              for(int i=0; i<tempResult.size(); i++) {
                output.add(tempResult.asMap());
              }
            }
          }
          return output;
        }
      } );
    }
  }
}
