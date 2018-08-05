package de.polygdbp.Neo4J;

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
 * Establishes a connection with a running Neo4J instance. It contains several API methods.
 */
public class Neo4jAPI {
  private final Driver driver;

  
  /**
   * The Constructor tries to connect to a running Neo4J instance.
   * @param uri Address of the running Neo4J. Valid example: "bolt://localhost:7687".
   */
  public Neo4jAPI(String uri) {
    this.driver = GraphDatabase.driver(uri);
  }
  
  /**
   * Getter method of the Neo4j Driver (API)
   * @return neo4j driver
   */
  public Driver getNeo4jDriver() {
    return driver;
  }
  
  /**
   * Closes a connection to the current Neo4j instance.
   */
  public void closeNeo4j() {
    driver.close();
  }
}
