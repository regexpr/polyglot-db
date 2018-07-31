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
 * Loads a dataset into the MongoDB.
 *
 */
public class MongoImporter {
  private final int lines;
  private final String pathDataset;
  private MongoAPI mongoApi;
  
  /**
   *
   * @param mongoApi
   * @param lines
   * @param path
   */
  public MongoImporter(MongoAPI mongoApi, int lines, String path) {
    this.lines = lines;
    this.mongoApi = mongoApi;
    this.pathDataset = path;
  }
  
  /**
   *
   */
  public void importData(){
    File folder = new File(pathDataset);
    List<File> files = new ArrayList<File>(Arrays.asList(folder.listFiles()));
    while(files.iterator().hasNext()){
      File file = files.iterator().next();
      FileInputStream fis;
      String collectionName = FilenameUtils.getBaseName(file.getName());
      mongoApi.setCurrentMongoCollection(collectionName);
      List<Document> documents = new ArrayList<Document>();
      String tempLine = null;
      int i = 0;
      try {
        fis = new FileInputStream(file);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        while ((tempLine = in.readLine()) != null) {
          if ((lines > -1) && (i >= lines)){
            break;
          }
          documents.add(Document.parse(tempLine));
        }
        mongoApi.getCurrentMongoCollection().insertMany(documents);
        // close stream
        in.close();
        fis.close();
      } catch (FileNotFoundException ex) {
        LOG.error("Dataset File not found" + ex.getMessage() + ex.getCause());
      } catch (IOException ex) {
        LOG.error("IOException: " + ex.getMessage() + ex.getCause());
      }
    }
  }
}

