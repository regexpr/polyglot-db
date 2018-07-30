/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.polygdbp;

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
 *
 * @author tim, HyeonUng
 */
public class DatabaseService {
	private static MongoClient mongoClient;
    private static Driver driver;
    
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;  
    
    //Constructor for the DatabaseService
    //TODO: connect to Neo4j also
	public DatabaseService(String host, int port, String dbName, String uri, String user, String password) throws UnknownHostException {
		DatabaseService.mongoClient = new MongoClient(new MongoClientURI("mongodb://"+host+":"+port));
		this.mongoDatabase = mongoClient.getDatabase(dbName);
		this.connectToNeo4j(uri, user, password);
	}
	
	//connects to Database with name: dbName
	public void changeMongoDatabase(String dbName) {
		this.mongoDatabase = mongoClient.getDatabase(dbName);
	}
	
	
	public MongoCollection<Document> chooseMongoCollection(String collectionName) {
		this.mongoCollection = mongoDatabase.getCollection(collectionName);
		return mongoCollection;
	}
	
	public MongoDatabase getMongoDatabase() {
		return mongoDatabase;
	}
	

    /**
    *
    * the following methods are for the Neo4j Server
    */
    
	//connects to Neo4j with credentials
    public void connectToNeo4j(String uri, String user, String password) {
    	DatabaseService.driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }
    
    public Driver getNeo4jDriver() {
    	return DatabaseService.driver;
    }
    
    public void closeNeo4j() {
    	driver.close();
    }
    
    
    
}