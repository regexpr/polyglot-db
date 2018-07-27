/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.polygdbp;

import java.net.UnknownHostException;
import java.util.List;

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
		DatabaseService dbs = new DatabaseService("localhost", 27017, "yelp", "bolt://localhost:7687", "yoshi", "mitsu");
		QueryHandler qh = new QueryHandler(dbs);
		List<Object> results = qh.customNeo4jQuery("MATCH (u:User)-[:WROTE]-(r:Review)-[:REVIEWS]-(Business) WHERE id(u)=214195 AND r.stars > 2 Return Business.name");
		for(int i=0; i<results.size(); i++) {
			System.out.println(results.get(i));
		}
	}    
}
