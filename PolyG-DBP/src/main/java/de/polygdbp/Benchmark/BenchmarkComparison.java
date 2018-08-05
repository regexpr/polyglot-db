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
package de.polygdbp.Benchmark;

import static de.polygdbp.Main.PolyGDBP.LOG;
import static de.polygdbp.Main.PolyGDBP.BENCHMARK;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Compares two measured time durations in form of de.polygdbp.Benchmark with each other.
 */
public class BenchmarkComparison {
  private final Benchmark benchmark1;
  private final Benchmark benchmark2;
  
  /**
   * Constructor.
   * @param benchmark1.
   * @param benchmark2.
   */
  public BenchmarkComparison(Benchmark benchmark1, Benchmark benchmark2) {
    this.benchmark1 = benchmark1;
    this.benchmark2 = benchmark2;
  }
  
  /**
   * Writes the comparison of two time durations into the LOG.
   */
  public void writeDurationComparisonToLOG(){
    long delta1 = benchmark1.getDuration();
    long delta2 = benchmark2.getDuration();
    String processName1 = benchmark1.getProcessName();
    String processName2 = benchmark2.getProcessName();
    
    if (delta1 < delta2) {
      // Swap variables to ensure the correct ordering of both time durations.
      long temp = delta1;
      delta1 = delta2;
      delta2 = temp;
      
      String tempName = processName1;
      processName1 = processName2;
      processName2 = tempName;
    }
    double percentage = Math.ceil(((double)delta1/delta2)*100);
    LOG.log(BENCHMARK,"Process "+processName1+" with "+ delta1 + " ns\n took " + (delta1-delta2) + " ns longer ("+ percentage+ "%) "
            +"than process" + processName2 + "with " + delta2 + " ns.");
  }
  /**
   * Appends the Durations and Process names of two Benchmarks as a JSON object into the benchmarkResults.log.
   */
  public void writeToResultsFile() {
    // check if file already exists
    // if not create benchmarkResults.log
    // write into benchmarkResults.log
    String fileName = "benchmarkResults.log";
    String result = "{\""+benchmark1.getProcessName()+"\" : "+benchmark1.getDuration()+",\""+ benchmark2.getProcessName()+"\" : "+benchmark2.getDuration()+"}";
    BufferedWriter bw = null;
    FileWriter fw = null;
    try {
      File file = new File(fileName);
      // if file doesnt exists, then create it
      if (!file.exists()) {
        file.createNewFile();
      } else {
      // Append "," for keeping the JSON format.
      result = ","+result;
      }
      // true = append file
      fw = new FileWriter(file.getAbsoluteFile(), true);
      bw = new BufferedWriter(fw);
      bw.write(result);
      LOG.info("query result has been written into benchmarkResults.log");
    } catch (IOException e) {
      
      e.printStackTrace();
      
    } finally {
      
      try {
        
        if (bw != null)
          bw.close();
        
        if (fw != null)
          fw.close();
        
      } catch (IOException ex) {
        
        ex.printStackTrace();
        
      }
    }
  }
  
}

