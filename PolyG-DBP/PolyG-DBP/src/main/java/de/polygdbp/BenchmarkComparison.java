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

import static de.polygdbp.Main.BenchmarkResult;
import static de.polygdbp.Main.LOG;

/**
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
public class BenchmarkComparison {
  private final Benchmark benchmark1;
  private final Benchmark benchmark2;

  public BenchmarkComparison(Benchmark benchmark1, Benchmark benchmark2) {
    this.benchmark1 = benchmark1;
    this.benchmark2 = benchmark2;
  }
  
  public void writeDurationComparisonToLOG(){
    long delta1 = benchmark1.getDuration();
    long delta2 = benchmark2.getDuration();
    String processName1 = benchmark1.getProcessName();
    String processName2 = benchmark2.getProcessName();
    
    if (delta1 < delta2) {
      // swap
      long temp = delta1;
      delta1 = delta2;
      delta2 = temp;
    }
      int percentage = (int) (delta1/delta2)*100;
      LOG.log(BenchmarkResult,"Process "+processName1+" with "+ delta1 + " ns took " + (delta1-delta2) + " ns longer ("+ percentage+ "%)");
      LOG.log(BenchmarkResult,"than process" + processName2 + "with " + delta2 + " ns.");
    
  }
}
