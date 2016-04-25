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

    /** Load one row from db, parse to estate object. result depends on type
     * (0=house, 1=apartment, 2=none, plain estate)
     */
    public Estate loadEstate(int id, int type) {
        try {
            Connection con = DB2ConnectionManager.getInstance().getConnection();
            String tableName = getEstateTableNameForType(type);
            String selectSQL = "SELECT * FROM " + tableName + " WHERE " + Estate.DB_COLUMN_ID + " = ?";
            PreparedStatement query = con.prepareStatement(selectSQL);
            query.setInt(1, id);

            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                Estate estate = extractEstate(resultSet, type);
                estate.setId(id);

                resultSet.close();
                query.close();
                return estate;
            }
            else {
                System.err.println("Could not find an estate with that ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Estate extractEstate(ResultSet resultSet, int type) throws SQLException {
        Estate estate = new Estate();
        estate.setCity(resultSet.getString(Estate.DB_COLUMN_CITY));
        estate.setPostalCode(resultSet.getString(Estate.DB_COLUMN_POSTAL_CODE));
        estate.setStreet(resultSet.getString(Estate.DB_COLUMN_STREET));
        estate.setStreetNumber(resultSet.getInt(Estate.DB_COLUMN_STREET_NUMBER));
        estate.setSquareArea(resultSet.getString(Estate.DB_COLUMN_SQUARE_AREA));
        estate.setEstateAgent(resultSet.getInt(Estate.DB_COLUMN_ESTATE_AGENT));
        if(type == 0) {
            House house = new House(estate);
            house.setFloors(resultSet.getInt(House.DB_COLUMN_FLOORS));
            house.setPrice(resultSet.getDouble(House.DB_COLUMN_PRICE));
            house.setGarden(resultSet.getInt(House.DB_COLUMN_GARDEN));
            house.setPurchaseContract(resultSet.getInt(House.DB_COLUMN_PURCHASE_CONTRACT));
            house.setPerson(resultSet.getInt(House.DB_COLUMN_PERSON));
            return house;
        }if(type == 0) {
            Apartment apartment = new Apartment(estate);
            apartment.setFloor(resultSet.getInt(Apartment.DB_COLUMN_FLOOR));
            apartment.setRent(resultSet.getDouble(Apartment.DB_COLUMN_RENT));
            apartment.setRooms(resultSet.getInt(Apartment.DB_COLUMN_ROOMS));
            apartment.setBalcony(resultSet.getInt(Apartment.DB_COLUMN_BALCONY));
            apartment.setBuiltInKitchen(resultSet.getInt(Apartment.DB_COLUMN_BUILT_IN_KITCHEN));
            apartment.setTenancyContract(resultSet.getInt(Apartment.DB_COLUMN_TENANCY_CONTRACT));
            apartment.setPerson(resultSet.getInt(Apartment.DB_COLUMN_PERSON));
            return apartment;
        }
        return estate;
    }

    public void deleteEstate(int id, int type){
        try {
            Connection con = DB2ConnectionManager.getInstance().getConnection();
            String tableName = getEstateTableNameForType(type);
            String deleteSQL = "DELETE FROM " + tableName + " WHERE " + Estate.DB_COLUMN_ID + " = ?";
            PreparedStatement query = con.prepareStatement(deleteSQL);
            query.setInt(1, id);
            query.executeUpdate();
            query.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
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
        String insertSQL;
        if(estate instanceof House) {
            insertSQL = "INSERT INTO house("
                    + Estate.DB_COLUMN_CITY + ", "
                    + Estate.DB_COLUMN_POSTAL_CODE + ", "
                    + Estate.DB_COLUMN_STREET + ", "
                    + Estate.DB_COLUMN_STREET_NUMBER + ", "
                    + Estate.DB_COLUMN_SQUARE_AREA + ", "
                    + Estate.DB_COLUMN_ESTATE_AGENT + ", "
                    + House.DB_COLUMN_FLOORS + ", "
                    + House.DB_COLUMN_PRICE + ", "
                    + House.DB_COLUMN_GARDEN + ", "
                    + House.DB_COLUMN_PURCHASE_CONTRACT + ", "
                    + House.DB_COLUMN_PERSON + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
        else if(estate instanceof Apartment) {
            insertSQL = "INSERT INTO house("
                    + Estate.DB_COLUMN_CITY + ", "
                    + Estate.DB_COLUMN_POSTAL_CODE + ", "
                    + Estate.DB_COLUMN_STREET + ", "
                    + Estate.DB_COLUMN_STREET_NUMBER + ", "
                    + Estate.DB_COLUMN_SQUARE_AREA + ", "
                    + Estate.DB_COLUMN_ESTATE_AGENT + ", "
                    + Apartment.DB_COLUMN_FLOOR + ", "
                    + Apartment.DB_COLUMN_RENT + ", "
                    + Apartment.DB_COLUMN_ROOMS + ", "
                    + Apartment.DB_COLUMN_BALCONY + ", "
                    + Apartment.DB_COLUMN_BUILT_IN_KITCHEN + ", "
                    + Apartment.DB_COLUMN_TENANCY_CONTRACT + ", "
                    + Apartment.DB_COLUMN_PERSON + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
        else {
            insertSQL = "INSERT INTO estate("
                    + Estate.DB_COLUMN_CITY + ", "
                    + Estate.DB_COLUMN_POSTAL_CODE + ", "
                    + Estate.DB_COLUMN_STREET + ", "
                    + Estate.DB_COLUMN_STREET_NUMBER + ", "
                    + Estate.DB_COLUMN_SQUARE_AREA + ", "
                    + Estate.DB_COLUMN_ESTATE_AGENT + ") VALUES (?, ?, ?, ?, ?, ?)";
        }
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
        if(estate instanceof House) {
            query.setInt(7, ((House) estate).getFloors());
            query.setDouble(8, ((House) estate).getPrice());
            query.setInt(9, ((House) estate).getGarden());
            query.setInt(10, ((House) estate).getPurchaseContract());
            query.setInt(11, ((House) estate).getPerson());
        }
        else if(estate instanceof Apartment) {
            query.setInt(7, ((Apartment) estate).getFloor());
            query.setDouble(8, ((Apartment) estate).getRent());
            query.setInt(9, ((Apartment) estate).getRooms());
            query.setInt(10, ((Apartment) estate).getBalcony());
            query.setInt(11, ((Apartment) estate).getBuiltInKitchen());
            query.setInt(12, ((Apartment) estate).getTenancyContract());
            query.setInt(13, ((Apartment) estate).getPerson());
        }
        query.executeUpdate();
    }

    private String getEstateTableNameForType(int type) {
        String tableName = "estate";
        switch (type) {
            case 0:
                tableName = "house";
                break;
            case 1:
                tableName = "apartment";
                break;
            default:
                break;
        }
        return tableName;
    }

    /*
     * Contract accesses:
     */

    /**
     * Persist the given contract object to db. An ID will be automatically
     * fetched (if not already present) and set. Returns the ID of the contract.
     *//*
    public int save(Contract contract) {
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // new, create
            if (contract.get_id() == -1) {
                createContract(contract, con);
            }
            // existing, update
            else {
                updateContract(contract, con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contract.get_id();
    }

    *//** Load one row from db, parse to contract object *//*
    public Contract loadContract(int id) {
        try {
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            String selectSQL = "SELECT * FROM estate WHERE " + Contract.DB_COLUMN_ID + " = ?";
            PreparedStatement query = con.prepareStatement(selectSQL);
            query.setInt(1, id);

            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                Contract contract = new Contract();
                contract.set_id(id);
                contract.set_id(resultSet.getInt(Contract.DB_COLUMN_ID));
                contract.set_date(resultSet.getString(Contract.DB_COLUMN_DATE));
                contract.set_place(resultSet.getString(Contract.DB_COLUMN_PLACE));
                contract.set_is_tenancy(resultSet.getBoolean(Contract.DB_COLUMN_IS_TENANCY));
                if(contract.get_is_tenancy()){
                    contract.set_start_date(resultSet.getString(Contract.DB_COLUMN_START_DATE));
                    contract.set_duration(resultSet.getInt(Contract.DB_COLUMN_DURATION));
                    contract.set_cost(resultSet.getInt(Contract.DB_COLUMN_COST));
                }else{
                    contract.set_installments(resultSet.getString(Contract.DB_COLUMN_INSTALLMENTS));
                    contract.set_interest(resultSet.getInt(Contract.DB_COLUMN_INTEREST));
                }
                resultSet.close();
                query.close();
                return contract;
            }
            else {
                System.err.println("Could not find a contract with that ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteContract(int id){
        try {
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            String deleteSQL = "DELETE FROM estate WHERE " + Contract.DB_COLUMN_ID + " = ?";
            PreparedStatement query = con.prepareStatement(deleteSQL);
            query.setInt(1, id);
            query.executeUpdate();
            query.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateContract(Contract contract, Connection con) throws SQLException {
        String updateSQL = "UPDATE contract SET "
                + Contract.DB_COLUMN_ID + " = ?, "
                + Contract.DB_COLUMN_DATE + " = ?, "
                + Contract.DB_COLUMN_PLACE + " = ?, "
                + Contract.DB_COLUMN_IS_TENANCY + " = ?, "
                + Contract.DB_COLUMN_START_DATE + " = ?, "
                + Contract.DB_COLUMN_DURATION + " = ?, "
                + Contract.DB_COLUMN_COST + " = ?, "
                + Contract.DB_COLUMN_INSTALLMENTS + " = ?, "
                + Contract.DB_COLUMN_INTEREST + " = ? WHERE "
                + Contract.DB_COLUMN_ID + " = ?";

        PreparedStatement query = con.prepareStatement(updateSQL);

        query.setInt(7, contract.get_id()); // set already existing id to complete query, then persist
        persistContract(contract, query);

        query.close();
    }

    private void createContract(Contract contract, Connection con) throws SQLException {
        String insertSQL = "INSERT INTO contract("
                + Contract.DB_COLUMN_ID + ", "
                + Contract.DB_COLUMN_DATE + ", "
                + Contract.DB_COLUMN_PLACE + ", "
                + Contract.DB_COLUMN_IS_TENANCY + ", "
                + Contract.DB_COLUMN_START_DATE + ", "
                + Contract.DB_COLUMN_DURATION + ", "
                + Contract.DB_COLUMN_COST + ", "
                + Contract.DB_COLUMN_INSTALLMENTS + ", "
                + Contract.DB_COLUMN_INTEREST + ") VALUES (?,?,?,?,?,?,?,?,?)";


        PreparedStatement query = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
        persistContract(contract, query);

        ResultSet resultSet = query.getGeneratedKeys();
        if (resultSet.next()) {
            contract.set_id(resultSet.getInt(1));
        }

        resultSet.close();
        query.close();
    }

    private void persistContract(Contract contract, PreparedStatement query) throws SQLException {
        query.setInt(1, contract.get_id());
        query.setString(2, contract.get_date());
        query.setString(3, contract.get_place());
        query.setBoolean(4, contract.get_is_tenancy());
        query.setString(5, contract.get_start_date());
        query.setInt(6, contract.get_duration());
        query.setInt(7, contract.get_cost());
        query.setString(8, contract.get_installments());
        query.setInt(9, contract.get_interest());


        query.executeUpdate();
    }
*/
    /*
     * Person accesses:
     */

    /**
     * Persist the given person object to db. An ID will be automatically
     * fetched (if not already present) and set. Returns the ID of the person.
     */
    public int save(Person person) {
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // new, create
            if (person.getId() == -1) {
                createPerson(person, con);
            }
            // existing, update
            else {
                updatePerson(person, con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person.getId();
    }

    /** Load one row from db, parse to person object */
    public Person loadPerson(int id) {
        try {
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            String selectSQL = "SELECT * FROM person WHERE " + Person.DB_COLUMN_ID + " = ?";
            PreparedStatement query = con.prepareStatement(selectSQL);
            query.setInt(1, id);

            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                Person person = new Person();
                person.setId(id);
                person.setName(resultSet.getString(Person.DB_COLUMN_NAME));
                person.setAddress(resultSet.getString(Person.DB_COLUMN_ADDRESS));

                resultSet.close();
                query.close();
                return person;
            }
            else {
                System.err.println("Could not find a person with that ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deletePerson(int id){
        try {
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            String deleteSQL = "DELETE FROM person WHERE " + Person.DB_COLUMN_ID + " = ?";
            PreparedStatement query = con.prepareStatement(deleteSQL);
            query.setInt(1, id);
            query.executeUpdate();
            query.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePerson(Person person, Connection con) throws SQLException {
        String updateSQL = "UPDATE person SET "
                + Person.DB_COLUMN_FIRSTNAME + " = ?, "
                + Person.DB_COLUMN_NAME + " = ?, "
                + Person.DB_COLUMN_ADDRESS + " = ? WHERE "
                + Person.DB_COLUMN_ID + " = ?";
        PreparedStatement query = con.prepareStatement(updateSQL);

        query.setInt(4, person.getId()); // set already existing id to complete query, then persist
        persistPerson(person, query);

        query.close();
    }

    private void createPerson(Person person, Connection con) throws SQLException {
        String insertSQL = "INSERT INTO person("
                + Person.DB_COLUMN_FIRSTNAME + " , "
                + Person.DB_COLUMN_NAME + " , "
                + Person.DB_COLUMN_ADDRESS + ") VALUES (?, ?, ?)";

        PreparedStatement query = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
        persistPerson(person, query);

        ResultSet resultSet = query.getGeneratedKeys();
        if (resultSet.next()) {
            person.setId(resultSet.getInt(1));
        }

        resultSet.close();
        query.close();
    }

    private void persistPerson(Person person, PreparedStatement query) throws SQLException {
        query.setString(1, person.getFirstName());
        query.setString(2, person.getName());
        query.setString(3, person.getAddress());
        query.executeUpdate();
    }


}
