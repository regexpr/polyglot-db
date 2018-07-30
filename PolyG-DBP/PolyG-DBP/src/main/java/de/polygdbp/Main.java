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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dient als ausführbare Hauptklasse des PolyG-DB Projektes.
 */
public class Main {
  private static final Logger LOG = LogManager.getLogger(Main.class);
  private static String inputPath = "";
  private static String mongoAddress = "";
  private static String neo4jAddress = "";
  private static int reduceLines =  -1;
  private static int simulationPercentage = -1;
  
  /**
   * Hauptmethode.
   * @param args enthält die Kommandozeilenoptionen.
   */
  public static void main(String[] args){
    
    LOG.info("Starting Polyglot Logging!");
    LOG.info("Checking arguments");
    checkUserInput(args);
    
    if (mongoAddress.isEmpty())
      mongoAddress = "localhost://774";
    if (neo4jAddress.isEmpty())
      neo4jAddress = "localhost://774";
    
    if (inputPath.isEmpty()){
      LOG.info("No dataset path set with --input. So we will import nothing.");
    } else {
      LOG.info("Dataset path set. Starting to import.");
      //MongoImporter mongoImporter = new MongoImporter();
    }
    
  }
  
  private static void help() {
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
    System.exit(0);
  }
  private static void list() {
    StringBuilder builder = new StringBuilder();
    
    builder.append("\n\nAVAILABLE QUERIES FOR THE YELP DATASET\n");
    builder.append("===========================================\n\n");
    builder.append("[q1]:\n");
    builder.append("[q2]:\n");
    builder.append("[q3]:\n");
    builder.append("[q4]:\n");
    builder.append("[q5]:\n");
    System.out.println(builder);
    System.exit(0);
  }
  private static void checkUserInput(final String[] args) {
    // User input handling
    if (args.length == 0) {
      LOG.error("Unexpected user input. No query set.");
      help();
    }
    if (args[0].equalsIgnoreCase("list"))
      list();
    if (args[0].equalsIgnoreCase("help")) 
      help();
    if (!args[0].startsWith("q")) {
      LOG.error("Unexpected user input. First Argument should start with 'q'.");
      help();
    }
    if (args.length % 2 == 0) {
      LOG.error("Unexpected user input. Number of arguments must be odd - one for query, two for each option");
      help();
    }
    
    String last = "";
    for (int i = 1; i < args.length; ++i) {
      String arg = args[i];
      
      if (arg.startsWith("-") && last.startsWith("-")) {
        LOG.error("Unexpected user input. Parameter "+last+"has no value.");
        help();
      }
      switch (last) {
        case "-i": case "--input":
          if (!inputPath.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one input path!");
            System.exit(0);
          }
          inputPath = arg;
          break;
        case "-n": case "--neo4j":
          if (!neo4jAddress.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one address to the Neo4j!");
            System.exit(0);
          }
          neo4jAddress = arg;
          break;
        case "-m": case "--mongo":
          if (!mongoAddress.isEmpty()) {
            LOG.error("Unexpected user input. You can only specify one address to the Mongodb!");
            System.exit(0);
          }
          mongoAddress = arg;
          break;
        case "-r": case "--reduce":
          if (reduceLines != -1) {
            LOG.error("Unexpected user input. You can only specify one number of reduced lines!");
            System.exit(0);
          }
          reduceLines = Integer.parseInt(arg);
          break;
        case "-s": case "--simulate":
          if (simulationPercentage != -1) {
           LOG.error("Unexpected user input. You can only specify one simulation percentage!");
            System.exit(0);
          }
          simulationPercentage = Integer.parseInt(arg);
          break;
        default:
          LOG.error("Unexpected user input.");
          help();
          System.exit(0);
          break;
      }
      
      last = arg;
    }
  }
}

