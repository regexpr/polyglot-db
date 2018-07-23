/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.polygdbp;


import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.util.Arrays;

/**
 *
 * @author tim, HyeonUng
 */
public class QueryHandler {
    private DatabaseService dbs;
    
    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };
	
    //giving the QueryHandler the DatabaseService, where it can execute its queries
	public QueryHandler(DatabaseService databaseService) {
    	this.dbs = databaseService;
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
	

	//give me all business names and ids a <user> rated with minumum of <stars>
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
	}

	
	

}
