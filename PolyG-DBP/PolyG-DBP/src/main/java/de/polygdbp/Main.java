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

import com.mongodb.MongoClientURI;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dient als ausführbare Hauptklasse des PolyG-DB Projektes.
 */
public class Main extends RuntimeException {
  
  /**
   *
   */
  protected static final Logger LOG = LogManager.getLogger(Main.class);
  private String pathDataset;
  private String mongoAddress;
  private String neo4jAddress;
  private int reduceLines;
  private int simulationPercentage;
  
  /**
   *
   */
  public Main() {
    pathDataset = "";
    mongoAddress = "";
    neo4jAddress = "";
    reduceLines = -1;
    simulationPercentage = -1;
  }
  
  /**
   *
   */
  public void run(){
    
    Benchmark bench = new Benchmark();
    if (mongoAddress.isEmpty())
      mongoAddress = "mongodb://localhost:27017";
    if (neo4jAddress.isEmpty())
      neo4jAddress = "jdbc:neo4j:bolt://localhost";
    // @TODO MongoAPI should get the mongoAddress
    // @TODO Try to connect to the MongoDB
    MongoClientURI mongoUri  = new MongoClientURI(mongoAddress);
    MongoAPI mongoApi = new MongoAPI(mongoUri);
    // @TODO Start Neo4j API
    // @TODO Try to connect to the Neo4j
    // <-- BEGIN importing .JSONS into MongoDB -->
    if (pathDataset.isEmpty()){
      LOG.info("No dataset path set with --input. So we will import nothing.");
    } else {
      LOG.info("Dataset path set. Starting to import.");
      LOG.info("This may take some time");
      MongoImporter mongoImporter = new MongoImporter(mongoApi, reduceLines, pathDataset);
      bench.start();
      mongoImporter.importData();
      bench.stop();
      bench.getElapsedSecondsString();
      LOG.info("Execute Mongo-Connector with Neo4j Doc Manager to import MongoDB database into Neo4j database");
      LOG.info("This may take some time");
      bench.start();
      Neo4jDocManager.startMongoConnector();
      bench.stop();
      bench.getElapsedSecondsString();
    }

    LOG.info("Executing MongoDB Query.");
    
    // Aufruf Neo4jAPI.java + MongoAPI.java
    // Aufruf Neo4jQuery.java + MongoQuery.java
    // Aufruf Neo4jExamples.java + MongoExamples.java
    //Neo4jquery.execute(Neo4jexample.giveme(query1));
    // neo4jquery.executeQuery(
    // <-- END importing .JSONS into MongoDB -->
  }
  
  /**
   * Runs PolyG-DBP.
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
   *
   */
  public void help() {
    StringBuilder builder = new StringBuilder();
    builder.append("USAGE:\n")
            .append("java -jar PolyG-DBP-0.1.jar help\n")
            .append("\tDisplays this help\n");
    
    builder.append("java -jar PolyG-DBP-0.1.jar list\n")
            .append("\tlists all queries provided by PolyG-DBP.");
    
    builder.append("java -jar PolyG-DBP-0.1.jar benchmark [Options] ")
            .append("Query\n")
            .append("\tBenchmark with the given query.\n")
            .append("Example: java -jar PolyG-DBP-0.1.jar benchmark q1");
    
    builder.append("OPTIONS (can be specified in any order):\n")
            .append("-i, --input\t\tPath to the input file(s).\n")
            .append("-n, --neo4j\t\tAdress of the neo4j instance\n")
            .append("-m, --mongo\t\tAdress of the mongodb instance\n")
            .append("-s, --simulate\t\tSimulates daily Update from Mongo to Neo4j\n")
            .append("-r, --reduce\t\tReduces each input file to certain number of lines\n");
    System.out.println(builder);
  }
  
  /**
   *
   */
  public void list() {
    StringBuilder builder = new StringBuilder();
    
    builder.append("\n\nAVAILABLE QUERIES FOR THE YELP DATASET\n");
    builder.append("===========================================\n\n");
    builder.append("[q1]:\n");
    builder.append("[q2]:\n");
    builder.append("[q3]:\n");
    builder.append("[q4]:\n");
    builder.append("[q5]:\n");
    System.out.println(builder);
  }
  
  /**
   *
   * @param args
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
    }

    if (args.length % 2 == 0) {
      LOG.error("Unexpected user input. Number of arguments must be odd - one for query, two for each option");
      help();
      throw new UnexpectedParameterException("Even number of parameters");
    }
    String last = "";
    for (int i = 1; i < args.length; ++i) {
      String arg = args[i];
      
      if (arg.startsWith("-") && last.startsWith("-")) {
        LOG.error("Unexpected user input. Parameter "+last+"has no value.");
        help();
        throw new UnexpectedParameterException("No value");
      }
      switch (last) {
        case "-i": case "--input":
          if (!pathDataset.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one input path!");
            throw new UnexpectedParameterException("Multiple input paths");
          }
          pathDataset = arg;
          break;
        case "-n": case "--neo4j":
          if (!neo4jAddress.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one address to the Neo4j!");
            throw new UnexpectedParameterException("Multiple neo4j addresses");
            
          }
          neo4jAddress = arg;
          break;
        case "-m": case "--mongo":
          if (!mongoAddress.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one address to the Mongodb!");
            throw new UnexpectedParameterException("Multiple mongodb addresses");
          }
          mongoAddress = arg;
          break;
        case "-r": case "--reduce":
          if (reduceLines != -1) {
            LOG.error("Unexpected user input. You can only specify one number of reduced lines!");
            throw new UnexpectedParameterException("Multiple numbers of reduced lines");
          }
          reduceLines = Integer.parseInt(arg);
          break;
        case "-s": case "--simulate":
          if (simulationPercentage != -1) {
            LOG.error("Unexpected user input. You can only specify one simulation percentage!");
            throw new UnexpectedParameterException("Multiple simulation percentages");
          }
          simulationPercentage = Integer.parseInt(arg);
          break;
        default:
          LOG.error("Unexpected user input.");
          help();
          throw new UnexpectedParameterException();
      }
      last = arg;
    }
  }
}

