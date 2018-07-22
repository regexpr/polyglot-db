/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.polygdbp;

import org.w3c.dom.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 *
 * @author tim, Hyeon Ung
 */
public class QueryHandler {
    private DatabaseService databaseService;
	
    //giving the QueryHandler the DatabaseService, where it can execute its queries
	public QueryHandler(DatabaseService dbs) {
    	this.databaseService = dbs;
    }
	
	//gets a single Object from the MongoDB with corresponding key-value
	public DBObject simpleMongoQueryFindOne(String collectionName, String key, String value) {
		DBCollection collection = databaseService.chooseMongoCollection(collectionName);
		DBObject result = collection.findOne(new BasicDBObject("name", "Starbucks"));
		System.out.println(result);
		return result;
	}
	
	
	//gets a single Object from the MongoDB with corresponding key-value
	public DBCursor simpleMongoQueryFindAll(String collectionName, String key, String value) {
		DBCollection collection = databaseService.chooseMongoCollection(collectionName);
		DBCursor results = collection.find(new BasicDBObject("name", "Starbucks"));
		System.out.println(results.size());
		while (results.hasNext()) {
			System.out.println(results.next());
		}
		return results;
	}

}
