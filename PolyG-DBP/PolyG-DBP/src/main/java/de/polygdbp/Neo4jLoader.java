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

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;


/**
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
public class Neo4jLoader {
  
  private static DatabaseService dbs;
  private static Driver driver;
  
  public Neo4jLoader(DatabaseService databaseService) {
    Neo4jLoader.dbs = databaseService;
    Neo4jLoader.driver = dbs.getNeo4jDriver();
  };
  
  public void loadYelpDataset() {
    this.schemaAssertion();
    this.loadBusinesses();
    this.loadTips();
    this.loadReviews();
    this.loadUsers();
    
  }
  
  public void schemaAssertion() {
    
    try ( Session session = driver.session() )
    {
      session.writeTransaction( new TransactionWork<Integer>()
      {
        @Override
        public Integer execute( Transaction tx )
        {
          tx.run( "CALL apoc.schema.assert( {Category:['name']}, {Business:['id'],User:['id'],Review:['id']})");
          return 1;
        }
      } );
    }
    
  }
  
  
  public void loadBusinesses()
  {
    try ( Session session = driver.session() )
    {
      session.writeTransaction( new TransactionWork<Integer>()
      {
        @Override
        public Integer execute( Transaction tx )
        {
          tx.run( "CALL apoc.periodic.iterate(\"\n" +
                  "CALL apoc.load.json('file:///home/hjk/Documents/Uni/BigData/data/dataset/business.json') YIELD value RETURN value\n" +
                  "\",\"\n" +
                  "MERGE (b:Business{id:value.business_id})\n" +
                  "SET b += apoc.map.clean(value, ['attributes','hours','business_id','categories','address','postal_code'],[]) \n" +
                  "WITH b,value.categories as categories\n" +
                  "UNWIND categories as category\n" +
                  "MERGE (c:Category{id:category})\n" +
                  "MERGE (b)-[:IN_CATEGORY]->(c)\n" +
                  "\",{batchSize: 10000, iterateList: true});");
          return 1;
        }
      } );
    }
  };
  
  
  public void loadTips()
  {
    try ( Session session = driver.session() )
    {
      session.writeTransaction( new TransactionWork<Integer>()
      {
        @Override
        public Integer execute( Transaction tx )
        {
          tx.run( "CALL apoc.periodic.iterate(\"\n" +
                  "CALL apoc.load.json('file:///home/hjk/Documents/Uni/BigData/data/dataset/tip.json') YIELD value RETURN value\n" +
                  "\",\"\n" +
                  "MATCH (b:Business{id:value.business_id})\n" +
                  "MERGE (u:User{id:value.user_id})\n" +
                  "MERGE (u)-[:TIP{date:value.date,likes:value.likes}]->(b)\n" +
                  "\",{batchSize: 20000, iterateList: true});");
          return 1;
        }
      } );
    }
  };
  
  public void loadReviews()
  {
    try ( Session session = driver.session() )
    {
      session.writeTransaction( new TransactionWork<Integer>()
      {
        @Override
        public Integer execute( Transaction tx )
        {
          tx.run( "CALL apoc.periodic.iterate(\"\n" +
                  "CALL apoc.load.json('file:///home/hjk/Documents/Uni/BigData/data/dataset/review.json') YIELD value RETURN value\n" +
                  "\",\"\n" +
                  "MATCH (b:Business{id:value.business_id})\n" +
                  "MERGE (u:User{id:value.user_id})\n" +
                  "MERGE (r:Review{id:value.review_id})\n" +
                  "MERGE (u)-[:WROTE]->(r)\n" +
                  "MERGE (r)-[:REVIEWS]->(b)\n" +
                  "SET r += apoc.map.clean(value, ['business_id','user_id','review_id','text'],[0]) \n" +
                  "\",{batchSize: 10000, iterateList: true});");
          return 1;
        }
      } );
    }
  };
  
  public void loadUsers()
  {
    try ( Session session = driver.session() )
    {
      session.writeTransaction( new TransactionWork<Integer>()
      {
        @Override
        public Integer execute( Transaction tx )
        {
          tx.run( "CALL apoc.periodic.iterate(\"\n" +
                  "CALL apoc.load.json('file:///home/hjk/Documents/Uni/BigData/data/dataset/user.json') YIELD value RETURN value\n" +
                  "\",\"\n" +
                  "MERGE (u:User{id:value.user_id})\n" +
                  "SET u += apoc.map.clean(value, ['friends','user_id'],[])\n" +
                  "WITH u,value.friends as friends\n" +
                  "UNWIND friends as friend\n" +
                  "MERGE (u1:User{id:friend})\n" +
                  "MERGE (u)-[:FRIEND]-(u1)\n" +
                  "\",{batchSize: 100, iterateList: true});");
          return 1;
        }
      } );
    }
  };
  
}
