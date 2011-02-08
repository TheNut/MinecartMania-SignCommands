package com.afforess.minecartmaniasigncommands;

import java.io.File;
import java.sql.*;

import com.afforess.bukkit.minecartmaniacore.Debug;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaCore;

public abstract class SignDatabase {
	
	public final static String db = "jdbc:sqlite:" + MinecartManiaCore.dataDirectory + File.separator + "minecartmania.db";
	private static Connection connection;
	
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(db);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static void initDatabase() throws SQLException  {
		connection = getConnection();
		if (connection == null) {
			Debug.log("Failed to initialize connection");
			return;
		}
    	DatabaseMetaData dbm = connection.getMetaData();
    	ResultSet rs = dbm.getTables(null, null, "warpsigns", null);
    	
    	//Empty table, create a new one
    	if (!rs.next()) {
    		Debug.log("Initializing Warp Sign Table");
    		
    		connection.setAutoCommit(false);
    		Statement st = connection.createStatement();
    		st.execute("CREATE TABLE `warpsigns` (`destination` varchar(32) NOT NULL,`x` REAL, `y` REAL, `z` REAL);");
    		connection.commit();
    		
    		Debug.log("Warp Sign Table Created.");
    	}
    	connection.close();
    	rs.close();
    	
	}
}
