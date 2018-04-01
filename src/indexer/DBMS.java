/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package indexer;
import java.sql.*;

/**
 *
 * @author Apt Team
 */
public class DBMS {
    
    
     // JDBC driver name and database URL
    String url =    "jdbc:sqlite:C:\\Users\\Owner\\Desktop\\Cairo University"+
                    "\\Senior-1\\Advanced Programming Techniques\\Project Modules"+
                    "\\Indexer\\Indexer.db";
    Connection conn;
	public DBMS() {
            
            try {
                    Class.forName("org.sqlite.JDBC");
                    conn = DriverManager.getConnection(url);
                    Statement stmt = conn.createStatement();
                    String createDocuments =
                                "CREATE TABLE IF NOT EXISTS Documents"+
                                "("+
                                "DID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                "URL VARCHAR(1000) NOT NULL, "+
                                "Title TEXT NOT NULL, "+
                                "Body TEXT NOT NULL);";
                    
                    stmt.execute(createDocuments);
                    
                    String createWords =
                                "CREATE TABLE IF NOT EXISTS Words"+
                                "("+
                                "WID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                "Word VARCHAR(50) NOT NULL, "+
                                "WordStemmed TEXT NOT NULL, "+
                                "Count INT NOT NULL, "+
                                "IDF FLOAT);";
                    
                    stmt.execute(createWords);
                    
                    String createRelations =
                                "CREATE TABLE IF NOT EXISTS Relations("+
                                "DocID INT, "+
                                "WordID INT, "+
                                "Positions TEXT NOT NULL," +
                                "TF FLOAT, "+
                                "Rank FLOAT, "+
                                "TF_IDF FLOAT, "+
                                "FOREIGN KEY(DocID) REFERENCES Documents(DID), "+
                                "FOREIGN KEY(WordID) REFERENCES Words(WID));";
                    
                    stmt.execute(createRelations);
                }
            catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                }
	}
        public ResultSet runSql(String sql) throws SQLException {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
	}
 
	public boolean runSqlQuery(String sql) throws SQLException {
            Statement stmt = conn.createStatement();
            return stmt.execute(sql);
	}

        
	protected void finalize() throws Throwable {
            if (conn != null || !conn.isClosed()) {
                    conn.close();
            }
	}
}
