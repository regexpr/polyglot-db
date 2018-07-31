package de.polygdbp;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

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

/**
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
public class Neo4jAPI {
  private Driver driver;
  
  //connects to Neo4j with credentials
  
  public Neo4jAPI(String uri) {
    this.driver = GraphDatabase.driver(uri);
  }
  
  /**
   *
   * @return
   */
  public Driver getNeo4jDriver() {
    return driver;
  }
  
  /**
   *
   */
  public void closeNeo4j() {
    driver.close();
  }
}
