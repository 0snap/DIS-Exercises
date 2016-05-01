package de.dis2013.core;

import de.dis2013.data.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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
        return (EstateAgent) criteria.uniqueResult();
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

    public List<Apartment> getAllApartmentsForAgent(EstateAgent agent) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Apartment.class);
        criteria.add(Restrictions.eq("estateAgent", agent));
        return criteria.list();
    }

    public List<House> getAllHousesForAgent(EstateAgent agent) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(House.class);
        criteria.add(Restrictions.eq("estateAgent", agent));
        return criteria.list();
    }







    // agents have no contracts
    public List<TenancyContract> getAllTenancyContractsForAgent(EstateAgent agent) {
        String hql = "FROM tenancy_contract contract WHERE contract.estate_agent= :agentId";
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery(hql);
        query.setInteger("agentId", agent.getId());
        return query.list();
    }

    public List<PurchaseContract> getAllPurchaseContractsForAgent(EstateAgent agent) {
        String hql = "FROM tenancy_contract contract WHERE contract.estate_agent= :agentId";
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery(hql);
        query.setInteger("agentId", agent.getId());
        return query.list();
    }

}
