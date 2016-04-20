package de.dis2011.data;

import java.sql.*;

/**
 * Normally one would have a data-access-object (DAO) per entity, but...
 */
public class DataAccessHelper {

    /*
     * EstateAgent accesses:
     */

    /** Load one row from db, parse to estateagent object */
    public EstateAgent loadEstateAgent(int id) {
        try {
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            String selectSQL = "SELECT * FROM estate_agent WHERE " + EstateAgent.DB_COLUMN_ID + " = ?";
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
     * Persist the given estateAgent object to db. An ID will be automatically
     * fetched (if not already present) and set. Returns the ID of the agent.
     */
    public int save(EstateAgent agent) {
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // new, create
            if (agent.getId() == -1) {
                createEstateAgent(agent, con);
            }
            // existing, update
            else {
                updateEstateAgent(agent, con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agent.getId();
    }

    private void updateEstateAgent(EstateAgent agent, Connection con) throws SQLException {
        String updateSQL = "UPDATE estate_agent SET "
                + EstateAgent.DB_COLUMN_NAME + " = ?, "
                + EstateAgent.DB_COLUMN_ADDRESS + " = ?, "
                + EstateAgent.DB_COLUMN_LOGIN + " = ?, "
                + EstateAgent.DB_COLUMN_PASSWORD + " = ? WHERE "
                + EstateAgent.DB_COLUMN_ID + " = ?";
        PreparedStatement query = con.prepareStatement(updateSQL);

        query.setInt(5, agent.getId()); // set already existing id to complete query, then persist
        persistEstateAgent(agent, query);

        query.close();
    }

    private void createEstateAgent(EstateAgent agent, Connection con) throws SQLException {
        String insertSQL = "INSERT INTO estate_agent("
                + EstateAgent.DB_COLUMN_NAME + ", "
                + EstateAgent.DB_COLUMN_ADDRESS + ", "
                + EstateAgent.DB_COLUMN_LOGIN + ", "
                + EstateAgent.DB_COLUMN_PASSWORD + ") VALUES (?, ?, ?, ?)";

        PreparedStatement query = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
        persistEstateAgent(agent, query);

        ResultSet resultSet = query.getGeneratedKeys();
        if (resultSet.next()) {
            agent.setId(resultSet.getInt(1));
        }

        resultSet.close();
        query.close();
    }

    private void persistEstateAgent(EstateAgent agent, PreparedStatement query) throws SQLException {
        query.setString(1, agent.getName());
        query.setString(2, agent.getAddress());
        query.setString(3, agent.getLogin());
        query.setString(4, agent.getPassword());
        query.executeUpdate();
    }

    /*
     * Estate accesses:
     */

    /**
     * Persist the given estate object to db. An ID will be automatically
     * fetched (if not already present) and set. Returns the ID of the estate.
     */
    public int save(Estate estate) {
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // new, create
            if (estate.getId() == -1) {
                createEstate(estate, con);
            }
            // existing, update
            else {
                updateEstate(estate, con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estate.getId();
    }

    /** Load one row from db, parse to estate object */
    public Estate loadEstate(int id) {
        try {
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            String selectSQL = "SELECT * FROM estate WHERE " + Estate.DB_COLUMN_ID + " = ?";
            PreparedStatement query = con.prepareStatement(selectSQL);
            query.setInt(1, id);

            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                Estate estate = new Estate();
                estate.setId(id);
                estate.setCity(resultSet.getString(Estate.DB_COLUMN_CITY));
                estate.setPostalCode(resultSet.getString(Estate.DB_COLUMN_POSTAL_CODE));
                estate.setStreet(resultSet.getString(Estate.DB_COLUMN_STREET));
                estate.setStreetNumber(resultSet.getInt(Estate.DB_COLUMN_STREET_NUMBER));
                estate.setSquareArea(resultSet.getString(Estate.DB_COLUMN_SQUARE_AREA));
                estate.setEstateAgent(resultSet.getInt(Estate.DB_COLUMN_ESTATE_AGENT));

                resultSet.close();
                query.close();
                return estate;
            }
            else {
                System.err.println("Could not find an estate agent with that ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateEstate(Estate estate, Connection con) throws SQLException {
        String updateSQL = "UPDATE estate SET "
                + Estate.DB_COLUMN_CITY + " = ?, "
                + Estate.DB_COLUMN_POSTAL_CODE + " = ?, "
                + Estate.DB_COLUMN_STREET + " = ?, "
                + Estate.DB_COLUMN_STREET_NUMBER + " = ?, "
                + Estate.DB_COLUMN_SQUARE_AREA + " = ?, "
                + Estate.DB_COLUMN_ESTATE_AGENT + " = ? WHERE "
                + Estate.DB_COLUMN_ID + " = ?";
        PreparedStatement query = con.prepareStatement(updateSQL);

        query.setInt(7, estate.getId()); // set already existing id to complete query, then persist
        persistEstate(estate, query);

        query.close();
    }

    private void createEstate(Estate estate, Connection con) throws SQLException {
        String insertSQL = "INSERT INTO estate("
                + Estate.DB_COLUMN_CITY + ", "
                + Estate.DB_COLUMN_POSTAL_CODE + ", "
                + Estate.DB_COLUMN_STREET + ", "
                + Estate.DB_COLUMN_STREET_NUMBER + ", "
                + Estate.DB_COLUMN_SQUARE_AREA + ", "
                + Estate.DB_COLUMN_ESTATE_AGENT + ") VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement query = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
        persistEstate(estate, query);

        ResultSet resultSet = query.getGeneratedKeys();
        if (resultSet.next()) {
            estate.setId(resultSet.getInt(1));
        }

        resultSet.close();
        query.close();
    }

    private void persistEstate(Estate estate, PreparedStatement query) throws SQLException {
        query.setString(1, estate.getCity());
        query.setString(2, estate.getPostalCode());
        query.setString(3, estate.getStreet());
        query.setInt(4, estate.getStreetNumber());
        query.setString(5, estate.getSquareArea());
        query.setInt(6, estate.getEstateAgent());
        query.executeUpdate();
    }
}
