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
  private long startTime = 0;
  private long stopTime = 0;
  private boolean running = false;
  
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
  }
  
  /**
   *
   */
  public void getElapsedSecondsString() {
    long seconds;
    if (running) {
      seconds = (System.nanoTime() - startTime);
    } else {
      seconds = (stopTime - startTime);
    }
    Main.LOG.info("elapsed Time in nanoseconds:\n"+(seconds/1000000000l));
  }

  /**
   *
   */
  public void getElapsedNanoSecondsString() {
    long nanoseconds;
    if (running) {
      nanoseconds = (System.nanoTime() - startTime);
    } else {
      nanoseconds = (stopTime - startTime);
    }
    Main.LOG.info("elapsed Time in nanoseconds:\n"+ nanoseconds);
  }
}
