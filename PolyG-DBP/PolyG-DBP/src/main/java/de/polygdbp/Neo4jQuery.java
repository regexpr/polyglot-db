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

import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

/**
 * Execute queries for the Neo4j and get the results back.
 */
public class Neo4jQuery {
  Neo4jAPI neo4jApi;
  
  /**
   * Constructor needs neo4jApi.
   * @param neo4jApi
   */
  public Neo4jQuery(Neo4jAPI neo4jApi) {
    this.neo4jApi = neo4jApi;
  }
  
  /**
   * Standard query method.
   * @param query
   * @return
   */
  public List<Object> customNeo4jQuery(String query)	{
    
    try ( Session session = neo4jApi.getNeo4jDriver().session() ) {
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
