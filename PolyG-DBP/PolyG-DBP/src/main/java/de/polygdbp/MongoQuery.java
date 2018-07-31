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
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
public class MongoQuery {

  MongoAPI mongoApi;
  
  /**
   *
   * @param mongoApi
   */
  public MongoQuery(MongoAPI mongoApi) {
    this.mongoApi = mongoApi;
  }
  
  
  Block<Document> printBlock = new Block<Document>() {
    @Override
    public void apply(final Document document) {
      System.out.println(document.toJson());
    }
  };
  //gets a single Object from the MongoDB with corresponding key-value
  
  /**
   *
   * @param collectionName
   * @param key
   * @param value
   * @return
   */
  public Document simpleMongoQueryFindOne(String collectionName, String key, String value) {
    mongoApi.setCurrentMongoCollection(collectionName);
    MongoCollection<Document> collection = mongoApi.getCurrentMongoCollection();
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
    mongoApi.setCurrentMongoCollection(collectionName);
    MongoCollection<Document> collection = mongoApi.getCurrentMongoCollection();
//		collection.find(eq(key, value)).forEach(printBlock);
FindIterable<Document> results = collection.find(eq(key, value));
return results;
  };
  
  /**
   *
   * @param phrase
   * @param collectionName
   */
  public void customMongoAggregation(String phrase, String collectionName) {
    mongoApi.setCurrentMongoCollection(collectionName);
    MongoCollection<Document> collection = mongoApi.getCurrentMongoCollection();
    MongoQueryBuilder mqb = new MongoQueryBuilder(phrase);
    
    List<Document> query = new ArrayList<Document>();
    List<Document> query2 = new ArrayList<Document>();
    
    mqb.buildMongoQuery();
    query = mqb.getMongoQuery();
    
    collection.aggregate(query).forEach(printBlock);
    
  }
}

