package de.dis2013.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.dis2013.data.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.dis2013.data.EstateAgent;

/**
 * Klasse zur Verwaltung aller Datenbank-Entitäten.
 * 
 * TODO: Aktuell werden alle Daten im Speicher gehalten. Ziel der Übung
 * ist es, schrittweise die Datenverwaltung in die Datenbank auszulagern.
 * Wenn die Arbeit erledigt ist, werden alle Sets dieser Klasse überflüssig.
 */
public class EstateService {
	//Datensätze im Speicher
	private Set<EstateAgent> estateAgents = new HashSet<EstateAgent>();
	private Set<Person> persons = new HashSet<Person>();
	private Set<House> houses = new HashSet<House>();
	private Set<Apartment> apartments = new HashSet<Apartment>();
	private Set<TenancyContract> tenancyContracts = new HashSet<TenancyContract>();
	private Set<PurchaseContract> purchaseContracts = new HashSet<PurchaseContract>();
	
	private SessionFactory sessionFactory;
	
	public EstateService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	public EstateAgent getMaklerById(int id) {
		Iterator<EstateAgent> it = estateAgents.iterator();
		
		while(it.hasNext()) {
			EstateAgent m = it.next();
			
			if(m.getId() == id)
				return m;
		}
		
		return null;
	}
	
	public EstateAgent getMaklerByLogin(String login) {
		Iterator<EstateAgent> it = estateAgents.iterator();
		
		while(it.hasNext()) {
			EstateAgent m = it.next();
			
			if(m.getLogin().equals(login))
				return m;
		}
		
		return null;
	}
	
	public Set<EstateAgent> getAllMakler() {
		return estateAgents;
	}
	
	public Person getPersonById(int id) {
		Iterator<Person> it = persons.iterator();
		
		while(it.hasNext()) {
			Person p = it.next();
			
			if(p.getId() == id)
				return p;
		}
		
		return null;
	}

	public Set<Person> getAllPersons() {
		return persons;
	}
	
	public void addMakler(EstateAgent m) {
		estateAgents.add(m);
	}
	
	public void deleteMakler(EstateAgent m) {
		estateAgents.remove(m);
	}
	
	public void addPerson(Person p) {
		persons.add(p);
	}
	

	public void deletePerson(Person p) {
		persons.remove(p);
	}
	
	public void addHaus(House h) {
		houses.add(h);
	}
	
	public Set<House> getAllHaeuserForMakler(EstateAgent m) {
		Set<House> ret = new HashSet<House>();
		Iterator<House> it = houses.iterator();
		
		while(it.hasNext()) {
			House h = it.next();
			
			if(h.getEstateAgent().equals(m))
				ret.add(h);
		}
		
		return ret;
	}
	
	public House getHausById(int id) {
		Iterator<House> it = houses.iterator();
		
		while(it.hasNext()) {
			House h = it.next();
			
			if(h.getId() == id)
				return h;
		}
		
		return null;
	}
	
	public void deleteHouse(House h) {
		houses.remove(h);
	}
	
	public void addWohnung(Apartment w) {
		apartments.add(w);
	}
	
	public Set<Apartment> getAllWohnungenForMakler(EstateAgent m) {
		Set<Apartment> ret = new HashSet<Apartment>();
		Iterator<Apartment> it = apartments.iterator();
		
		while(it.hasNext()) {
			Apartment w = it.next();
			
			if(w.getEstateAgent().equals(m))
				ret.add(w);
		}
		
		return ret;
	}
	
	public Apartment getWohnungById(int id) {
		Iterator<Apartment> it = apartments.iterator();
		
		while(it.hasNext()) {
			Apartment w = it.next();
			
			if(w.getId() == id)
				return w;
		}
		
		return null;
	}
	
	public void deleteWohnung(Apartment w) {
		apartments.remove(w);
	}
	
	
	public void addMietvertrag(TenancyContract m) {
		tenancyContracts.add(m);
	}
	
	public void addKaufvertrag(PurchaseContract k) {
		purchaseContracts.add(k);
	}
	
	public Set<TenancyContract> getAllMietvertraegeForMakler(EstateAgent m) {
		Set<TenancyContract> ret = new HashSet<TenancyContract>();
		Iterator<TenancyContract> it = tenancyContracts.iterator();
		
		while(it.hasNext()) {
			TenancyContract v = it.next();
			
			if(v.getApartment().getEstateAgent().equals(m))
				ret.add(v);
		}
		
		return ret;
	}
	
	public Set<PurchaseContract> getAllKaufvertraegeForMakler(EstateAgent m) {
		Set<PurchaseContract> ret = new HashSet<PurchaseContract>();
		Iterator<PurchaseContract> it = purchaseContracts.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getHouse().getEstateAgent().equals(m))
				ret.add(k);
		}
		
		return ret;
	}
	
	public TenancyContract getMietvertragById(int id) {
		Iterator<TenancyContract> it = tenancyContracts.iterator();
		
		while(it.hasNext()) {
			TenancyContract m = it.next();
			
			if(m.getId() == id)
				return m;
		}
		
		return null;
	}
	
	public Set<TenancyContract> getMietvertragByVerwalter(EstateAgent m) {
		Set<TenancyContract> ret = new HashSet<TenancyContract>();
		Iterator<TenancyContract> it = tenancyContracts.iterator();
		
		while(it.hasNext()) {
			TenancyContract mv = it.next();
			
			if(mv.getApartment().getEstateAgent().getId() == m.getId())
				ret.add(mv);
		}
		
		return ret;
	}
	
	public Set<PurchaseContract> getKaufvertragByVerwalter(EstateAgent m) {
		Set<PurchaseContract> ret = new HashSet<PurchaseContract>();
		Iterator<PurchaseContract> it = purchaseContracts.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getHouse().getEstateAgent().getId() == m.getId())
				ret.add(k);
		}
		
		return ret;
	}
	
	public PurchaseContract getKaufvertragById(int id) {
		Iterator<PurchaseContract> it = purchaseContracts.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getId() == id)
				return k;
		}
		
		return null;
	}
	
	public void deleteMietvertrag(TenancyContract m) {
		apartments.remove(m);
	}
	
	public void addTestData() {
		//Hibernate Session erzeugen
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		EstateAgent m = new EstateAgent();
		m.setName("Max Mustermann");
		m.setAddress("Am Informatikum 11");
		m.setLogin("max");
		m.setPassword("max");
		
		//TODO: Dieser EstateAgent wird im Speicher und der DB gehalten
		this.addMakler(m);
		session.save(m);
		session.getTransaction().commit();

		session.beginTransaction();
		
		Person p1 = new Person();
		p1.setAddress("Informatikum");
		p1.setName("Mustermann");
		p1.setFirstName("Erika");
		
		
		Person p2 = new Person();
		p2.setAddress("Reeperbahn 9");
		p2.setName("Albers");
		p2.setFirstName("Hans");
		
		session.save(p1);
		session.save(p2);
		
		//TODO: Diese Personen werden im Speicher und der DB gehalten
		this.addPerson(p1);
		this.addPerson(p2);
		session.getTransaction().commit();
		
		//Hibernate Session erzeugen
		session.beginTransaction();
		House h = new House();
		h.setCity("Hamburg");
		h.setPostalCode(22527);
		h.setStreet("Vogt-Kölln-Straße");
		h.setStreetNumber("2a");
		h.setSquareArea(384);
		h.setFloors(5);
		h.setPrice(10000000);
		h.setGarden(true);
		h.setEstateAgent(m);
		
		session.save(h);
		
		//TODO: Dieses House wird im Speicher und der DB gehalten
		this.addHaus(h);
		session.getTransaction().commit();

		//Hibernate Session erzeugen
		session = sessionFactory.openSession();
		session.beginTransaction();
		EstateAgent m2 = (EstateAgent)session.get(EstateAgent.class, m.getId());
		Set<Estate> immos = m2.getEstates();
		Iterator<Estate> it = immos.iterator();

		while(it.hasNext()) {
			Estate i = it.next();
			System.out.println("Immo: "+i.getCity());
		}
		session.close();
		
		Apartment w = new Apartment();
		w.setCity("Hamburg");
		w.setPostalCode(22527);
		w.setStreet("Vogt-Kölln-Straße");
		w.setStreetNumber("3");
		w.setSquareArea(120);
		w.setFloor(4);
		w.setRent(790);
		w.setBuiltInKitchen(true);
		w.setBalcony(false);
		w.setEstateAgent(m);
		this.addWohnung(w);
		
		w = new Apartment();
		w.setCity("Berlin");
		w.setPostalCode(22527);
		w.setStreet("Vogt-Kölln-Straße");
		w.setStreetNumber("3");
		w.setSquareArea(120);
		w.setFloor(4);
		w.setRent(790);
		w.setBuiltInKitchen(true);
		w.setBalcony(false);
		w.setEstateAgent(m);
		this.addWohnung(w);
		
		PurchaseContract kv = new PurchaseContract();
		kv.setHouse(h);
		kv.setPerson(p1);
		kv.setContractNumber(9234);
		kv.setDate(new Date(System.currentTimeMillis()));
		kv.setPlace("Hamburg");
		kv.setNumberOfInstallments(5);
		kv.setInterestRate(4);
		this.addKaufvertrag(kv);
		
		TenancyContract mv = new TenancyContract();
		mv.setApartment(w);
		mv.setPerson(p2);
		mv.setContractNumber(23112);
		mv.setDate(new Date(System.currentTimeMillis()-1000000000));
		mv.setPlace("Berlin");
		mv.setStartDate(new Date(System.currentTimeMillis()));
		mv.setAdditionalCosts(65);
		mv.setDuration(36);
		this.addMietvertrag(mv);
	}
}
