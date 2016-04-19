package de.dis2011.data;

import java.sql.*;

/**
 * Normally one would have a data-access-object (DAO) per entity, but...
 */
public class DataAccessHelper {

    /**
     * Load one row from db, parse to estateagent object
     */
    public EstateAgent load(int id) {
        try {
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            String selectSQL = "SELECT * FROM estate_agent WHERE id = ?";
            PreparedStatement query = con.prepareStatement(selectSQL);
            query.setInt(1, id);

            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                EstateAgent agent = new EstateAgent();
                agent.setId(id);
                agent.setName(resultSet.getString(EstateAgent.DB_COLUMN_NAME));
                agent.setAddress(resultSet.getString(EstateAgent.DB_COLUMN_ADDRESS));
                agent.setLogin(resultSet.getString(EstateAgent.DB_COLUMN_LOGIN));
                agent.setPassword(resultSet.getString(EstateAgent.DB_COLUMN_PASSWORD));

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
    public int save(EstateAgent agent) {
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            if (agent.getId() == -1) {
                String insertSQL = "INSERT INTO estate_agent("
                        + EstateAgent.DB_COLUMN_NAME + ", "
                        + EstateAgent.DB_COLUMN_ADDRESS + ", "
                        + EstateAgent.DB_COLUMN_LOGIN + ", "
                        + EstateAgent.DB_COLUMN_PASSWORD + ") VALUES (?, ?, ?, ?)";

                PreparedStatement query = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

                query.setString(1, agent.getName());
                query.setString(2, agent.getAddress());
                query.setString(3, agent.getLogin());
                query.setString(4, agent.getPassword());
                query.executeUpdate();

                ResultSet resultSet = query.getGeneratedKeys();
                if (resultSet.next()) {
                    agent.setId(resultSet.getInt(1));
                }

                resultSet.close();
                query.close();
            } else {
                String updateSQL = "UPDATE estate_agent SET "
                        + EstateAgent.DB_COLUMN_NAME + " = ?, "
                        + EstateAgent.DB_COLUMN_ADDRESS + " = ?, "
                        + EstateAgent.DB_COLUMN_LOGIN + " = ?, "
                        + EstateAgent.DB_COLUMN_PASSWORD + " = ? WHERE id = ?";
                PreparedStatement query = con.prepareStatement(updateSQL);

                query.setString(1, agent.getName());
                query.setString(2, agent.getAddress());
                query.setString(3, agent.getLogin());
                query.setString(4, agent.getPassword());
                query.setInt(5, agent.getId());
                query.executeUpdate();

                query.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agent.getId();
    }
}
