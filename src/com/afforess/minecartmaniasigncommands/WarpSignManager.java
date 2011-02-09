package com.afforess.minecartmaniasigncommands;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
public class WarpSignManager {

	/*public static void addWarpSign(Sign sign) {
		Connection conn = SignDatabase.getConnection();
		if (conn != null) {
			try {
				ResultSet rs = conn.getMetaData().getTables(null, null, "warpsigns", null);
				//rs.
			} catch (SQLException e) {
				
			}
		}
	}
	
	public static Sign getWarpSign(String destination) {
		//Iterator<Entry<Vector, Sign>> i = signs.entrySet().iterator();
		//while(i.hasNext()) {
		//	Entry<Vector, Sign> e = i.next();
		//	if (e.getValue().getLine(1).equals(destination)) {
				return e.getValue();
		//	}
		//}
		return null;
	}
*/
}
