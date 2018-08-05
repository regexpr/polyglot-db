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

/**
 * Supports time measuring for one certain process.
 */
public class Benchmark {
  private long startTime;
  private long stopTime;
  private long duration;
  private boolean running;
  private final String processName;
  
  /**
   * Constructor.
   * @param processName will be shown in the LOG.
   */
  public Benchmark(String processName) {
    this.startTime = 0;
    this.stopTime = 0;
    this.duration = 0;
    this.running = false;
    this.processName = processName;
  }
  
  /**
   * Starts measuring time.
   */
  public void start() {
    this.startTime = System.nanoTime();
    this.running = true;
  }
  
  /**
   * Calculates the duration by determining the stopTime.
   */
  public void stop() {
    this.stopTime = System.nanoTime();
    this.running = false;
    this.duration = stopTime-startTime;
  }
  
  /**
   * Getter method of the duration.
   * @return class variable long duration
   */
  public long getDuration() {
    return duration;
  }
  
  /**
   * Getter method of the process name.
   * @return class variable string process name.
   */
  public String getProcessName() {
    return processName;
  }
  
  
  /**
   * Writes the Duration into the log.
   * @param accuracy to select the preffered time accuration:
   * 's' - seconds
   * 'm' - milliseconds
   * 'n' - nanoseconds
   */
  public void writeDurationToLOG(char accuracy) {
    if (running) {
      // to determine the stopTime and so the duration.
      stop();
    }
    LOG.log(BENCHMARK,"Elapsed time for the process "+processName+" :");
    switch (accuracy) {
      case 's':
        LOG.log(BENCHMARK,(duration/1000000000) + " seconds");
        break;
      case 'm':
        LOG.log(BENCHMARK,(duration/1000000) + " milliseconds");
        break;
      case 'n': default:
        LOG.log(BENCHMARK,(duration) + " nanoseconds");
        break;
    }
  }
  
  /**
   * Overloaded method. See writeDurationToLog(char accuracy) for more information.
   * Default accuracy: nanoseconds.
   */
  public void writeDurationToLOG(){
    writeDurationToLOG('n');
  }
}
