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
 * Contains all hard-coded quering examples for the MongoDB to make Quering easier. 
 * They refer to the <code>>Yelp Dataset</code>.
 */
public class MongoExamples {
  // Output all business names and ids a <specific user> rated with minumum of <stars>
  private final String q1 = "db.review.aggregate([{$match : {$and: [{\"user_id\":\"nOTl4aPC4tKHK35T3bNauQ\"},{\"stars\": {$gt: 4}}]}}, {$lookup: {from:\"business\", localField: \"business_id\", foreignField: \"business_id\", as: \"business\"}}, {$addFields: {\"business\":\"$business\"}}, {$project:{\"business.name\":1, \"business.business_id\":1}}])"; 
  
  // Output the average stars of all businesses
  private final String q2 = "db.business.aggregate({$group:{_id: null,\"average_stars\":{$avg: \"$stars\"}}})";
  
  // Output the average stars of all businesses that grouped by category
  private final String q3 = "db.business.aggregate({$unwind:\"$categories\"},{$group:{_id: {categories:\"$categories\"},\"average_stars\":{ $avg: \"$stars\" }}})";
  
  // Output all businesses that are in the category Cannabis Tours and return the average of all stars grouped by all the categories that they are in
  private final String q4 = "db.business.aggregate({$match:{categories:\"Cannabis Tours\"}},{$unwind:\"$categories\"},{$group:{_id: {categories:\"$categories\"},\"average_stars\":{ $avg: \"$stars\" }}})";
  
  /**
   * Get the Query by the related shortcut.
   * @param q Shortcut for the Query. Valid Example: "q1".
   * @return Query result in String.
   */
  public String getQuery(String q) {
    switch(q) {
    case "q1": return q1;
    case "q2": return q2;
    case "q3": return q3;
    case "q4": return q4;
    }
    return q;
  }
}
