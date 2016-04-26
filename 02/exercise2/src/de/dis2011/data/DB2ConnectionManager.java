package de.dis2011.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Einfaches Singleton zur Verwaltung von Datenbank-Verbindungen.
 * 
 * @author Michael von Riegen
 * @version April 2009
 */
public class DB2ConnectionManager {

	// instance of Driver Manager
	private static DB2ConnectionManager _instance = null;

	// DB2 connection
	private Connection _con;

	/**
	 * Erzeugt eine Datenbank-Verbindung
	 */
	private DB2ConnectionManager() {
		try {
			String jdbcUser = "vsisp09";
			String jdbcPass = "zd72OoKT";
			String jdbcUrl = "jdbc:db2://vsisls4.informatik.uni-hamburg.de:50001/VSISP";

			// Verbindung zur DB2 herstellen
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			_con = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Liefert Instanz des Managers
	 * 
	 * @return DB2ConnectionManager
	 */
	public static DB2ConnectionManager getInstance() {
		if (_instance == null) {
			_instance = new DB2ConnectionManager();
		}
		return _instance;
	}

	/**
	 * Liefert eine Verbindung zur DB2 zurC<ck
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		return _con;
	}

}