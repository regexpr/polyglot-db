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
 * Produces preciser exceptions for the de.polygdbp.Main when facing unexpected arguments of
 * the user.
 */
public class UnexpectedParameterException extends RuntimeException {
  
  /**
   * Creates a new instance of <code>UnexpectedParameterException</code> without
   * detail message.
   */
  public UnexpectedParameterException() {
  }
  
  /**
   * Constructs an instance of <code>UnexpectedParameterException</code> with
   * the specified detail message.
   *
   * @param msg the detail message.
   */
  public UnexpectedParameterException(String msg) {
    super(msg);
  }
}
