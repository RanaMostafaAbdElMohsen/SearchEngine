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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 *
 * @author Apt Team
 */
public class Indexer {

  
    // This array of strings of stopwords
    public static String[] stopWords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
    
    
    /*
        This function is used to search for index of specific string within 
        another array of strings and if it does not exist return -1
    */
    public static int binarySearch(String[] a, String x) {
        int low = 0;
        int high = a.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (a[mid].compareTo(x) < 0) {
                low = mid + 1;
            } else if (a[mid].compareTo(x) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }
    
    /*
        This function returns true if a specific word is found within array 
        of stopwords[] if index=-1 return false else return true
    */
    public static boolean isStopword(String word) {
		if(binarySearch(stopWords,word)!=-1) return true;
		else return false;
}
    
    /*
        This function checks file last modified time
         if file last modified time was less than time of Sytem right now 
         by 3 seconds or more return true ; That means that file is completely written
    */
    private static boolean isCompletelyWritten(File file) {
        if(System.currentTimeMillis()-file.lastModified()>3)
            return true;
        return false;
}
    
    private static void insertNewDocument(Document doc, DBMS myDatabase) throws SQLException{
        /*
            This function used to insert new document that wasn't inserted before in database
            Creation of Arraylist for headers (1-6)
            
        */
        
        ArrayList<String> headers1 = new ArrayList<>();
        ArrayList<String> headers2 = new ArrayList<>();
        ArrayList<String> headers3 = new ArrayList<>();
        ArrayList<String> headers4 = new ArrayList<>();
        ArrayList<String> headers5 = new ArrayList<>();
        ArrayList<String> headers6 = new ArrayList<>();
        String title = doc.title();
        /*
            This function used to populate the arraylist of headers
        */
        String documentText = headersExtraction(doc,headers1,headers2,headers3,headers4,headers5,headers6);
        ArrayList<String> words = new ArrayList<>(); //Creation Arraylist of words
        String[] strList = documentText.split("\\s+"); // Splitting the body into words
        String documentTitle = doc.title();
        if (documentTitle=="")
            documentTitle=strList[0];
        int index = documentText.indexOf(documentTitle); // Get index of document title
        String documentBody = documentText.substring(index, documentText.length()-1);
        documentBody = documentBody.replaceAll("'", "''");//We can add here '' instead
        documentTitle= documentTitle.replaceAll("'", "''");//this is for replacing can't with can''t
        
  
        //Insert into table document the url, title and body
        String documentInsertQuery = "INSERT INTO Documents (URL,Title,Body) VALUES ('" + strList[0] + "', '" + documentTitle + "', '" + documentBody + "');";
        myDatabase.runSqlQuery(documentInsertQuery);

        databaseInserter(-1,strList,myDatabase,headers1,headers2,headers3,headers4,headers5,headers6,title);
    } //  Inserting new document
    
    private static void updateOldDocument(Document doc, DBMS myDatabase, int docID) throws SQLException{
        ArrayList<String> headers1 = new ArrayList<>();
        ArrayList<String> headers2 = new ArrayList<>();
        ArrayList<String> headers3 = new ArrayList<>();
        ArrayList<String> headers4 = new ArrayList<>();
        ArrayList<String> headers5 = new ArrayList<>();
        ArrayList<String> headers6 = new ArrayList<>();
        String title = doc.title();
        
        String documentText = headersExtraction(doc,headers1,headers2,headers3,headers4,headers5,headers6);

        String documentTitle = doc.title();
        int index = documentText.indexOf(documentTitle);
        String documentBody = documentText.substring(index, documentText.length()-1);
        documentBody = documentBody.replaceAll("'", "''");//We can add here '' instead
        documentTitle= documentTitle.replaceAll("'", "''");
        
        String[] strList = documentText.split("\\s+");
        
        //This query needed to select words if they exist to update them and decrease their count
        String SelectWordCountQuery ="Select WordID from Relations "+
                                     "where DocID = "+docID+";";
        ResultSet rs = myDatabase.runSql(SelectWordCountQuery);
        while(rs.next())//If there are words in this document exists
        {
            /*
                Decrement all count by 1
            */
            String updateQueryCount="Update Words SET Count=Count-1 where WID= "+rs.getInt("WordID");
            myDatabase.runSqlQuery(updateQueryCount);
        }
        //Delete from Relations all words that has relation with this document
        String clearRelationsQuery = "DELETE FROM Relations WHERE DocID = "+docID+";";
        myDatabase.runSqlQuery(clearRelationsQuery);
        // If an words in the table Words was of count 0 that he deletes it
        String clearWordsQuery = "DELETE FROM Words WHERE Count = 0;";
        myDatabase.runSqlQuery(clearWordsQuery);
        // Update the body as the text inside the body has changed
        String updateDocumentQuery = "UPDATE Documents SET Body='" + documentBody + "' where DID = "+ docID + ";";
        myDatabase.runSqlQuery(updateDocumentQuery);
        //Insert in database the words
        databaseInserter(docID,strList,myDatabase,headers1,headers2,headers3,headers4,headers5,headers6,title);
    } //  Updatig an old document
    
    /*
       This function returns true or false to check if a specific word in
       a specific header and if it contains that word, I will replace it with empty string
       this helps me to calculate rank of each word
    */
    private static boolean isWithin(ArrayList<String> headersList, String word){
        for(int i=0;i<headersList.size();i++){
            if(headersList.get(i).contains(word)){
                headersList.add(i, headersList.get(i).replaceFirst(word, ""));
                return true;
            }
        }
        return false;
    }
    /*
        This function calculates the total summation of the Rank of a specific word
        Returns rank as a float
    */
    private static float getWordRank(String str, ArrayList<String> words, ArrayList<Integer> ranks){
        float rank = 0;
        for(int i=0;i<words.size();i++){
            if(words.get(i).equals(str))
                rank += ranks.get(i);
        }
        return rank;
    }
    
    private static void databaseInserter(int docID, String[] strList, DBMS myDatabase, ArrayList<String> headers1,ArrayList<String> headers2,ArrayList<String> headers3,ArrayList<String> headers4,ArrayList<String> headers5, ArrayList<String> headers6, String title) throws SQLException{
        /*
            docID is sent with updateDocument as a document actually exists
            docID in insertDocument is hardcoded with -1 so in this case we need
            to run this query to select documentID
        */
        if(docID == -1) { 
             ResultSet rs = myDatabase.runSql("Select DID from Documents where URL='" + strList[0] + "';");
             docID = rs.getInt("DID");
         } 
         ArrayList<String> words = new ArrayList<>();
         ArrayList<Integer> ranks = new ArrayList<>();
         System.out.println("Extracting Words..");

         /*
            Looping on array of words and if word is not empty
            and is not stop word
            Converting it to lower case as words in database in lower case
            and checking what headers contain this word and incrementing its rank 
            accordingly
            Ranks        Score
            title          8
            h1             7
            h2             6
            h3             5
            h4             4
            h5             3
            h6             2
            anything else  1
         */
         
         for( int i=1 ;i<strList.length;i++){
             strList[i] = strList[i].replaceAll("[^\\w]", "");
             if(!(strList[i].isEmpty()) && !isStopword(strList[i])){
                 words.add(strList[i].toLowerCase());
                 
                 if(title.contains(strList[i])){
                     ranks.add(8);
                     title = title.replaceFirst(strList[i], "");
                 }
                 else if(isWithin(headers1,strList[i]))
                     ranks.add(7);
                 else if(isWithin(headers2,strList[i]))
                     ranks.add(6);
                 else if(isWithin(headers3,strList[i]))
                     ranks.add(5);
                 else if(isWithin(headers4,strList[i]))
                     ranks.add(4);
                 else if(isWithin(headers5,strList[i]))
                     ranks.add(3);
                 else if(isWithin(headers6,strList[i]))
                     ranks.add(2);
                 else
                     ranks.add(1);
             }
         }
         /*
            Creating set of unique words
         */
         Set<String> unique = new HashSet<>(words);
         ArrayList<ArrayList<Integer>> positions = new ArrayList<ArrayList<Integer>>();//Creating ArrayList of ArrayList of positions
         int positionsIterator = 0;
         /*
            Intiliasing that each element within position array 
            has its own list of ArrayList
         */
         for(int i=0; i<unique.size(); i++){
             positions.add(positionsIterator++, new ArrayList<Integer>());
         }

         /*
            If we found a word in array of words equals a unique word in hashset
            we add its position
         */
         
        positionsIterator = 0;
       for(String key : unique){
             for(int i=0; i<words.size(); i++) {
                 if(words.get(i).equals(key))
                     (positions.get(positionsIterator)).add(i); 
             }
             positionsIterator++;
       }
         System.out.println("Inserting Words...");
         int uniqueIterator = 0;
         
         
         for (String key : unique) {
             int wordID = -1;
             /*
              Transforming position array into string to be ready for insertion in database
              and removing space and [ and ]
             */
             String Pos = Arrays.toString((positions.get(uniqueIterator++)).toArray());
             Pos = Pos.replaceAll("\\[", "");
             Pos = Pos.replaceAll("\\]", "");
             Pos = Pos.replaceAll("\\s+", "");
             /*
                Stemming the word
             */
             Stemmer s = new Stemmer();
             for(int i=0; i<key.length(); i++) s.add(key.charAt(i));
             s.stem();
             /*
                Calculating average rank of the word
             */
             
             float wordRank = getWordRank(key,words,ranks);
             wordRank/=(float)Collections.frequency(words, key);
             
             /*
                Insert word in Table words and Relations with their values
                and also Updating count of words by 1 if exists
             */ 
             String wordSelectQuery =   "Select * from Words WHERE Word='"+key+"';";
             
             
             String wordInsertQuery =   "Insert into Words (Word, WordStemmed, Count) VALUES ('"+key+"','"+s.toString()+"',1);";
        
             ResultSet result = myDatabase.runSql(wordSelectQuery);
             if(result.next()){
                 wordID = result.getInt("WID");
                 String wordUpdateQuery =   "UPDATE Words SET Count = Count + 1 where WID="+wordID+";";
                 myDatabase.runSqlQuery(wordUpdateQuery);
             }
             else {
                 myDatabase.runSqlQuery(wordInsertQuery);
                 wordID = myDatabase.runSql(wordSelectQuery).getInt("WID");
             }
             result.close();
             
             String tfInsertQuery =     "Insert into Relations (DocID,WordID,Positions,TF, Rank) "+
                                        "VALUES ("+docID+","+wordID+",'"+Pos+"',"+(float)((float)Collections.frequency(words, key)/(float)words.size())+", " 
                                        + wordRank + ");";
             //myDatabase.runSqlQuery(idfRealUpdateQuery);
             myDatabase.runSqlQuery(tfInsertQuery);
         }
         
    }
    /*
        This function parse the body text with proper space indentation between each tag and next one
        This function Extract headers h1,h2,h3,h4,h5,h6 and other than that
        This function inserts all headers in corresponding headers
        h1 in arraylist of headers 1 and so on
        
    
    */
    private static String headersExtraction(Document doc, ArrayList<String> headers1,ArrayList<String> headers2,ArrayList<String> headers3,ArrayList<String> headers4,ArrayList<String> headers5, ArrayList<String> headers6){
        Elements tags = doc.select("*");
        String docText = "";
        for (Element tag : tags) {
            for (TextNode tn : tag.textNodes()) {
                String tagText = tn.text().trim();
                if (tagText.length() > 0) {
                    docText += tagText + " ";
                }

                switch(tag.tagName()){
                     case "h1":
                         headers1.add(tagText);
                         break;
                     case "h2":
                         headers2.add(tagText);
                         break;
                     case "h3":
                         headers3.add(tagText);
                         break;
                     case "h4":
                         headers4.add(tagText);
                         break;
                     case "h5":
                         headers5.add(tagText);
                         break;
                     case "h6":
                         headers6.add(tagText);
                         break;
                     default:
                         break;
                }    
            }
        }
        
        return docText;
    }
    
    public static void main(String[] args) throws FileNotFoundException,NoSuchFileException, FileSystemException, IOException, InterruptedException, SQLException {
        //Reading Files from Document
        DBMS myDatabase = new DBMS();  // Creating new database Indexer
        int count = 0;// Initiliasing the count 0
        File documentsFolder = new File("C:\\Users\\Owner\\Desktop\\Cairo University\\Senior-1\\Advanced Programming Techniques\\Project Modules\\Sample HTML Documents");   // Sample of html documents exists 
        while(true){
            
           
            try {
                while(documentsFolder.list().length==0); // Wait till we have files to index
                for (File file : documentsFolder.listFiles()) { // loop on files
                    if(file.exists() && isCompletelyWritten(file)){ // check if file exists and completely written
                       String startTransactionQuery = "Begin Transaction;"; // Begin Transaction  
                        myDatabase.runSqlQuery(startTransactionQuery); 
                       String content = new String(Files.readAllBytes(Paths.get(file.getPath())));// read bytes from string content
                       Document doc = Jsoup.parse(content);
                       String documentExistQuery = "Select * from Documents where URL='"+((doc.body().text()).split("\\s+"))[0]+"';";//query to check if document exist
                       ResultSet result = myDatabase.runSql(documentExistQuery);
                       if(result.next()){// check if there is document and so updating
                           int docID = result.getInt("DID");
                           System.out.println("Updating Document");
                           updateOldDocument(doc, myDatabase,docID);
                       }
                       else {
                           System.out.println("Inserting Document");//Inserting document
                           insertNewDocument(doc, myDatabase);
                       }
                       result.close(); // result.close()
                       String endTransactionQuery = "End Transaction;";
                        myDatabase.runSqlQuery(endTransactionQuery);
                       boolean delete = file.delete(); // delete files
                       if(delete)System.out.println("Document Finished and Deleted!");
                       else System.out.println("Error deleting files!");
                       System.out.println("Documents Indexed: "+ count++);
                       
                       if(count% 20==0) { //that we update idf every 20 documents or its multiples
                           String idfUpdateQuery =  "UPDATE Words SET IDF = "+
                                                    "CAST(LOG10((CAST((Select Count(URL) from Documents) AS float))/(CAST(Count AS float))) AS FLOAT);";
                           myDatabase.runSqlQuery(idfUpdateQuery);
                       }
                    }
                }//Update IDF Query if there are no documents
                String idfUpdateQuery = "UPDATE Words SET IDF = "+
                                        "CAST(LOG10((CAST((Select Count(URL) from Documents) AS float))/(CAST(Count AS float))) AS FLOAT);";
                myDatabase.runSqlQuery(idfUpdateQuery);
                
               
            }
            catch (IOException | SQLException ex) {// Exception catched
                System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            }
        }
    }
}