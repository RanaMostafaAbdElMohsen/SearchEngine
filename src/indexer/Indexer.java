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

    public static String[] stopWords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
    
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
    
    public static boolean isStopword(String word) {
		if(binarySearch(stopWords,word)!=-1) return true;
		else return false;
}
    
    private static boolean isCompletelyWritten(File file) {
        if(System.currentTimeMillis()-file.lastModified()>3)
            return true;
        return false;
}
    
    private static void insertNewDocument(Document doc, DBMS myDatabase) throws SQLException{
        String documentText = doc.body().text();
        documentText = documentText.replaceAll("'", " ");//We can add here '' instead
        String documentTitle = doc.title();
        int index = documentText.indexOf(documentTitle);
        String documentBody = documentText.substring(index, documentText.length()-1);
        


        ArrayList<String> words = new ArrayList<>();
        String[] strList = documentText.split("\\s+");


        String documentInsertQuery = "INSERT INTO Documents (URL,Title,Body) VALUES ('" + strList[0] + "', '" + documentTitle + "', '" + documentBody + "');";
        myDatabase.runSqlQuery(documentInsertQuery);


        System.out.println("Extracting Words..");

         for( int i=1 ;i<strList.length;i++){
             strList[i] = strList[i].replaceAll("[^\\w]", "");
             if(!(strList[i].isEmpty()) && !isStopword(strList[i])){
                 words.add(strList[i].toLowerCase());
             }
         }

         Set<String> unique = new HashSet<String>(words);
         int wordCount = unique.size();
         System.out.println("Inserting Words..");
         for (String key : unique) {
             //System.out.println(key + ": " + Collections.frequency(words, key));
             String wordSelectQuery =   "Select * from Words WHERE Word='"+key+"';";
             
             
             String wordInsertQuery =   "Insert into Words (Word,Count) VALUES ('"+key+"',1);";
             
             
             String wordUpdateQuery =   "UPDATE Words SET Count = Count + 1 where Word='"+key+"';";
             
             String idfRealUpdateQuery ="UPDATE Words SET IDFreal = "+
                                        "CAST(LOG10((CAST((Select Count(URL) from Documents) AS float))/(CAST(Count AS float))) AS FLOAT);";
             
             String idfStemmedUpdateQuery = "UPDATE Words SET IDFstemmed = "+
                                            "CAST(0.2*IDFreal AS FLOAT);";
             
             String tfInsertQuery =     "Insert into Relations (DocID,WordID,TF) "+
                                        "VALUES ((Select docid from Documents where URL='" +
                                        strList[0] + "'),(Select docid from Words where Word='"+key+
                                        "'),"+(float)((float)Collections.frequency(words, key)/(float)words.size())+");";
             System.out.println((float)((float)Collections.frequency(words, key)/(float)words.size()));
             System.out.println("Word inserted..Count Left = "+ --wordCount);
             ResultSet result = myDatabase.runSql(wordSelectQuery);
             if(result.next()){
                 myDatabase.runSqlQuery(wordUpdateQuery);
             }
             else {
                 myDatabase.runSqlQuery(wordInsertQuery);
             }
             result.close();
             myDatabase.runSqlQuery(idfRealUpdateQuery);
             myDatabase.runSqlQuery(idfStemmedUpdateQuery);
             myDatabase.runSqlQuery(tfInsertQuery);
         }
    } //  Inserting new document
    
    private static void updateOldDocument(Document doc, DBMS myDatabase) throws SQLException{
        String documentText = doc.body().text();
        documentText = documentText.replaceAll("'", " ");// We can add here '' instead
        int index = documentText.indexOf(doc.title());
        String documentBody = documentText.substring(index, documentText.length()-1);
        ArrayList<String> words = new ArrayList<>();
        String[] strList = documentText.split("\\s+");
        String decrementWordCountQuery = "UPDATE Words SET Count = Count - 1 "+
                                        "from Documents,Relations "+
                                        "where Words.docid = WordID and " +
                                        "DocID = Documents.docid "+
                                        "and URL='"+strList[0]+"'; ";
        myDatabase.runSqlQuery(decrementWordCountQuery);
        String clearRelationsQuery = "DELETE FROM Relations WHERE DocID = (Select docid from Documents where URL='"+strList[0]+"');";
        myDatabase.runSqlQuery(clearRelationsQuery);
        String clearWordsQuery = "DELETE FROM Words WHERE Count = 0;";
        myDatabase.runSqlQuery(clearWordsQuery);
        String updateDocumentQuery = "UPDATE Documents SET Body='" + documentBody + "' where URL = '"+ strList[0] + "';";
        myDatabase.runSqlQuery(updateDocumentQuery);
        
         for( int i=1 ;i<strList.length;i++){
             strList[i] = strList[i].replaceAll("[^\\w]", "");
             if(!(strList[i].isEmpty()) && !isStopword(strList[i])){
                 words.add(strList[i].toLowerCase());
             }
         }

         Set<String> unique = new HashSet<String>(words);

         for (String key : unique) {
             //System.out.println(key + ": " + Collections.frequency(words, key));
             String wordSelectQuery =   "Select * from Words WHERE Word='"+key+"';";
             
             
             String wordInsertQuery =   "Insert into Words (Word,Count) VALUES ('"+key+"',1);";
             
             
             String wordUpdateQuery =   "UPDATE Words SET Count = Count + 1 where Word='"+key+"';";
             
             String idfRealUpdateQuery ="UPDATE Words SET IDFreal = "+
                                        "CAST(LOG10((CAST((Select Count(URL) from Documents) AS float))/(CAST(Count AS float))) AS FLOAT);";
             
             String idfStemmedUpdateQuery = "UPDATE Words SET IDFstemmed = "+
                                            "CAST(0.2*IDFreal AS FLOAT);";
             
             String tfInsertQuery =     "Insert into Relations (DocID,WordID,TF) "+
                                        "VALUES ((Select docid from Documents where URL='" +
                                        strList[0] + "'),(Select docid from Words where Word='"+key+
                                        "'),"+(float)((float)Collections.frequency(words, key)/(float)words.size())+");";
             ResultSet result = myDatabase.runSql(wordSelectQuery);
             if(result.next()){
                 myDatabase.runSqlQuery(wordUpdateQuery);
             }
             else {
                 myDatabase.runSqlQuery(wordInsertQuery);
             }
             result.close();
             myDatabase.runSqlQuery(idfRealUpdateQuery);
             myDatabase.runSqlQuery(idfStemmedUpdateQuery);
             myDatabase.runSqlQuery(tfInsertQuery);
         }
    } //  Updatig an old document
    
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
                       String startTransactionQuery = "Begin Transaction;";
                       myDatabase.runSqlQuery(startTransactionQuery);
                       String documentExistQuery = "Select * from Documents where URL='"+((doc.body().text()).split("\\s+"))[0]+"';";
                       ResultSet result = myDatabase.runSql(documentExistQuery);
                       if(result.next()){
                           System.out.println("Updating Document");
                           updateOldDocument(doc,myDatabase);
                       }
                       else {
                           System.out.println("Inserting Document");
                           insertNewDocument(doc,myDatabase);
                       }
                       result.close();
                       String endTransactionQuery = "End Transaction;";
                       myDatabase.runSqlQuery(endTransactionQuery);
                       boolean delete = file.delete();
                       if(delete)System.out.println("Document Finished and Deleted!");
                       else System.out.println("Error deleting files!");
                    }
                }
            }
            catch (IOException | SQLException ex) {
                System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            }
        }
    }
}