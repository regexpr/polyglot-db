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

import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dient als ausführbare Hauptklasse des PolyG-DB Projektes.
 */
public class Main {
  private static final Logger LOG = LogManager.getLogger(Main.class);
  
  /**
   * Hauptmethode.
   * @param args enthält die Kommandozeilenoptionen.
   */
  public static void main(String[] args){
    
    LOG.info("Polyglot Logging!");
        
    if (args.length < 1) {
      help();
      return;
    }
    checkUserInput(args);
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
            .append("\tBenchmark with the given query.\n");
    
    builder.append("OPTIONS (can be specified in any order):\n")
            .append("-i, --input\t\tPath to the input file(s).\n")
            .append("-n, --neo4j\t\tAdress of the neo4j instance\n")
            .append("-m, --mongo\t\tAdress of the mongodb instance\n")
            .append("-s, --simulate\t\tSimulates daily Update from Mongo to Neo4j\n")
            .append("-r, --reduce\t\tReduces each input file to certain number of lines\n")
            .append("    --quiet\t\tSet the default log-level to quiet.\n")
            .append("    --verbose\t\tSet the default log-level to verbose.\n");
    System.out.println(builder);
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
  }
  
  
  private static void checkUserInput(final String[] args) {
    // User input handling
    if (args[0].equalsIgnoreCase("help")) {
      help();
      return;
    } else if (args[0].equalsIgnoreCase("list")) {
      list();
      return;
    }
    else if (args[0].equalsIgnoreCase("benchmark") && args.length < 2 ) {
      //Not enough arguments
      System.out.println(
              "Usage: java -jar PolyG-DBP-0.1.jar benchmark"
                      + "[OPTIONS] QUERY\n");
      System.out.println("Example: java -jar PolyG-DBP-0.1.jar benchmark"
              + "q1");
      return;
    } else if (!args[0].equalsIgnoreCase("benchmark")) {
      System.out.println("Unknown command: " + args[0]);
      System.out.println("See help for usage: java -jar PolyG-DBP-0.1.jar help");
      return;
    }
    
    String inputPath = "";
    String neo4jPath = "";
    String mongoPath = "";
    String reduceLines = "";
    String simulationPercentage = "";
    
    boolean needsValue = false;
    String last = "";
    
    if (args.length > 3) {
      for (int i = 1; i < args.length - 3; ++i) {
        String arg = args[i];
        
        if (arg.startsWith("-") && needsValue) {
          System.out.println("Parameter " + last + " needs value.");
          System.exit(0);
        }
        switch (arg) {
          case "--verbose":
            //LogManager.getRootLogger().setLevel(Level.ALL);
            break;
          case "--quiet":
            // LogManager.getRootLogger().
            break;
          case "-i": case "--input": case "-n": case "--neo4j": case "-m":
          case "--mongo": case "-s": case "--simulate": case "-r":
          case "--reduce":
            needsValue = true;
            break;
          default:
            if (needsValue) {
              switch (last) {
                case "-i": case "--input":
                  if (!inputPath.isEmpty()) {
                    System.out.println("You can only specify one input path!");
                    System.exit(0);
                  }
                  inputPath = arg;
                  break;
                case "-n": case "--neo4j":
                  if (!neo4jPath.isEmpty()) {
                    System.out.println("You can only specify one address to the Neo4j!");
                    System.exit(0);
                  }
                  neo4jPath = arg;
                  break;
                case "-m": case "--mongo":
                  if (!mongoPath.isEmpty()) {
                    System.out.println("You can only specify one address to the Mongodb!");
                    System.exit(0);
                  }
                  mongoPath = arg;
                  break;
                case "-r": case "--reduce":
                  if (!reduceLines.isEmpty()) {
                    System.out.println("You can only specify one number of reduced lines!");
                    System.exit(0);
                  }
                  reduceLines = arg;
                  break;
                case "-s": case "--simulate":
                  if (!simulationPercentage.isEmpty()) {
                    System.out.println("You can only specify one simulation percentage!");
                    System.exit(0);
                  }
                  simulationPercentage = arg;
                  break;
                default:
                  //This can not happen
                  break;
              }
              needsValue = false;
              break;
            }
            System.out.println("Unknown command line option: " + arg);
            System.exit(0);
        }
        
        if (i < args.length - 1) {
          last = arg;
        }
      }
      if (needsValue) {
        System.out.println("Parameter " + last + " needs value.");
        System.exit(0);
      }
      startBenchmark();
    }
    
    
  }
  
  private static void startBenchmark() {
    boolean err = false;
    if (err) {
      System.exit(-1);
    }
  }
}

