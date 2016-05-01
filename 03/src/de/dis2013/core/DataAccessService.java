package de.dis2013.core;

import de.dis2013.data.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Date;

public class DataAccessService {

    private SessionFactory sessionFactory;

    public DataAccessService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

    public void persistEstateAgent(String name, String address, String login, String pass) {
        EstateAgent agent = new EstateAgent();
        agent.setName(name);
        agent.setAddress(address);
        agent.setLogin(login);
        agent.setPassword(pass);

        persist(agent);
    }

    public void persistPerson(String firstName, String name, String address) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setName(name);
        person.setAddress(address);
        persist(person);
    }

    public void persistHouse(String city, int postalCode, String street,  String streetNumber, int squareArea,
                             EstateAgent agent, int floors, int price, boolean garden) {
        House house = new House();
        house.setCity(city);
        house.setPostalCode(postalCode);
        house.setStreet(street);
        house.setSquareArea(squareArea);
        house.setEstateAgent(agent);
        house.setFloors(floors);
        house.setPrice(price);
        house.setGarden(garden);
        persist(house);
    }

    public void persistApartment(String city, int postalCode, String street,  String streetNumber, int squareArea,
                             EstateAgent agent, int floor, int rent, int rooms, boolean balcony, boolean kitchen) {
        Apartment apartment = new Apartment();
        apartment.setCity(city);
        apartment.setPostalCode(postalCode);
        apartment.setStreet(street);
        apartment.setSquareArea(squareArea);
        apartment.setEstateAgent(agent);
        apartment.setFloor(floor);
        apartment.setRent(rent);
        apartment.setRooms(rooms);
        apartment.setBalcony(balcony);
        apartment.setBuiltInKitchen(kitchen);
        persist(apartment);
    }

    public void persistTenancyContract(int contractNumber, Date date, String place, Person person, Date startDate,
                                       int duration, int additionalCosts, Apartment apartment) {
        TenancyContract contract = new TenancyContract();
        contract.setContractNumber(contractNumber);
        contract.setDate(date);
        contract.setPlace(place);
        contract.setPerson(person);
        contract.setStartDate(startDate);
        contract.setDuration(duration);
        contract.setAdditionalCosts(additionalCosts);
        contract.setApartment(apartment);
        persist(contract);
    }

    public void persistPurchaseContract(int contractNumber, Date date, String place, Person person, int numberOfInstallments,
                                        int interestRate, House house) {
        PurchaseContract contract = new PurchaseContract();
        contract.setContractNumber(contractNumber);
        contract.setDate(date);
        contract.setPlace(place);
        contract.setPerson(person);
        contract.setNumberOfInstallments(numberOfInstallments);
        contract.setInterestRate(interestRate);
        contract.setHouse(house);
        persist(contract);
    }


    /** persist given object to db*/
    private void persist(Object object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(object);
		session.getTransaction().commit();
    }

    public Object getById(Class clazz, int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Object result = session.get(clazz, id);
        session.close();

        return result;
    }

    public EstateAgent getAgentByLogin(String login) {
        String hql = "FROM estate_agent agent WHERE agent.login= :login";
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery(hql);
        query.setString("login", login);
        return (EstateAgent) query.uniqueResult();
    }
}
