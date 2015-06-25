/**
 * 
 */
package com.crowd.streetbuzzalgo.webcrawler;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * @author Atrijit
 * 
 */
public class BonePool {
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BoneCPDataSource ds = new BoneCPDataSource();
		ds.setJdbcUrl("jdbc:mysql://localhost:3306/onlineadmissions");
		ds.setUsername("on_lineadms"); // set the username
		ds.setPassword("on_lineadms#@!");
		ds.setPartitionCount(1);
		ds.setMaxConnectionsPerPartition(10);
		ds.setMinConnectionsPerPartition(5);
		Connection connection = null;
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	public static void closeConnection(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
