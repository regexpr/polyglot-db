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

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
/**
 * This Class enables the user to enter custom Mongodb aggregation queries in a Java framework
 * this allows the user to focus more on the designing of the queries, without having to translate it
 */
public final class MongoQueryBuilder {
  
  
  private final int[] globalBracketCounter;
  private int counter;
  private int openCloseCounter;
  private final String query;
  private String inner;
  private String[] tokens;
  private String[] firstPart;
  private List<Document> globalDoc;
  
  
  
  /**
   * The constructor will take the query and cut it into it's pieces and save them in the tokens.
   * @param mongoQuery
   */
  public MongoQueryBuilder(String mongoQuery) {
    this.globalBracketCounter = new int[]{0,0,0,0};
    this.counter = 0;
    this.openCloseCounter = 0;
    this.query = mongoQuery;
    cutQuery();
    setTokens();
  }
  
  /**
   * This will build the Document for the query.
   * as long as the counter hasn't reached the end, it will keep adding Documents
   */
  public void buildMongoQuery() {

    this.globalDoc = new ArrayList<Document>();
    while(counter<tokens.length-1) {
      this.globalDoc.add(buildDocument());
    }
  }
  
  /**
   * Getter method of globalDoc.
   * @return Document which is being built
   */
  public List<Document> getMongoQuery() {
    return globalDoc;
  }
  
  /**
   * This is used to enter the tokens into the Document as a String.
   * @param token
   * @return the token without quotation marks and brackets/parentheses
   */
  public String cleanToken(String token) {
    StringBuilder sb = new StringBuilder(token);
    int removed = 0;
    for(int i = 0; i<token.length(); i++) {
      switch (token.charAt(i)) {
        case '[':
          sb.deleteCharAt(i-removed);
          removed+=1;
          break;
        case ']':
          sb.deleteCharAt(i-removed);
          removed+=1;
          break;
        case '{':
          sb.deleteCharAt(i-removed);
          removed+=1;
          break;
        case '}':
          sb.deleteCharAt(i-removed);
          removed+=1;
          break;
        default: break;
      }
    }
    return sb.toString().replaceAll("\"", "");
  }
  
  /**
   *bracketCounter
   *1. [
   *2. {
   *3. }
   *4. ]
   *
   * @param token
   * @return the bracket count of the token
   */
  public int[] countBrackets(String token) {
    int[] bracketCounter = new int[] {0,0,0,0};
    for(int i = 0; i<token.length(); i++) {
      switch (token.charAt(i)) {
        case '[':
          bracketCounter[0]+=1;
          break;
        case '{':
          bracketCounter[1]+=1;
          break;
        case '}':
          bracketCounter[2]+=1;
          break;
        case ']':
          bracketCounter[3]+=1;
          break;
        default: break;
      }
    }
    return bracketCounter;
  }
  
  /**
   * This updates the global bracket counter, to keep the bracket counts of the tokens, which have been inserted into documents
   *
   * @param bracketCounter
   */
  public void updateGlobalBracketCounter(int[] bracketCounter){
    for(int i = 0; i<4; i++) globalBracketCounter[i]+=bracketCounter[i];
    this.openCloseCounter += bracketCounter[1];
    this.openCloseCounter -= bracketCounter[2];
  }
  
  /**
   * This is used to decide what to build
   *
   * return value used for switch cases
   * 1: [ found ==> Array as List
   * 2: { found ==> new Document
   * 3: } found ==> stop signal
   * 4: ] found ==> stop signal
   * 0: none found ==> append to the Document
   *
   * @param bracketCounter
   * @return
   */
  public int evalBracketCounter(int[] bracketCounter) {
    if(bracketCounter[0]>0) return 1;
    if(bracketCounter[1]>0) return 2;
    if(bracketCounter[3]>0) return 4;
    if(bracketCounter[2]>0) return 3;
    else return 0;
  }
  
  
  /**
   * Build the complete Document.
   * @return the complete Document
   */
  public Document buildDocument() {
    Document doc = null;
    
    // look at current and next token
    String key = tokens[counter];
    String value = tokens[counter+1];
    
    // counting the brackets
    int[] thisBracketCount = countBrackets(key);
    int[] nextBracketCount = countBrackets(value);
    
    // cleaning the tokens
    String cleanedKey = cleanToken(key);
    // the value has to be checked if it is an Integer value
    Object cleanedValue = cleanToken(value);
    
    if(isInteger(cleanedValue.toString())) {
      cleanedValue = Integer.parseInt(cleanedValue.toString());
    }
    else {
      cleanedValue = cleanedValue.toString();
    }
    
    // deciding what to build
    switch(evalBracketCounter(nextBracketCount)){
      
      // the value is a list
      case 1:{
        counter++;
        updateGlobalBracketCounter(thisBracketCount);
        doc = new Document(cleanedKey, documentsAsList());
        break;
      }
      // the value is another Document
      case 2:{
        counter++;
        updateGlobalBracketCounter(thisBracketCount);
        doc = new Document(cleanedKey, buildDocument());
        break;
      }
      // the value is just a value with the end of the Document
      case 3:{
        
        updateGlobalBracketCounter(thisBracketCount);
        updateGlobalBracketCounter(nextBracketCount);
        if(openCloseCounter > 0) {
          doc = appendedDocument();
        }
        else {
          counter+=2;
          doc = new Document(cleanedKey, cleanedValue);
        }
        break;
      }
      
      case 4:{
        counter+=2;
        updateGlobalBracketCounter(thisBracketCount);
        updateGlobalBracketCounter(nextBracketCount);
        doc = new Document(cleanedKey, cleanedValue);
        break;
      }
      // the value ist just a value without the end of a Document
      case 0:{
        updateGlobalBracketCounter(thisBracketCount);
        updateGlobalBracketCounter(nextBracketCount);
        doc =appendedDocument();
      }
      
      default:break;
    }
    return doc;
  }
  
  /**
   *
   * @return Documents as a List
   */
  public List<Document> documentsAsList(){
    List<Document> result = new ArrayList<>();
    
    //as long as the number of '[' exceeds ']' add documents into list
    while(globalBracketCounter[3]<1) {
      result.add(buildDocument());
    }
    globalBracketCounter[3]--;
    return result;
  }
  
  /**
   *
   *
   * @return an appended Document
   */
  public Document appendedDocument() {
    String key = tokens[counter];
    String value = tokens[counter+1];
    String thirdItem = tokens[counter+2];
    String fourthItem = tokens[counter+3];
    
    Object cleanedValue = cleanToken(value);
    
    if(isInteger(cleanedValue.toString())) {
      cleanedValue = Integer.parseInt(cleanedValue.toString());
    }
    else {
      cleanedValue = cleanedValue.toString();
    }
    
    int[] thisBracketCount = countBrackets(key);
    int[] nextBracketCount = countBrackets(value);
    int[] thirdBracketCount = countBrackets(thirdItem);
    int[] fourthBracketCount = countBrackets(fourthItem);
    

    Document tempDoc = new Document(cleanToken(key), cleanedValue);
    counter+=2;
    
    // checks if the second value closes the Document
    if(fourthBracketCount[1]>0) {
      counter++;
      updateGlobalBracketCounter(thirdBracketCount);
      tempDoc.append(cleanToken(thirdItem), buildDocument());
      
      // checks if it already reached the end, or it still needs to append Documents
      if(counter>=tokens.length || globalBracketCounter[3]>0 || openCloseCounter==0) {
        return tempDoc;
      }
      else {
        value = tokens[counter+1];
        nextBracketCount = countBrackets(value);
      }
      
    }
    // as long as there was no closing bracket, it will keep appending Documents
    while(nextBracketCount[2]<1) {
      key = tokens[counter];
      value = tokens[counter+1];
      String cleanedKey = cleanToken(key);
      cleanedValue = cleanToken(value);
      
      if(isInteger(cleanedValue.toString())) {
        cleanedValue = Integer.parseInt(cleanedValue.toString());
      }
      else {
        cleanedValue = cleanedValue.toString();
      }
      
      thisBracketCount = countBrackets(key);
      nextBracketCount = countBrackets(value);
      
      updateGlobalBracketCounter(thisBracketCount);
      updateGlobalBracketCounter(nextBracketCount);
      
      tempDoc.append(cleanedKey, cleanedValue);
      
      nextBracketCount = countBrackets(value);
      counter+=2;
    }
    
    return tempDoc;
  }
  
  /**
   *
   *this cuts the query into 2 parts, for example
   *1st part: db.collection.aggregate
   *2nd part: [{$match:...},{...}]
   */
  public void cutQuery() {
    String delims1 = "\\(";
    String delims2 = "\\.";
    
    String[] tempTokens = query.split(delims1);
    firstPart = tempTokens[0].split(delims2);
    inner = (String) tempTokens[1];
  }
  
  /**
   *
   *cuts the 2nd part into it's tokens for example
   *{$match
   *{name
   *Jim}}
   */
  public void setTokens() {
    String delims = "[+(\\:,)]+";
    tokens = inner.split(delims);
  }
  
  /**
   *
   * @return 1st part
   */
  public String[] getFirstPart() {
    return firstPart;
  }
  
  /**
  *
  * @return inner part
  */
  public String getInner() {
    return inner;
  }
  
  
  public String[] getTokens() {
    return tokens;
  }
  
  /**
   *
   * @param s
   * @return returns true if it is an Integer else false
   */
  public boolean isInteger(String s) {
    return isInteger(s,10);
  }
  
  /**
   *
   * @param s
   * @param radix
   * @return checks a String if it is just a number
   */
  public boolean isInteger(String s, int radix) {
    if(s.isEmpty()) return false;
    for(int i = 0; i < s.length(); i++) {
      if(i == 0 && s.charAt(i) == '-') {
        if(s.length() == 1) return false;
        else continue;
      }
      if(Character.digit(s.charAt(i),radix) < 0) return false;
    }
    return true;
  }
}