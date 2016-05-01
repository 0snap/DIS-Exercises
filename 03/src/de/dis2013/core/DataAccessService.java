package de.dis2013.core;

import de.dis2013.data.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataAccessService {

    private SessionFactory sessionFactory;

    public DataAccessService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}


    /** persist given object to db*/
    public void persist(Object object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(object);
		session.getTransaction().commit();
        session.close();
    }

    /** persist given object to db*/
    public void update(Object object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(object);
        session.getTransaction().commit();
        session.close();
    }



    // query stuff

    public Object getById(Class clazz, int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Object result = session.get(clazz, id);
        session.close();
        return result;
    }

    public void delete(Object object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(object);
        session.getTransaction().commit();
        session.close();
    }

    public EstateAgent getAgentByLogin(String login) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(EstateAgent.class);
        criteria.add(Restrictions.eq("login", login));
        EstateAgent result = (EstateAgent) criteria.uniqueResult();
        session.close();
        return result;
    }

    public List<EstateAgent> getAllAgents() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<EstateAgent> result = session.createCriteria(EstateAgent.class).list();
        session.close();
        return result;
    }

    public List<Person> getAllPersons() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Person> result = session.createCriteria(Person.class).list();
        session.close();
        return result;
    }

    private List<TenancyContract> getAllTenancyContracts() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<TenancyContract> result = session.createCriteria(TenancyContract.class).list();
        session.close();
        return result;
    }

    private List<PurchaseContract> getAllPurchaseContracts() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<PurchaseContract> result = session.createCriteria(PurchaseContract.class).list();
        session.close();
        return result;
    }

    public List<Apartment> getAllApartmentsForAgent(EstateAgent agent) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Apartment.class);
        criteria.add(Restrictions.eq("estateAgent", agent));
        List<Apartment> result = criteria.list();
        session.close();
        return result;
    }

    public List<House> getAllHousesForAgent(EstateAgent agent) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(House.class);
        criteria.add(Restrictions.eq("estateAgent", agent));
        List<House> result = criteria.list();
        session.close();
        return result;
    }

    public List<TenancyContract> getAllTenancyContractsForAgent(EstateAgent agent) {
        List<Apartment> apartments = getAllApartmentsForAgent(agent);
        List<TenancyContract> allContracts = getAllTenancyContracts();
        List<TenancyContract> result = new ArrayList<>();
        for(Apartment apartment : apartments) {
            result.addAll(allContracts.stream().filter(
                    contract -> contract.getApartment().getId() == apartment.getId()).collect(Collectors.toList()));
        }
        return result;
    }

    public List<PurchaseContract> getAllPurchaseContractsForAgent(EstateAgent agent) {
        List<House> houses = getAllHousesForAgent(agent);
        List<PurchaseContract> allContracts = getAllPurchaseContracts();
        List<PurchaseContract> result = new ArrayList<>();
        for(House house : houses) {
            result.addAll(allContracts.stream().filter(
                    contract -> contract.getHouse().getId() == house.getId()).collect(Collectors.toList()));
        }
        return result;
    }

}
