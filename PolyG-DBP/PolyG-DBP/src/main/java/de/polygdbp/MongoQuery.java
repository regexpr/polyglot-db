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

import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;

import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 * Execute queries for the MongoDB and get the results back.
 */
public class MongoQuery {

  MongoAPI mongoApi;
  
  private ArrayList<String> results = new ArrayList<String>();
  
  /**
   * Constructor needs a valid mongoApi.
   * @param mongoApi
   */
  public MongoQuery(MongoAPI mongoApi) {
    this.mongoApi = mongoApi;
  }
  
  // Override apply()to add all query results to the output class variable result".
  Block<Document> printBlock = new Block<Document>() {
    @Override
    public void apply(final Document document) {
      results.add(document.toJson());
    }
  };
  
  /**
   * Gets a single Object from the MongoDB with corresponding key-value.
   * @param collectionName
   * @param key
   * @param value
   * @return Document
   */
  public Document simpleMongoQueryFindOne(String collectionName, String key, String value) {
    mongoApi.setCurrentMongoCollection(collectionName);
    MongoCollection<Document> collection = mongoApi.getCurrentMongoCollection();
    Document result = collection.find(eq(key, value)).first();
    System.out.println(result);
    return result;
  };
  
  /**
   * Gets all Objects from the MongoDB with corresponding key-value.
   * @param collectionName
   * @param key
   * @param value
   * @return List of Documents
   */
  public FindIterable<Document> simpleMongoQueryFindAll(String collectionName, String key, String value) {
    mongoApi.setCurrentMongoCollection(collectionName);
    MongoCollection<Document> collection = mongoApi.getCurrentMongoCollection();
	//		collection.find(eq(key, value)).forEach(printBlock);
	FindIterable<Document> finalResults = collection.find(eq(key, value));
	return finalResults;
  };
  
  /**
   * Standard query method.
   * @param phrase The Query.
   */
  public void customMongoAggregation(String phrase) {
    MongoQueryBuilder mqb = new MongoQueryBuilder(phrase);
    String collectionName = mqb.getFirstPart()[1];
	
    mongoApi.setCurrentMongoCollection(collectionName);
    MongoCollection<Document> collection = mongoApi.getCurrentMongoCollection();
    
    List<Document> query = new ArrayList<Document>();
    
    mqb.buildMongoQuery();
    query = mqb.getMongoQuery();
    
    AggregateIterable<Document> result = collection.aggregate(query);
    for (Document dbObject : result)
    {
        results.add(dbObject.toJson());
    }


  }
  

  
  /**
   * Getter method of the query results.
   * @return
   */
  public ArrayList<String> getResults(){
    return results;
  }
}

