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

import java.net.UnknownHostException;
import org.bson.Document;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Klasse zum Verwalten der MongoDB/Neo4J Instanzen.
 * Enthält Methoden zum Starten, Beenden und Verwenden der Datenbank API.
 *
 */
public class DatabaseService {
  private static MongoClient mongoClient;
  private static Driver driver;
  
  private MongoDatabase mongoDatabase;
  private MongoCollection<Document> mongoCollection;
  
  //Constructor for the DatabaseService
  //TODO: connect to Neo4j also

  /**
   *
   * @param host
   * @param port
   * @param dbName
   * @param uri
   * @param user
   * @param password
   * @throws UnknownHostException
   */
  public DatabaseService(String host, int port, String dbName, String uri, String user, String password) throws UnknownHostException {
    DatabaseService.mongoClient = new MongoClient(new MongoClientURI("mongodb://"+host+":"+port));
    this.mongoDatabase = mongoClient.getDatabase(dbName);
    this.connectToNeo4j(uri, user, password);
  }
  
  //connects to Database with name: dbName

  /**
   *
   * @param dbName
   */
  public void changeMongoDatabase(String dbName) {
    this.mongoDatabase = mongoClient.getDatabase(dbName);
  }
  
  /**
   *
   * @param collectionName
   * @return
   */
  public MongoCollection<Document> chooseMongoCollection(String collectionName) {
    this.mongoCollection = mongoDatabase.getCollection(collectionName);
    return mongoCollection;
  }
  
  /**
   *
   * @return
   */
  public MongoDatabase getMongoDatabase() {
    return mongoDatabase;
  }
  
  
  /**
   *
   * The following methods are for the Neo4j Server.
   * @param uri
   * @param user
   * @param password
   */
  
  //connects to Neo4j with credentials
  public void connectToNeo4j(String uri, String user, String password) {
    DatabaseService.driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
  }
  
  /**
   *
   * @return
   */
  public Driver getNeo4jDriver() {
    return DatabaseService.driver;
  }
  
  /**
   *
   */
  public void closeNeo4j() {
    driver.close();
  }
  
}
