/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.polygdbp;

import java.net.UnknownHostException;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;


/**
 *
 * @author tim HyeonUng
 */
public class Main {

	public static void main(String[] argv) throws UnknownHostException{
	    DatabaseService dbs = new DatabaseService("localhost", 27017, "yelp");
	    
	    
	    QueryHandler qh = new QueryHandler(dbs);
	    
//	    Document obj = qh.simpleMongoQueryFindOne("business", "name", "Starbucks");
//	    System.out.println(((Document) obj.get("hours")).get("Monday"));
//	    
//	    FindIterable<Document> obj2 = qh.simpleMongoQueryFindAll("business", "name", "Starbucks");
	    qh.mongoAggregation2("nOTl4aPC4tKHK35T3bNauQ", 4);
	}    
}
