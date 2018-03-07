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
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Owner
 */
public class Indexer {

   
    
  //  @SuppressWarnings("empty-statement")
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws FileNotFoundException,NoSuchFileException, FileSystemException, IOException {
        //Reading Files from Document

        File documentsFolder = new File("C:\\Users\\Owner\\Desktop\\Cairo University\\Senior-1\\Advanced Programming Techniques\\Project Modules\\Sample HTML Documents");    
        while(true){
            try {
                while(documentsFolder.list().length==0);
           
                for (File file : documentsFolder.listFiles()) {
                    if(file.exists()){
                        String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                        Document doc = Jsoup.parse(content);
                        String documentText = doc.body().text();        //To Be Sent to DB
                        String documentHeader = doc.head().text();      //To Be Sent to DB
                        String documentTitle = doc.title();             //To Be Sent to DB
                        System.out.println(documentTitle);
                        System.out.println(documentHeader);
                        ArrayList<String> words = new ArrayList<>();    //To Be Sent to DB
                        String[] strList = documentText.split("\\s+");
                        for( String str : strList){
                            str = str.replaceAll("[^\\w]", "");
                            if(!(str.isEmpty())){
                                // System.out.println(str);
                                words.add(str);
                            }
                        }
                        boolean delete = file.delete();
                    }
                }
            } catch (FileSystemException ex) {
              
            }
        }
    }
}