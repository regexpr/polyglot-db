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

/**
 * Klasse, die Methoden zur Performanzmessung bereithält.
 */
public class Benchmark {
  // https://stackoverflow.com/questions/8255738/is-there-a-stopwatch-in-java
  private long startTime = 0;
  private long stopTime = 0;
  private boolean running = false;
  
  
  public void start() {
    this.startTime = System.nanoTime();
    this.running = true;
  }
  
  
  public void stop() {
    this.stopTime = System.nanoTime();
    this.running = false;
  }
  
  
  //elapsed time in nanoseconds
  public long getElapsedTime() {
    long elapsed;
    if (running) {
      elapsed = (System.nanoTime() - startTime);
    } else {
      elapsed = (stopTime - startTime);
    }
    return elapsed;
  }
  
  
  //elaspsed time in seconds
  public long getElapsedTimeSecs() {
    long elapsed;
    if (running) {
      elapsed = ((System.nanoTime() - startTime) / 1000);
    } else {
      elapsed = ((stopTime - startTime) / 1000);
    }
    return elapsed;
  }
}
