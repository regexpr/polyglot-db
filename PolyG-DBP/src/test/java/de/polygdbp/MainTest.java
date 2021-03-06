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

import de.polygdbp.Main.PolyGDBP;
import de.polygdbp.Main.UnexpectedParameterException;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
public class MainTest {
  
@Rule
public ExpectedException expectedEx = ExpectedException.none();

  /**
   * Test of main method, of class PolyGDBP.
   */
  @Ignore
  @Test
  public void testMain() {
    System.out.println("main");
    String[] args = null;
    PolyGDBP instance = new PolyGDBP();
    expectedEx.expect(RuntimeException.class);
    expectedEx.expectMessage("No query");
    instance.main(args);
    
  }

  /**
   * Test of help method, of class PolyGDBP.
   */
  @Test
  public void testHelp() {
    System.out.println("help");
    PolyGDBP instance = new PolyGDBP();
    instance.list();
    
  }

  /**
   * Test of list method, of class PolyGDBP.
   */
  @Test
  public void testList() {
    System.out.println("list");
    PolyGDBP instance = new PolyGDBP();
    instance.list();
    
  }

  /**
   * Test of checkUserInput method, of class PolyGDBP.
   */
  @Test
  public void testCheckUserInput() throws UnexpectedParameterException {
    System.out.println("checkUserInput");
    String[] args = {"blub"};
    PolyGDBP instance = new PolyGDBP();
    
    expectedEx.expect(RuntimeException.class);
    expectedEx.expectMessage("No query");
    instance.checkUserInput(args);
  }

  /**
   * Test of run method, of class PolyGDBP.
   */
  @Test
  public void testRun() {
  }

  /**
   * Test of executeQuery method, of class PolyGDBP.
   */
  @Test
  public void testExecuteQuery() {
  }
  
}
