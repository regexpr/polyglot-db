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
package de.polygdbp.Main;

import de.polygdbp.Benchmark.Benchmark;
import de.polygdbp.Benchmark.BenchmarkComparison;
import de.polygdbp.Neo4J.Neo4jExamples;
import de.polygdbp.Neo4J.Neo4jDocManager;
import de.polygdbp.Neo4J.Neo4jQuery;
import de.polygdbp.Neo4J.Neo4jAPI;
import de.polygdbp.MongoDB.MongoExamples;
import de.polygdbp.MongoDB.MongoImporter;
import de.polygdbp.MongoDB.MongoAPI;
import de.polygdbp.MongoDB.MongoQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

/**
 * Serves as executable PolyGDBP class of the PolyG-DB project.
 * @author Tim Niehoff, Hyeon Ung Kim
 * Please note, that in all other Java Classes @author is left out in the javadoc as the
 * License header already contains the contributors.
 *
 */
public class PolyGDBP extends RuntimeException {
  /**
   * Logger tracks all stages of a PolyG-DBP run by the level "info",
   * in some circumstances errors by the same-named level and
   * performance results through the level results.
   */
  public static final Logger LOG = LogManager.getLogger(PolyGDBP.class);
  
  /**
   * Custom LogLevel to indicate relevant Benchmark lines.
   */
  public static final Level BENCHMARK = Level.forName("BENCHMARK-RESULT", 400);
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
  // For pre-built YELP queries. Could be q1,q2,..., or qa
  private String preBuiltQuery;
  // Otherwise custom queries.
  private String mongoQuery;
  private String neo4jQuery;
  
  /**
   * Constructor initializes variables that can later be filled by the users CLI.
   */
  public PolyGDBP() {
    pathDataset = "";
    mongoAddress = "";
    mongoDatabase="";
    mongoQuery = "";
    neo4jAddressBolt = "";
    neo4jAddressRemote = "";
    neo4jQuery = "";
    preBuiltQuery = "";
    reduceLines = -1;
    
  }
  
  /**
   * run() contents the common thread by connecting to the databases, optionally importing .JSONs into
   * the MongoDB, filling and synchronizing the Neo4jDB by use of mongo-connector,
   * executing queries on both databases, logging and measering the time.
   */
  public void run(){
    // Connect to a running MongoDB by calling MongoAPI constructor
    MongoAPI mongoApi = new MongoAPI("mongodb://"+mongoAddress, mongoDatabase);
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
      LOG.info("Execute Mongo-Connector with Neo4j Doc Manager to import MongoDB database into Neo4j database");
      LOG.info("This may take some time");
      Benchmark benchMongoConnector = new Benchmark("Mongo-Connector/Neo4j Doc Manager");
      benchMongoConnector.start();
      Neo4jDocManager docManager = new Neo4jDocManager(mongoAddress, neo4jAddressRemote);
      docManager.startMongoConnector();
      benchMongoConnector.writeDurationToLOG('s');
      // <========================= END Mongo Connector =========================>
    }
    // <========================= BEGIN Executing Queries =========================>
    if (!this.preBuiltQuery.isEmpty()){
      MongoExamples mongoExamples = new MongoExamples();
      Neo4jExamples neo4jExamples = new Neo4jExamples();
      if (this.preBuiltQuery.equalsIgnoreCase("qa")){
        // Execute all prebuilt queries
        // 7 is the number of all prebuilt queries.
        // @TODO: Refactor. No hard numbers, please.
        for (int i=1; i <= 7; i++) {
          this.mongoQuery = mongoExamples.getQuery("q"+i);
          this.neo4jQuery = neo4jExamples.getQuery("q"+i);
          executeQuery(mongoApi, neo4jApi);
        }
      } else {
        // Execute Prebuilt Query.
        this.mongoQuery = mongoExamples.getQuery(preBuiltQuery);
        this.neo4jQuery = neo4jExamples.getQuery(preBuiltQuery);
        executeQuery(mongoApi, neo4jApi);
      }
    } else {
      // Execute Custom Query
      executeQuery(mongoApi, neo4jApi);
    }
    // <========================= END Executing Queries =========================>
    
    LOG.info("Stopping PolyG-DBP");
    LOG.info("The results are written in the file PolyG-DBP.log as well as in benchmarkResults.log.");
  }
  
  /**
   *
   * @param mongoApi
   * @param neo4jApi
   */
  public void executeQuery(MongoAPI mongoApi, Neo4jAPI neo4jApi){
    MongoQuery mongoQueryHandler = new MongoQuery(mongoApi);
    Neo4jQuery neo4jQueryHandler = new Neo4jQuery(neo4jApi);
    LOG.info("Executing MongoDB Query and measuring time.");
    // Mongo Benchmark
    //Benchmark benchMongoQuery = new Benchmark("Execution of a MongoDB Query " + this.mongoQuery);
    Benchmark benchMongoQuery = new Benchmark("MongoDB");
    List<Document> mongoQueryList = mongoQueryHandler.buildQuery(this.mongoQuery);
    benchMongoQuery.start();
    mongoQueryHandler.executeQuery(mongoQueryList);
    benchMongoQuery.writeDurationToLOG('n');
    // Mongo Results
    LOG.info("Results of the MongoDB Query:");
    for(int i=0; i<mongoQueryHandler.getResults().size(); i++) {
      LOG.info(mongoQueryHandler.getResults().get(i));
    }
    LOG.info("Executing Neo4j Query and measuring time.");
    // Neo4j Benchmark
    //Benchmark benchNeoQuery = new Benchmark("Execution of a Neo4j Query" + this.neo4jQuery);
    Benchmark benchNeoQuery = new Benchmark("Neo4j");
    benchNeoQuery.start();
    List<Object> neo4jResults = neo4jQueryHandler.customNeo4jQuery(neo4jQuery);
    benchNeoQuery.writeDurationToLOG('n');
    // Neo4j Results
    LOG.info("Results of the Neo4j Query:");
    LOG.info(neo4jResults.toString());
    
    // Compare Neo4j and MongoDB Query Execution
    BenchmarkComparison benchCompare = new BenchmarkComparison(benchMongoQuery, benchNeoQuery);
    // Write into log
    benchCompare.writeDurationComparisonToLOG();
    // Write into benchmarkResults.log
    benchCompare.writeToResultsFile();
    
  }
  /**
   * Starting method of the PolyG-DBP. Initializes PolyGDBP Class and runs checkUserInput() and run().
   * @param args contains users command line arguments.
   */
  public static void main(String[] args){
    PolyGDBP main = new PolyGDBP();
    LOG.info("Starting Polyglot Logging!");
    LOG.debug("Processing command line arguments: "+Arrays.toString(args));
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
    
    builder.append("java -jar PolyG-DBP-0.1.jar QUERY [Options]\n")
            .append("\tBenchmark with a query from PolyG-DBP for the YELP dataset.\n")
            .append("Example: java -jar PolyG-DBP-0.1.jar q1\n");
    
    builder.append("java -jar PolyG-DBP-0.1.jar custom [Options]\n")
            .append("\tBenchmark with a custom query from PolyG-DBP. "
                    + "You will be asked to specify your queries for Mongo and Neo4j afterwards.\n")
            .append("Example: java -jar PolyG-DBP-0.1.jar custom.\n");
    
    builder.append("OPTIONS (can be specified in any order):\n")
            .append("-i, --input\tPath to the directory with JSON file(s). Example: \"-i /yelp\"\n")
            .append("-m, --mongoAddress\tAdress of the mongodb instance. Example: \"-m localhost:27017\"\n")
            .append("-md, --mongoDatabase\tName of the mongodb database. Example: \"-md yelp\"\n")
            .append("-nb, --neo4jAddressBolt1\tAdress of the neo4j instance with the bolt address. Example: \"-nb localhost:7687\"\n")
            .append("-nr, --neo4jAddressRemote\tAdress of the neo4j instance with the remote address. Example: \"-nr localhost:7474\"\n")
            .append("-r, --reduce\tImport just a certain amount of lines of each input JSON. Example: \"-r 300\"\n");
    System.out.println(builder);
  }
  
  /**
   * list() contents all hard-coded example queries with the related short form.
   */
  public void list() {
    StringBuilder builder = new StringBuilder();
    builder.append("\n\nAVAILABLE QUERIES FOR THE YELP DATASET\n");
    builder.append("Vist https://www.yelp.com/dataset to read more.\n");
    builder.append("=======================================================================================================\n\n");
    builder.append("q1:\tOutput all business names and ids a <specific user> rated with minumum of <stars>\n");
    builder.append("q2:\tOutput the average stars of all businesses\n");
    builder.append("q3:\tOutput the average stars of all businesses that grouped by category\n");
    builder.append("q4:\tOutput the average stars of all businesses grouped by category descending order\n");
    builder.append("q5:\tOutput all businesses that are in the category Cannabis Tours and return the average of all stars grouped by all the categories that they are in\n");
    builder.append("q6:\tOutput all businesses that were reviewed more than 700 times\n");
    builder.append("q7:\tThe same query as above, but this time we count the reviews in the reviews collection\n");
    builder.append("qa:\tRun all queries above.\n");
    
    System.out.println(builder);
  }
  
  /**
   * checkUserInput() validates all set CLI parameters and assigns all Class variables with
   * the related parameter values.
   * @param args contents users CLI parameters.
   */
  public void checkUserInput(final String[] args) {
    // User input handling
    if ((args == null) || (args.length == 0)){
      LOG.error("Unexpected user input. No query set.");
      help();
      throw new UnexpectedParameterException("No query");
    } 
    if (args[0].equalsIgnoreCase("list")){
      list();
      System.exit(0);
    }
    if (args[0].equalsIgnoreCase("help")) {
      help();
      System.exit(0);
    }
    if (args[0].equalsIgnoreCase("custom")) {
      askForCustomQueries();
    } else if (args[0].startsWith("q")){
      preBuiltQuery = args[0];
    } else {
      LOG.error("Unexpected user input. No query set.");
      help();
      throw new UnexpectedParameterException("No query");
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
  
  private void askForCustomQueries() {
    // Create a Scanner using the InputStream available.
    Scanner scanner = new Scanner( System.in );
    // Don't forget to prompt the user
    System.out.print( "Please enter your MongoDB query. Do not use white spaces except in names.\n" );
    System.out.print("Example: db.business.aggregate({$group:{_id: null,\"average_stars\":{$avg:\"$stars\"}}})\n");
    // Use the Scanner to read a line of text from the user.
    this.mongoQuery = scanner.nextLine();
    // The same procedure as given above.
    System.out.print("Please enter your Neo4j query.\n" );
    System.out.print("Example: MATCH (b:business) return avg(b.stars)\n");
    this.neo4jQuery = scanner.nextLine();
    LOG.info("Finished handling entering custom queries");
    LOG.info("MongoDB query: "+mongoQuery);
    LOG.info("Neo4j query: "+neo4jQuery);
  }
}