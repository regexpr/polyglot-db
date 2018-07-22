/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.polygdbp;

import java.net.UnknownHostException;

import org.json.JSONObject;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 *
 * @author tim
 */
public class Main {

	public static void main(String[] argv) throws UnknownHostException{
	    DatabaseService dbs = new DatabaseService();
	    
	    
	    QueryHandler qh = new QueryHandler(dbs);
	    
	    DBObject obj = qh.simpleMongoQueryFindOne("business", "name", "Starbucks");
	    System.out.println(((DBObject)obj.get("hours")).get("Monday"));

	    
	}    
}
