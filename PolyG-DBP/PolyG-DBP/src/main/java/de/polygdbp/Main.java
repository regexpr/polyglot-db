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

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Serves as executable Main class of the PolyG-DB project.
 * @author Tim Niehoff, Hyeon Ung Kim
 * Please note, that in all other Java Classes @author is left out in the javadoc as the
 * License header already contains the contributors.
 *
 */
public class Main extends RuntimeException {
  /**
   * Logger tracks all stages of a PolyG-DBP run by the level "info",
   * in some circumstances errors by the same-named level and
   * performance results through the level results.
   */
  protected static final Logger LOG = LogManager.getLogger(Main.class);
  
  /**
   * Custom LogLevel to indicate relevant Benchmark lines.
   */
  protected static final Level BENCHMARK = Level.forName("BENCHMARK-RESULT", 400);
  /**
   * Parameters to specify a run of PolyG-DBP. All of them are optional.
   */
  private String pathDataset;
  private String mongoAddress;
  private String mongoDatabase;
  // Bolt Neo4j Address
  private String neo4jAddressBolt;
  // Remote Neo4j Address
  private String neo4jAddressRemote;
  private int reduceLines;
  /**
   * Mandatory parameter that has to be set by the user.
   */
  private String queryName;
  
  /**
   * Constructor initializes variables that can later be filled by the users CLI.
   */
  public Main() {
    pathDataset = "";
    mongoAddress = "";
    mongoDatabase="";
    neo4jAddressBolt = "";
    neo4jAddressRemote = "";
    reduceLines = -1;
  }
  
  /**
   * run() contents the common thread by connecting to the databases, optionally importing .JSONs into
   * the MongoDB, filling and synchronizing the Neo4jDB by use of mongo-connector,
   * executing queries on both databases, logging and measering the time.
   */
  public void run(){
    // Connect to a running MongoDB by calling MongoAPI constructor
    MongoAPI mongoApi = new MongoAPI("mongodb://"+mongoAddress, "polyg-dbp");
    // Connect to a running Neo4j by calling Neo4jAPI constructor
    Neo4jAPI neo4jApi = new Neo4jAPI("bolt://"+neo4jAddressBolt);
    // <========================= BEGIN Importing .JSONs into MongoDB =========================>
    if (pathDataset.isEmpty()){
      LOG.info("No dataset path set with --input. So we will import nothing.");
    } else {
      LOG.info("Dataset path set. Starting to import.");
      LOG.info("This may take some time");
      MongoImporter mongoImporter = new MongoImporter(mongoApi, reduceLines, pathDataset);
      Benchmark benchMongoImporter = new Benchmark("MongoImporter");
      benchMongoImporter.start();
      mongoImporter.importData();
      benchMongoImporter.writeDurationToLOG('s');
      // <========================= END Importing .JSONs into MongoDB =========================>
      // <========================= BEGIN Mongo-Connector =========================>
      // <-- BEGIN Mongo-Connector -->
      LOG.info("Execute Mongo-Connector with Neo4j Doc Manager to import MongoDB database into Neo4j database");
      LOG.info("This may take some time");
      Benchmark benchMongoConnector = new Benchmark("Mongo-Connector/Neo4j Doc Manager");
      benchMongoConnector.start();
      Neo4jDocManager docManager = new Neo4jDocManager(mongoAddress, neo4jAddressRemote);
      docManager.startMongoConnector();
      benchMongoConnector.writeDurationToLOG('s');
      // <========================= END Mongo Connector =========================>
    }
    // <========================= BEGIN Queries =========================>
    MongoQuery mongoQuery = new MongoQuery(mongoApi);
    Neo4jQuery neo4jQuery = new Neo4jQuery(neo4jApi);
    LOG.info("Executing MongoDB Query.");
    Benchmark benchMongoQuery = new Benchmark("Execution of a MongoDB Query " + queryName);
    benchMongoQuery.start();
    MongoExamples mongoExamples = new MongoExamples();
    String mongoQueryString = mongoExamples.getQuery(queryName);
    mongoQuery.customMongoAggregation(mongoQueryString);
    benchMongoQuery.writeDurationToLOG('n');
    LOG.info("Results of the MongoDB Query:\n"+mongoQuery.getResults());
    Benchmark benchNeoQuery = new Benchmark("Execution of a Neo4j Query" + queryName);
    benchNeoQuery.start();
    Neo4jExamples neo4jExamples = new Neo4jExamples();
    String neo4jQueryString = neo4jExamples.getQuery(queryName);
    List<Object> neo4jResults = neo4jQuery.customNeo4jQuery(neo4jQueryString);
    benchNeoQuery.writeDurationToLOG('n');
    LOG.info("Results of the Neo4j Query:\n");
    neo4jResults.toString();
    // Compare Neo4j and MongoDB Query Execution
    BenchmarkComparison benchCompare = new BenchmarkComparison(benchMongoQuery, benchNeoQuery);
    benchCompare.writeDurationComparisonToLOG();
    // <========================= END Queries =========================>
    LOG.info("Stopping PolyG-DBP");
    LOG.info("The results are written in the file benchmark.log");
  }
  
  /**
   * Starting method of the PolyG-DBP. Initializes Main Class and runs checkUserInput() and run().
   * @param args contains users command line arguments.
   */
  public static void main(String[] args){
    Main main = new Main();
    LOG.info("Starting Polyglot Logging!");
    LOG.info("Processing command line arguments: "+Arrays.toString(args));
    main.checkUserInput(args);
    main.run();
  }
  
  /**
   * help() prints help instructions to the console with expected valid CLI arguments.
   */
  public void help() {
    StringBuilder builder = new StringBuilder();
    builder.append("USAGE:\n")
            .append("java -jar PolyG-DBP-0.1.jar help\n")
            .append("\tDisplays this help\n");
    
    builder.append("java -jar PolyG-DBP-0.1.jar list\n")
            .append("\tlists all queries provided by PolyG-DBP.");
    
    builder.append("java -jar PolyG-DBP-0.1.jar [Options] QUERY\n")
            .append("\tBenchmark with the given query.\n")
            .append("Example: java -jar PolyG-DBP-0.1.jar q1");
    
    builder.append("OPTIONS (can be specified in any order):\n")
            .append("-i, --input\t\tPath to the directory with JSON file(s). Example: \"-i /yelp\"\n")
            .append("-nb, --neo4jAddressBolt1\t\tAdress of the neo4j instance with the bolt address. Example: \"-nb localhost:7687\"\n")
            .append("-nr, --neo4jAddressRemote\t\tAdress of the neo4j instance with the remote address. Example: \"-nr localhost:7474\"\n")
            .append("-m, --mongoAddress\t\tAdress of the mongodb instance. Example: \"-m localhost:27017\"\n")
            .append("-md, --mongoDatabase\t\tName of the mongodb database. Example: \"-md yelp\"\n")
            .append("-r, --reduce\t\tImport just a certain amount of lines of each input JSON. Example: \"-r 300\"\n");
    System.out.println(builder);
  }
  
  /**
   * list() contents all hard-coded example queries with the related short form.
   */
  public void list() {
    StringBuilder builder = new StringBuilder();
    builder.append("\n\nAVAILABLE QUERIES FOR THE YELP DATASET\n");
    builder.append("=======================================================================================================\n\n");
    builder.append("[q1]:\n");
    builder.append("[q2]:\n");
    builder.append("[q3]:\n");
    builder.append("[q4]:\n");
    builder.append("[q5]:\n");
    System.out.println(builder);
  }
  
  /**
   * checkUserInput() validates all set CLI parameters and assigns all Class variables with
   * the related parameter values.
   * @param args contents users CLI parameters.
   */
  public void checkUserInput(final String[] args) {
    // User input handling
    if (args[0].equalsIgnoreCase("list")){
      list();
      System.exit(0);
    }
    if (args[0].equalsIgnoreCase("help")) {
      help();
      System.exit(0);
    }
    if ((args == null) || (args.length == 0) || (!args[0].startsWith("q"))){
      LOG.error("Unexpected user input. No query set.");
      help();
      throw new UnexpectedParameterException("No query");
    } else {
      queryName = args[0];
    }
    if (args.length % 2 == 0) {
      LOG.error("Unexpected user input. Number of arguments must be odd - one for query, two for each option");
      help();
      throw new UnexpectedParameterException("Even number of parameters");
    }
    String lastArgument = "";
    for (int i = 1; i < args.length; ++i) {
      String currentArgument = args[i];
      if (currentArgument.startsWith("-") && lastArgument.startsWith("-")) {
        LOG.error("Unexpected user input. Parameter "+lastArgument+"has no value.");
        help();
        throw new UnexpectedParameterException("No value");
      }
      switch (lastArgument) {
        case "-i": case "--input":
          if (!pathDataset.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one input path!");
            throw new UnexpectedParameterException("Multiple input paths");
          }
          pathDataset = currentArgument;
          break;
        case "-m": case "--mongoAddress":
          if (!mongoAddress.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one address to the Mongodb!");
            throw new UnexpectedParameterException("Multiple mongodb addresses");
          }
          mongoAddress = currentArgument;
          break;
        case "-md": case "--mongoDatabase":
          if (!mongoDatabase.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one Mongodb database name!");
            throw new UnexpectedParameterException("Multiple mongodb database names");
          }
          mongoDatabase = currentArgument;
          break;
        case "-nb": case "--neo4jAddressBolt":
          if (!neo4jAddressBolt.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one bolt address to the Neo4j!");
            throw new UnexpectedParameterException("Multiple neo4j bolt addresses");
          }
          neo4jAddressBolt = currentArgument;
          break;
        case "-nr": case "--neo4jAddressRemote":
          if (!neo4jAddressRemote.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one remote address to the Neo4j!");
            throw new UnexpectedParameterException("Multiple neo4j remote addresses");
          }
          neo4jAddressRemote = currentArgument;
          break;
        case "-r": case "--reduce":
          if (reduceLines != -1) {
            LOG.error("Unexpected user input. You can only specify one number of reduced lines!");
            throw new UnexpectedParameterException("Multiple numbers of reduced lines");
          }
          reduceLines = Integer.parseInt(currentArgument);
          break;
      }
      lastArgument = currentArgument;
    }
    if (mongoAddress.isEmpty())
      mongoAddress = "localhost:27017";
    if (neo4jAddressBolt.isEmpty())
      neo4jAddressBolt = "localhost:7687";
    if (neo4jAddressRemote.isEmpty())
      neo4jAddressRemote = "localhost:7474";
    if (mongoDatabase.isEmpty())
      mongoDatabase = "yelp";
  }
}