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
 * Klasse, die Methoden zur Performanzmessung bereithält.
 */
public class Benchmark {
  private long startTime;
  private long stopTime;
  private long duration;
  private boolean running;
  private final String processName;

  public Benchmark(String processName) {
    this.startTime = 0;
    this.stopTime = 0;
    this.duration = 0;
    this.running = false;
    this.processName = processName;
  }
  
  /**
   *
   */
  public void start() {
    this.startTime = System.nanoTime();
    this.running = true;
  }
  
  /**
   *
   */
  public void stop() {
    this.stopTime = System.nanoTime();
    this.running = false;
    this.duration = stopTime-startTime;
  }

  public long getDuration() {
    return duration;
  }

  public String getProcessName() {
    return processName;
  }
  
  
  /**
   *
   */
  public void writeDurationToLOG(char accuracy) {
    if (running) {
      stop();
    }
    LOG.log(BenchmarkResult,"Elapsed time for the process "+processName+" :");
    switch (accuracy) {
      case 's':
        LOG.log(BenchmarkResult,(duration/1000000000) + " seconds");
        break;
      case 'm':
        LOG.log(BenchmarkResult,(duration/1000000) + " milliseconds");
        
        break;
      case 'n': default:
        LOG.log(BenchmarkResult,(duration) + " nanoseconds");
        break;
    }
    
  }
}
