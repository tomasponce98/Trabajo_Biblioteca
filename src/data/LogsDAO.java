package data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Log;

public class LogsDAO extends BaseDAO{

	public void insert(Log log) throws SQLException {
		PreparedStatement pst = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("INSERT INTO logs(level,stacktrace,fecha)"
					+ "  VALUES(?,?,current_timestamp())");
			pst.setString(1, log.getLevel());
			pst.setString(2, log.getStack());
			pst.executeUpdate();
			this.closeConnection(pst);
		}
		catch (SQLException e){
			throw e;
		}
		finally {
			this.closeConnection(pst);
		}
	}
	
}
