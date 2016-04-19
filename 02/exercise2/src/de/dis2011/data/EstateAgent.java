package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * EstateAgent-Pojo
 */
public class EstateAgent {

	private static final String DB_COLUMN_NAME = "NAME";
	private static final String DB_COLUMN_ADDRESS = "ADDRESS";
	private static final String DB_COLUMN_LOGIN = "LOGIN";
	private static final String DB_COLUMN_PASSWORD = "PASSWORD";
	private int id = -1;
	private String name;
	private String address;
	private String login;
	private String password;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Load one row from db, parse to estateagent object
	 */
	public static EstateAgent load(int id) {
		try {
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			String selectSQL = "SELECT * FROM estate_agent WHERE id = ?";
			PreparedStatement query = con.prepareStatement(selectSQL);
			query.setInt(1, id);

			ResultSet resultSet = query.executeQuery();
			if (resultSet.next()) {
				EstateAgent agent = new EstateAgent();
				agent.setId(id);
				agent.setName(resultSet.getString(DB_COLUMN_NAME));
				agent.setAddress(resultSet.getString(DB_COLUMN_ADDRESS));
				agent.setLogin(resultSet.getString(DB_COLUMN_LOGIN));
				agent.setPassword(resultSet.getString(DB_COLUMN_PASSWORD));

				resultSet.close();
				query.close();
				return agent;
			}
			else {
				System.err.println("Could not find an estate agent with that ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Persist the current estateAgent object to db. An ID will be automatically
	 * fetched (if not already present) and set. Returns the ID of the agent.
	 */
	public int save() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			if (getId() == -1) {
				String insertSQL = "INSERT INTO estate_agent("
                        + DB_COLUMN_NAME + ", "
                        + DB_COLUMN_ADDRESS + ", "
                        + DB_COLUMN_LOGIN + ", "
                        + DB_COLUMN_PASSWORD + ") VALUES (?, ?, ?, ?)";

				PreparedStatement query = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

				query.setString(1, getName());
				query.setString(2, getAddress());
				query.setString(3, getLogin());
				query.setString(4, getPassword());
				query.executeUpdate();

				ResultSet resultSet = query.getGeneratedKeys();
				if (resultSet.next()) {
					setId(resultSet.getInt(1));
				}

				resultSet.close();
				query.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate_agent SET "
						+ DB_COLUMN_NAME + " = ?, "
						+ DB_COLUMN_ADDRESS + " = ?, "
						+ DB_COLUMN_LOGIN + " = ?, "
						+ DB_COLUMN_PASSWORD + " = ? WHERE id = ?";
				PreparedStatement query = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				query.setString(1, getName());
				query.setString(2, getAddress());
				query.setString(3, getLogin());
				query.setString(4, getPassword());
				query.setInt(5, getId());
				query.executeUpdate();

				query.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getId();
	}
}
