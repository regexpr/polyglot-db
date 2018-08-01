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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({de.polygdbp.Neo4jDocManagerTest.class, de.polygdbp.MongoAPITest.class, de.polygdbp.MainTest.class, de.polygdbp.MongoQueryBuilderTest.class, de.polygdbp.Neo4jQueryTest.class, de.polygdbp.Neo4jAPITest.class, de.polygdbp.MongoImporterTest.class, de.polygdbp.MongoQueryTest.class, de.polygdbp.Neo4jExamplesTest.class, de.polygdbp.UnexpectedParameterExceptionTest.class, de.polygdbp.BenchmarkTest.class, de.polygdbp.BenchmarkComparisonTest.class, de.polygdbp.MongoExamplesTest.class})
public class PolygdbpSuite {
  
}
