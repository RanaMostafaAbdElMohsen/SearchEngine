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
    String url = "jdbc:sqlserver://DELL\\SQLEXPRESS;databaseName=Indexer;integratedSecurity=true";
    Connection conn;
	public DBMS() {
            
		try {
                        
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        //STEP 3: Open a connection
                        System.out.println("Connecting to database...");
                        conn = DriverManager.getConnection(url);
			System.out.println("conn built");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                    }
	}
        public ResultSet runSql(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.executeQuery(sql);
	}
 
	public boolean runSqlQuery(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.execute(sql);
	}

        
	protected void finalize() throws Throwable {
		if (conn != null || !conn.isClosed()) {
			conn.close();
		}
	}
}
