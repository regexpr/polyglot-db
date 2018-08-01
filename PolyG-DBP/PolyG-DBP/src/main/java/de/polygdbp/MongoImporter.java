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

import static de.polygdbp.Main.LOG;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;

/**
 * Loads a dataset into the MongoDB. The dataset path must be given. 
 * It can consist of several .JSON documents.
 * Optional: Import just a specific number of lines of the given .JSON files.
 * For that the user must ensure that every line of the .JSON is a complete JSON object.
 */
public class MongoImporter {
  private final int lines;
  private final String pathDataset;
  private final MongoAPI mongoApi;
  
  /**
   * Constructor.
   * @param mongoApi. Initialized de.polygdbp.MongoAPI.
   * @param lines. Number of lines that will be imported from every .JSON document.
   * @param path. Path to the directory with .JSON dataset.
   */
  public MongoImporter(MongoAPI mongoApi, int lines, String path) {
    this.lines = lines;
    this.mongoApi = mongoApi;
    this.pathDataset = path;
  }
  
  /**
   * Import .JSON objects into the running MongoDB.
   * For every file in the dataset directory of the given path,
   * transform every line (until file end or user's number of maximum lines) 
   * into a Document and finally add it into the related Collection.
   */
  public void importData(){
    // get all files from the given path
    File folder = new File(pathDataset);
    List<File> files = new ArrayList<>(Arrays.asList(folder.listFiles()));
    // iterate all found files
    //while(files.iterator().hasNext()){
    for (int k=0;k<files.size();k++) {
      File file = files.get(k);
      LOG.debug(file.toString());
      FileInputStream fis;
      String collectionName = FilenameUtils.getBaseName(file.getName());
      // ensure that the right MongoDB collection is selected
      mongoApi.setCurrentMongoCollection(collectionName);
      List<Document> documents = new ArrayList<>();
      String tempLine;
      int i = 0;
      try {
        fis = new FileInputStream(file);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        // Iterate all lines and use them as Strings 
        // to pass them over to the current MongoDB document collection
        while ((tempLine = in.readLine()) != null) {
          if ((lines > -1) && (i >= lines)){
            break;
          }
          documents.add(Document.parse(tempLine));
        }
        LOG.debug("Start importing Collection"+collectionName);
        mongoApi.getCurrentMongoCollection().insertMany(documents);
        LOG.debug("Finished importing Collection"+collectionName);
        // Close streams
        in.close();
        fis.close();
        // Catch important exceptions
      } catch (FileNotFoundException ex) {
        LOG.error("Dataset File not found" + ex.getMessage() + ex.getCause());
        System.exit(-1);
      } catch (IOException ex) {
        LOG.error("IOException: " + ex.getMessage() + ex.getCause());
        System.exit(-1);
      }
      // end while
    }
  }
}

