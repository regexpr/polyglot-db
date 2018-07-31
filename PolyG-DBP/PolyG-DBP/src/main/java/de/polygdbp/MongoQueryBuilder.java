package main.java.de.polygdbp;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

/**
 *
 * @author tim
 */
public class MongoQueryBuilder {
  
  
  private int[] globalBracketCounter;
  private int counter;
  private String query;
  private String inner;
  private String[] tokens;
  private String[] firstPart;
  private List<Document> globalDoc;
  
  private List<String> appendCommands = Arrays.asList("$lookup", "$addFields", "$project");
  
  /**
   *
   * @param mongoQuery
   */
  public MongoQueryBuilder(String mongoQuery) {
    this.globalBracketCounter = new int[]{0,0,0,0};
    this.counter = 0;
    this.query = mongoQuery;
    cutQuery();
    setTokens();
  }
  
  /**
   *
   */
  public void buildMongoQuery() {
    this.globalDoc = new ArrayList<Document>();
    while(counter<tokens.length-1) {
      this.globalDoc.add(buildDocument());
    }
  }
  
  /**
   *
   * @return
   */
  public List<Document> getMongoQuery() {
    return globalDoc;
  }
  
  /**
   *
   * @param token
   * @return
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
   * @param token
   * @return
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
   *
   * @param bracketCounter
   */
  public void updateGlobalBracketCounter(int[] bracketCounter){
    for(int i = 0; i<4; i++) globalBracketCounter[i]+=bracketCounter[i];
  }
  
  /**
   *
   * @return
   */
  public Document buildDocument() {
    Document doc = null;
    String thisItem = tokens[counter];
    String nextItem = tokens[counter+1];
    
    int[] thisBracketCount = countBrackets(thisItem);
    int[] nextBracketCount = countBrackets(nextItem);
    
    updateGlobalBracketCounter(thisBracketCount);
    
    String cleanedThisItem = cleanToken(thisItem);
    Object cleanedNextItem = cleanToken(nextItem);
    
    if(isInteger(cleanedNextItem.toString())) {
      cleanedNextItem = Integer.parseInt(cleanedNextItem.toString());
    }
    else {
      cleanedNextItem = cleanedNextItem.toString();
    }
    
    if(counter >= tokens.length) {
      return doc;
    }
    
    else if(appendCommands.contains(cleanedThisItem)) {
      counter++;
      doc = new Document(cleanedThisItem, appendedDocument());
    }
    else if(nextBracketCount[0]>0) {
      counter++;
      doc = new Document(cleanedThisItem, documentsAsList());
    }
    else if (nextBracketCount[1]>0) {
      counter++;
      doc = new Document(cleanedThisItem, buildDocument());
    }
    else if (nextBracketCount[2]>0) {
      counter+=2;
      updateGlobalBracketCounter(nextBracketCount);
      doc = new Document(cleanedThisItem, cleanedNextItem);
    }
    
    
    return doc;
  }
  
  /**
   *
   * @return
   */
  public List<Document> documentsAsList(){
    List<Document> result = new ArrayList<Document>();
    
    //as long as the number of '[' exceeds ']' add documents into list
    while(globalBracketCounter[3]<1) {
      result.add(buildDocument());
    }
    globalBracketCounter[3]--;
    return result;
  }
  
  /**
   *
   * @return
   */
  public Document appendedDocument() {
    String thisItem = tokens[counter];
    String nextItem = tokens[counter+1];
    int[] thisBracketCount = countBrackets(thisItem);
    int[] nextBracketCount = countBrackets(nextItem);
    
    updateGlobalBracketCounter(thisBracketCount);
    updateGlobalBracketCounter(nextBracketCount);
    Document tempDoc = new Document(cleanToken(thisItem), cleanToken(nextItem));
    counter+=2;
    
    while(nextBracketCount[2]<1) {
      thisItem = tokens[counter];
      nextItem = tokens[counter+1];
      String cleanedThisItem = cleanToken(thisItem);
      String cleanedNextItem = cleanToken(nextItem);
      thisBracketCount = countBrackets(thisItem);
      nextBracketCount = countBrackets(nextItem);
      
      updateGlobalBracketCounter(thisBracketCount);
      updateGlobalBracketCounter(nextBracketCount);
      
      tempDoc.append(cleanedThisItem, cleanedNextItem);
      
      nextBracketCount = countBrackets(nextItem);
      counter+=2;
    }
    
    return tempDoc;
  }
  
  /**
   *
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
   */
  public void setTokens() {
    String delims = "[+(\\:, )]+";
    tokens = inner.split(delims);
  }
  
  /**
   *
   * @param s
   * @return
   */
  public static boolean isInteger(String s) {
    return isInteger(s,10);
  }
  
  /**
   *
   * @param s
   * @param radix
   * @return
   */
  public static boolean isInteger(String s, int radix) {
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
