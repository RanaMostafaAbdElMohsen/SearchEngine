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
                                "CREATE VIRTUAL TABLE IF NOT EXISTS Documents "+
                                "USING fts4("+
                                "URL TEXT NOT NULL, "+
                                "Title TEXT NOT NULL, "+
                                "Body TEXT NOT NULL);";
                    
                    stmt.execute(createDocuments);
                    
                    String createWords =
                                "CREATE VIRTUAL TABLE IF NOT EXISTS Words "+
                                "USING fts4("+
                                "Word TEXT NOT NULL, "+
                                "Count INT NOT NULL, "+
                                "IDFreal FLOAT, "+
                                "IDFstemmed FLOAT);";
                    
                    stmt.execute(createWords);
                    
                    String createRelations =
                                "CREATE TABLE IF NOT EXISTS Relations("+
                                "DocID INT, "+
                                "WordID INT, "+
                                "TF FLOAT, "+
                                "TF_IDF FLOAT, "+
                                "FOREIGN KEY(DocID) REFERENCES Documents(docid), "+
                                "FOREIGN KEY(WordID) REFERENCES Words(docid));";
                    
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
