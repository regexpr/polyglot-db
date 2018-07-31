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
 *
 * @author Tim Niehoff, Hyeon Ung Kim
 */
public class MongoExamples {
  private String q1 = "db.review.aggregate([{$match : {$and: [{\"user_id\":\"nOTl4aPC4tKHK35T3bNauQ\"},{\"stars\": {$gt: 4}}]}}, {$lookup: {from:\"business\", localField: \"business_id\", foreignField: \"business_id\", as: \"business\"}}, {$addFields: {\"business\":\"$business\"}}, {$project:{\"business.name\":1, \"business.business_id\":1}}])"; 
  
  
  
  public String getQuery(String q) {
    switch(q) {
    case "q1": return q1;
    }
    return q;
  }
}
