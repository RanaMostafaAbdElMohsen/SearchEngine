/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

import java.io.File;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Apt Team
 */
public class Indexer {

    private static boolean isCompletelyWritten(File file) {
        if(System.currentTimeMillis()-file.lastModified()>3)
            return true;
        return false;
}
    private static void insertNewDocument(Document doc, DBMS myDatabase) throws SQLException{
        String documentText = doc.body().text();
        documentText = documentText.replaceAll("'", " ");
        String documentTitle = doc.title();
        int index = documentText.indexOf(documentTitle);
        String documentBody = documentText.substring(index, documentText.length()-1);
        //System.out.println(documentBody);


        ArrayList<String> words = new ArrayList<>();
        String[] strList = documentText.split("\\s+");


        String documentInsertQuery = "INSERT INTO dbo.Documents (URL,Title,Body) VALUES ('" + strList[0] + "', '" + documentTitle + "', '" + documentBody + "');";
        myDatabase.runSqlQuery(documentInsertQuery);




         for( int i=1 ;i<strList.length;i++){
             strList[i] = strList[i].replaceAll("[^\\w]", "");
             if(!(strList[i].isEmpty())){
                 words.add(strList[i].toLowerCase());
             }
         }

         Set<String> unique = new HashSet<String>(words);

         for (String key : unique) {
             //System.out.println(key + ": " + Collections.frequency(words, key));
             String wordSelectQuery = "Select * from dbo.Words WHERE Word='"+key+"';";
             String wordInsertQuery = "Insert into dbo.Words (Word,Count) VALUES ('"+key+"',1);";
             String wordUpdateQuery = "DECLARE @IncrementValue int; \n" +
                                      "SET @IncrementValue = 1; \n" +
                                      "UPDATE dbo.Words SET Count = Count + @IncrementValue where Word='"+key+"';";
             String idfUpdateQuery = "UPDATE dbo.Words SET IDF ="+
                                     " (CAST((Select Count(URL) from dbo.Documents) AS float))/(CAST(Count AS float));";
             String tfInsertQuery =  "Insert into dbo.Relations (DocID,WordID,TF)"+
                                     " VALUES ((Select ID from dbo.Documents where URL='" +
                                     strList[0] + "'),(Select ID from dbo.Words where Word='"+key+
                                     "'),"+Collections.frequency(words, key)+");";
             ResultSet result = myDatabase.runSql(wordSelectQuery);
             if(result.next()){
                 myDatabase.runSqlQuery(wordUpdateQuery);
             }
             else {
                 myDatabase.runSqlQuery(wordInsertQuery);
             }
             myDatabase.runSqlQuery(idfUpdateQuery);
             myDatabase.runSqlQuery(tfInsertQuery);
         }
    } //  Inserting new document
    private static void updateOldDocument(Document doc, DBMS myDatabase) throws SQLException{
        String documentText = doc.body().text();
        documentText = documentText.replaceAll("'", " ");
        int index = documentText.indexOf(doc.title());
        String documentBody = documentText.substring(index, documentText.length()-1);
        ArrayList<String> words = new ArrayList<>();
        String[] strList = documentText.split("\\s+");
        String decrementWordCountQuery =     "DECLARE @DecrementValue int; "+
                                        "SET @DecrementValue = 1; "+
                                        "UPDATE dbo.Words SET Count = Count - @DecrementValue "+
                                        "from dbo.Documents,dbo.Relations "+
                                        "where dbo.Words.ID = WordID and " +
                                        "DocID = dbo.Documents.ID "+
                                        "and URL='"+strList[0]+"'; ";
        myDatabase.runSqlQuery(decrementWordCountQuery);
        String clearRelationsQuery = "DELETE FROM dbo.Relations WHERE DocID = (Select ID from dbo.Documents where URL='"+strList[0]+"');";
        myDatabase.runSqlQuery(clearRelationsQuery);
        String clearWordsQuery = "DELETE FROM dbo.Words WHERE Count = 0;";
        myDatabase.runSqlQuery(clearWordsQuery);
        String updateDocumentQuery = "UPDATE dbo.Documents SET Body='" + documentBody + "' where URL = '"+ strList[0] + "';";
        myDatabase.runSqlQuery(updateDocumentQuery);
        
         for( int i=1 ;i<strList.length;i++){
             strList[i] = strList[i].replaceAll("[^\\w]", "");
             if(!(strList[i].isEmpty())){
                 words.add(strList[i].toLowerCase());
             }
         }

         Set<String> unique = new HashSet<String>(words);

         for (String key : unique) {
             //System.out.println(key + ": " + Collections.frequency(words, key));
             String wordSelectQuery = "Select * from dbo.Words WHERE Word='"+key+"';";
             String wordInsertQuery = "Insert into dbo.Words (Word,Count) VALUES ('"+key+"',1);";
             String wordUpdateQuery = "DECLARE @IncrementValue int; \n" +
                                      "SET @IncrementValue = 1; \n" +
                                      "UPDATE dbo.Words SET Count = Count + @IncrementValue where Word='"+key+"';";
             String idfUpdateQuery = "UPDATE dbo.Words SET IDF ="+
                                     " (CAST((Select Count(URL) from dbo.Documents) AS float))/(CAST(Count AS float));";
             String tfInsertQuery =  "Insert into dbo.Relations (DocID,WordID,TF)"+
                                     " VALUES ((Select ID from dbo.Documents where URL='" +
                                     strList[0] + "'),(Select ID from dbo.Words where Word='"+key+
                                     "'),"+Collections.frequency(words, key)+");";
             ResultSet result = myDatabase.runSql(wordSelectQuery);
             if(result.next()){
                 myDatabase.runSqlQuery(wordUpdateQuery);
             }
             else {
                 myDatabase.runSqlQuery(wordInsertQuery);
             }
             myDatabase.runSqlQuery(idfUpdateQuery);
             myDatabase.runSqlQuery(tfInsertQuery);
         }
    } //  Updatig an old document
  //  @SuppressWarnings("empty-statement")
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws FileNotFoundException,NoSuchFileException, FileSystemException, IOException, InterruptedException, SQLException {
        //Reading Files from Document
        DBMS myDatabase = new DBMS();
        
        File documentsFolder = new File("C:\\Users\\Owner\\Desktop\\Cairo University\\Senior-1\\Advanced Programming Techniques\\Project Modules\\Sample HTML Documents");    
        while(true){
            try {
                while(documentsFolder.list().length==0);
                for (File file : documentsFolder.listFiles()) {
                    if(file.exists() && isCompletelyWritten(file)){
                       String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                       Document doc = Jsoup.parse(content);
                       String documentExistQuery = "Select * from dbo.Documents where URL='"+((doc.body().text()).split("\\s+"))[0]+"';";
                       ResultSet result = myDatabase.runSql(documentExistQuery);
                       if(result.next()){
                           updateOldDocument(doc,myDatabase);
                       }
                       else {
                           insertNewDocument(doc,myDatabase);
                       }
                       boolean delete = file.delete();
                       if(delete)System.out.println("Document Finished and Deleted!");
                       else System.out.println("Error deleting files!");
                    }
                }
            }
            catch (FileSystemException ex) {
              
            }
        }
    }
}