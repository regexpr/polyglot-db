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

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Establishes a connection with a running MongoDB instance. It contains several API methods.
 */
public class MongoAPI {
  private final MongoClient mongoClient;
  private final MongoDatabase mongoDb;
  private MongoCollection<Document> currentMongoCollection;
  
  /**
   * The constructor tries to connect to the MongoDB.
   * @param uri. Address of the running MongoDB. Valid example: "mongodb://localhost:27017".
   * @param dbName. Name of the current MongoDB database name.
   */
  public MongoAPI(String uri, String dbName) {
    this.mongoClient = new MongoClient(new MongoClientURI(uri));
    this.mongoDb = mongoClient.getDatabase(dbName);
    this.currentMongoCollection = null;
    // Checking if Connection is refused by provoking a potential com.mongodb.MongoSocketOpenException
    mongoClient.listDatabaseNames();

  }

  /**
   * Getter method of the current MongoClient.
   * @return MongoClient.
   */
  public MongoClient getMongoClient() {
    return mongoClient;
  }
  
  /**
   * Getter method of one considered MongoCollection.
   * @return MongoCollection of Documents.
   */
  public MongoCollection<Document> getCurrentMongoCollection() {
    return currentMongoCollection;
  }
  
  /**
   * Setter method of one considered MongoCollection.
   * @param currentMongoCollection. Name of the relevant Collection.
   */
  public void setCurrentMongoCollection(String currentMongoCollection) {
    this.currentMongoCollection = mongoDb.getCollection(currentMongoCollection);
  }
}
