package fr.wixonic.market;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	private Connection connection;

	public Connection connect(String url) throws SQLException {
		connection = DriverManager.getConnection(url);
		connection.createStatement().execute("SELECT version FROM CoreData;");
		return connection;
	}

	public Object get(String path) throws SQLException {
		Statement statement = connection.createStatement();
		
		return null;
	}

	public void set(String path, Object obj) {
		
	}

	public boolean getBoolean(String path) {
		try {
			return (boolean) this.get(path);
		} catch (Exception ignored) {
			this.set(path, false);
			return false;
		}
	}

	public int getInt(String path) {
		try {
			return (int) this.get(path);
		} catch (Exception ignored) {
			this.set(path, 0);
			return 0;
		}
	}

	public String getString(String path) {
		try {
			return (String) this.get(path);
		} catch (Exception ignored) {
			this.set(path, "");
			return "";
		}
	}

	public String count(int buying, int selling, String type) {
		if (buying > 0 && selling > 0) {
			return (buying + selling) + " " + type + "s - " + count(buying, 0, "buying " + type) + ", " + count(0, selling, "selling " + type);
		} else if (buying > 0) {
			return buying > 1 ? buying + type + "s" : "One " + type;
		} else if (selling > 0) {
			return selling > 1 ? selling + type + "s" : "One " + type;
		} else {
			return "No " + type;
		}
	}
}