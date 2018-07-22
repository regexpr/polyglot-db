/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.polygdbp;

import java.net.UnknownHostException;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


/**
 *
 * @author tim, Hyeon Ung
 */
public class DatabaseService {
	private static MongoClient mongoClient;
    private static Driver driver;
    
    private DB mongoDatabase;
    private DBCollection mongoCollection;  
    
	public DatabaseService() throws UnknownHostException {
		this.mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		this.mongoDatabase = mongoClient.getDB("yelp");
	}
	
	public void changeMongoDatabase(String dbName) {
		this.mongoDatabase = mongoClient.getDB(dbName);
	}
	
	
	public DBCollection chooseMongoCollection(String collectionName) {
		this.mongoCollection = mongoDatabase.getCollection(collectionName);
		return mongoCollection;
	}
	
	public DB getMongoDatabase() {
		return mongoDatabase;
	}
	
	
//    //this method connects to the standard localhost mongodb, and connects to the database "databaseName"
//    public void connectToMongo(String host, int port, String databaseName) throws UnknownHostException {
//    	MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://"+host+":"+port));
//    	this.mongoClient = new MongoClient(host, port);
//    	this.mongoDb = mongoClient.getDatabase(databaseName);
//    }
//    
//    public void changeMongoDatabase(String databaseName) {
//    	this.mongoDb = mongoClient.getDatabase(databaseName);
//    }
//    
//    public MongoCollection getMongoCollection(String collectionName) {
//    	return mongoDb.getCollection(collectionName);
//    }
//    
    /**
    *
    * the following methods are for the Neo4j Server
    */
    
    public void connectToNeo4j(String uri, String user, String password) {
    	this.driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }
    
    public void closeNeo4j() {
    	driver.close();
    }
    
    
    
}
