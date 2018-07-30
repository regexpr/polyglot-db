/*
 q* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.polygdbp;


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
import com.mongodb.client.model.Filters;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim, HyeonUng
 */
public class QueryHandler {
    private static DatabaseService dbs;
    private static Driver driver;
    
    private Type type;
    
    
    //giving the QueryHandler the DatabaseService, where it can execute its queries
	public QueryHandler(DatabaseService databaseService) {
    	QueryHandler.dbs = databaseService;
    	QueryHandler.driver = dbs.getNeo4jDriver();
    };
    
    
    //
    //The Mongo DB queries  
    //

    
    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };
	
	
	//gets a single Object from the MongoDB with corresponding key-value
	public Document simpleMongoQueryFindOne(String collectionName, String key, String value) {
		MongoCollection<Document> collection = dbs.chooseMongoCollection(collectionName);
		Document result = collection.find(eq(key, value)).first();
		System.out.println(result);
		return result;
	};
	

	//gets all Objects from the MongoDB with corresponding key-value
	public FindIterable<Document> simpleMongoQueryFindAll(String collectionName, String key, String value) {
		MongoCollection<Document> collection = dbs.chooseMongoCollection(collectionName);
//		collection.find(eq(key, value)).forEach(printBlock);
		FindIterable<Document> results = collection.find(eq(key, value));
		return results;
	};
	

	public void customMongoAggregation(String phrase, String collectionName) {
		MongoCollection<Document> collection = dbs.chooseMongoCollection(collectionName);
		MongoQueryBuilder mqb = new MongoQueryBuilder(phrase);
		
		List<Document> query = new ArrayList<Document>();
		List<Document> query2 = new ArrayList<Document>();
		
		mqb.buildMongoQuery();
		query = mqb.getMongoQuery();
		
		collection.aggregate(query).forEach(printBlock);

	}

	
	
	//
	//Neo4j Queries
	//
	

	public List<Object> customNeo4jQuery(String query)	{
	    try ( Session session = driver.session() ) {
	        return session.readTransaction( new TransactionWork<List<Object>>() {
	            @Override
	            public List<Object> execute( Transaction tx ) {
	        	    List<Object> output = new ArrayList<>();
	        	    StatementResult result = tx.run( query );
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