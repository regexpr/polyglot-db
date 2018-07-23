package test.java;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import main.java.de.polygdbp.DatabaseService;
import main.java.de.polygdbp.QueryHandler;

class QueryTester{
	DatabaseService dbs;
	QueryHandler qh;
	
	@Before
	void connect() throws UnknownHostException {
		this.dbs = new DatabaseService("localhost", 27017, "yelp");
		this.qh = new QueryHandler(dbs);
	}
	
	
	
	@Test
	void testSimpleMongoQueryOne() {
		DBObject obj = (DBObject) JSON.parse("{ \"_id\" : { \"$oid\" : \"5b40f6c23707bc7df7bae7f5\"} , \"business_id\" : \"lHYiCS-y8AFjUitv6MGpxg\" , \"name\" : \"Starbucks\" , \"neighborhood\" : \"Liberty Village\" , \"address\" : \"85 Hanna Avenue\" , \"city\" : \"Toronto\" , \"state\" : \"ON\" , \"postal_code\" : \"M6K 3S3\" , \"latitude\" : 43.6398633116 , \"longitude\" : -79.4195331865 , \"stars\" : 4.0 , \"review_count\" : 21 , \"is_open\" : 1 , \"attributes\" : { \"BusinessParking\" : { \"garage\" : false , \"street\" : true , \"validated\" : false , \"lot\" : false , \"valet\" : false} , \"Caters\" : false , \"WiFi\" : \"free\" , \"OutdoorSeating\" : true , \"BusinessAcceptsCreditCards\" : true , \"RestaurantsPriceRange2\" : 2 , \"BikeParking\" : true , \"RestaurantsTakeOut\" : true , \"WheelchairAccessible\" : true} , \"categories\" : [ \"Food\" , \"Coffee & Tea\"] , \"hours\" : { \"Monday\" : \"5:30-22:00\" , \"Tuesday\" : \"5:30-22:00\" , \"Friday\" : \"5:30-23:00\" , \"Wednesday\" : \"5:30-22:00\" , \"Thursday\" : \"5:30-22:00\" , \"Sunday\" : \"7:00-22:00\" , \"Saturday\" : \"6:00-23:00\"}}");
		
		assertEquals(obj, qh.simpleMongoQueryFindOne("business", "name", "Starbucksssss"));
	}

}
