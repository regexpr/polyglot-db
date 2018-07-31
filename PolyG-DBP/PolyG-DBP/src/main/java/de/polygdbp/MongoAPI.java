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
import static de.polygdbp.Main.LOG;
import org.bson.Document;

/**
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
public class MongoAPI {
  private final MongoClient mongoClient;
  private final MongoDatabase mongoDb;
  private MongoCollection<Document> currentMongoCollection;

  /**
   *
   */
  public MongoAPI(MongoClientURI uri) {
    
    this.mongoClient = new MongoClient(uri);
    this.mongoDb = mongoClient.getDatabase("polyg-benchmark");
    this.currentMongoCollection = null;
    // Checking if Connection is refused by provoking a potential com.mongodb.MongoSocketOpenException
    mongoClient.listDatabaseNames();

  }

  /**
   *
   * @return
   */
  public MongoCollection<Document> getCurrentMongoCollection() {
    return currentMongoCollection;
  }

  /**
   *
   * @param currentMongoCollection
   */
  public void setCurrentMongoCollection(String currentMongoCollection) {
    this.currentMongoCollection = mongoDb.getCollection(currentMongoCollection);
  }
  
  
  
  
  

}
