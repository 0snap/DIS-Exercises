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
public class ImmoService {
	//Datensätze im Speicher
	private Set<EstateAgent> makler = new HashSet<EstateAgent>();
	private Set<Person> personen = new HashSet<Person>();
	private Set<House> haeuser = new HashSet<House>();
	private Set<Apartment> wohnungen = new HashSet<Apartment>();
	private Set<TenancyContract> mietvertraege = new HashSet<TenancyContract>();
	private Set<PurchaseContract> kaufvertraege = new HashSet<PurchaseContract>();
	
	//Hibernate Session
	private SessionFactory sessionFactory;
	
	public ImmoService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	/**
	 * Finde einen EstateAgent mit gegebener Id
	 * @param id Die ID des Maklers
	 * @return EstateAgent mit der ID oder null
	 */
	public EstateAgent getMaklerById(int id) {
		Iterator<EstateAgent> it = makler.iterator();
		
		while(it.hasNext()) {
			EstateAgent m = it.next();
			
			if(m.getId() == id)
				return m;
		}
		
		return null;
	}
	
	/**
	 * Finde einen EstateAgent mit gegebenem Login
	 * @param login Der Login des Maklers
	 * @return EstateAgent mit der ID oder null
	 */
	public EstateAgent getMaklerByLogin(String login) {
		Iterator<EstateAgent> it = makler.iterator();
		
		while(it.hasNext()) {
			EstateAgent m = it.next();
			
			if(m.getLogin().equals(login))
				return m;
		}
		
		return null;
	}
	
	/**
	 * Gibt alle EstateAgent zurück
	 */
	public Set<EstateAgent> getAllMakler() {
		return makler;
	}
	
	/**
	 * Finde eine Person mit gegebener Id
	 * @param id Die ID der Person
	 * @return Person mit der ID oder null
	 */
	public Person getPersonById(int id) {
		Iterator<Person> it = personen.iterator();
		
		while(it.hasNext()) {
			Person p = it.next();
			
			if(p.getId() == id)
				return p;
		}
		
		return null;
	}
	
	/**
	 * Fügt einen EstateAgent hinzu
	 * @param m Der EstateAgent
	 */
	public void addMakler(EstateAgent m) {
		makler.add(m);
	}
	
	/**
	 * Löscht einen EstateAgent
	 * @param m Der EstateAgent
	 */
	public void deleteMakler(EstateAgent m) {
		makler.remove(m);
	}
	
	/**
	 * Fügt eine Person hinzu
	 * @param p Die Person
	 */
	public void addPerson(Person p) {
		personen.add(p);
	}
	
	/**
	 * Gibt alle Personen zurück
	 */
	public Set<Person> getAllPersons() {
		return personen;
	}
	
	/**
	 * Löscht eine Person
	 * @param p Die Person
	 */
	public void deletePerson(Person p) {
		personen.remove(p);
	}
	
	/**
	 * Fügt ein House hinzu
	 * @param h Das House
	 */
	public void addHaus(House h) {
		haeuser.add(h);
	}
	
	/**
	 * Gibt alle Häuser eines Maklers zurück
	 * @param m Der EstateAgent
	 * @return Alle Häuser, die vom EstateAgent verwaltet werden
	 */
	public Set<House> getAllHaeuserForMakler(EstateAgent m) {
		Set<House> ret = new HashSet<House>();
		Iterator<House> it = haeuser.iterator();
		
		while(it.hasNext()) {
			House h = it.next();
			
			if(h.getEstateAgent().equals(m))
				ret.add(h);
		}
		
		return ret;
	}
	
	/**
	 * Findet ein House mit gegebener ID
	 * @param m Der EstateAgent
	 * @return Das House oder null, falls nicht gefunden
	 */
	public House getHausById(int id) {
		Iterator<House> it = haeuser.iterator();
		
		while(it.hasNext()) {
			House h = it.next();
			
			if(h.getId() == id)
				return h;
		}
		
		return null;
	}
	
	/**
	 * Löscht ein House
	 * @param p Das House
	 */
	public void deleteHouse(House h) {
		haeuser.remove(h);
	}
	
	/**
	 * Fügt eine Apartment hinzu
	 * @param w die Apartment
	 */
	public void addWohnung(Apartment w) {
		wohnungen.add(w);
	}
	
	/**
	 * Gibt alle Wohnungen eines Maklers zurück
	 * @param m Der EstateAgent
	 * @return Alle Wohnungen, die vom EstateAgent verwaltet werden
	 */
	public Set<Apartment> getAllWohnungenForMakler(EstateAgent m) {
		Set<Apartment> ret = new HashSet<Apartment>();
		Iterator<Apartment> it = wohnungen.iterator();
		
		while(it.hasNext()) {
			Apartment w = it.next();
			
			if(w.getEstateAgent().equals(m))
				ret.add(w);
		}
		
		return ret;
	}
	
	/**
	 * Findet eine Apartment mit gegebener ID
	 * @param id Die ID
	 * @return Die Apartment oder null, falls nicht gefunden
	 */
	public Apartment getWohnungById(int id) {
		Iterator<Apartment> it = wohnungen.iterator();
		
		while(it.hasNext()) {
			Apartment w = it.next();
			
			if(w.getId() == id)
				return w;
		}
		
		return null;
	}
	
	/**
	 * Löscht eine Apartment
	 * @param p Die Apartment
	 */
	public void deleteWohnung(Apartment w) {
		wohnungen.remove(w);
	}
	
	
	/**
	 * Fügt einen TenancyContract hinzu
	 * @param w Der TenancyContract
	 */
	public void addMietvertrag(TenancyContract m) {
		mietvertraege.add(m);
	}
	
	/**
	 * Fügt einen PurchaseContract hinzu
	 * @param w Der PurchaseContract
	 */
	public void addKaufvertrag(PurchaseContract k) {
		kaufvertraege.add(k);
	}
	
	/**
	 * Gibt alle Mietverträge zu Wohnungen eines Maklers zurück
	 * @param m Der EstateAgent
	 * @return Alle Mietverträge, die zu Wohnungen gehören, die vom EstateAgent verwaltet werden
	 */
	public Set<TenancyContract> getAllMietvertraegeForMakler(EstateAgent m) {
		Set<TenancyContract> ret = new HashSet<TenancyContract>();
		Iterator<TenancyContract> it = mietvertraege.iterator();
		
		while(it.hasNext()) {
			TenancyContract v = it.next();
			
			if(v.getApartment().getEstateAgent().equals(m))
				ret.add(v);
		}
		
		return ret;
	}
	
	/**
	 * Gibt alle Kaufverträge zu Wohnungen eines Maklers zurück
	 * @param m Der EstateAgent
	 * @return Alle Kaufverträge, die zu Häusern gehören, die vom EstateAgent verwaltet werden
	 */
	public Set<PurchaseContract> getAllKaufvertraegeForMakler(EstateAgent m) {
		Set<PurchaseContract> ret = new HashSet<PurchaseContract>();
		Iterator<PurchaseContract> it = kaufvertraege.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getHouse().getEstateAgent().equals(m))
				ret.add(k);
		}
		
		return ret;
	}
	
	/**
	 * Findet einen TenancyContract mit gegebener ID
	 * @param id Die ID
	 * @return Der TenancyContract oder null, falls nicht gefunden
	 */
	public TenancyContract getMietvertragById(int id) {
		Iterator<TenancyContract> it = mietvertraege.iterator();
		
		while(it.hasNext()) {
			TenancyContract m = it.next();
			
			if(m.getId() == id)
				return m;
		}
		
		return null;
	}
	
	/**
	 * Findet alle Mietverträge, die Wohnungen eines gegebenen Verwalters betreffen
	 * @param id Der Verwalter
	 * @return Set aus Mietverträgen
	 */
	public Set<TenancyContract> getMietvertragByVerwalter(EstateAgent m) {
		Set<TenancyContract> ret = new HashSet<TenancyContract>();
		Iterator<TenancyContract> it = mietvertraege.iterator();
		
		while(it.hasNext()) {
			TenancyContract mv = it.next();
			
			if(mv.getApartment().getEstateAgent().getId() == m.getId())
				ret.add(mv);
		}
		
		return ret;
	}
	
	/**
	 * Findet alle Kaufverträge, die Häuser eines gegebenen Verwalters betreffen
	 * @param id Der Verwalter
	 * @return Set aus Kaufverträgen
	 */
	public Set<PurchaseContract> getKaufvertragByVerwalter(EstateAgent m) {
		Set<PurchaseContract> ret = new HashSet<PurchaseContract>();
		Iterator<PurchaseContract> it = kaufvertraege.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getHouse().getEstateAgent().getId() == m.getId())
				ret.add(k);
		}
		
		return ret;
	}
	
	/**
	 * Findet einen PurchaseContract mit gegebener ID
	 * @param id Die ID
	 * @return Der PurchaseContract oder null, falls nicht gefunden
	 */
	public PurchaseContract getKaufvertragById(int id) {
		Iterator<PurchaseContract> it = kaufvertraege.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getId() == id)
				return k;
		}
		
		return null;
	}
	
	/**
	 * Löscht einen TenancyContract
	 * @param m Der TenancyContract
	 */
	public void deleteMietvertrag(TenancyContract m) {
		wohnungen.remove(m);
	}
	
	/**
	 * Fügt einige Testdaten hinzu
	 */
	public void addTestData() {
		//Hibernate Session erzeugen
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		EstateAgent m = new EstateAgent();
		m.setName("Max Mustermann");
		m.setAddress("Am Informatikum 9");
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
		kv.setVertragspartner(p1);
		kv.setContractNumber(9234);
		kv.setDate(new Date(System.currentTimeMillis()));
		kv.setPlace("Hamburg");
		kv.setNumberOfInstallments(5);
		kv.setInterestRate(4);
		this.addKaufvertrag(kv);
		
		TenancyContract mv = new TenancyContract();
		mv.setApartment(w);
		mv.setVertragspartner(p2);
		mv.setContractNumber(23112);
		mv.setDate(new Date(System.currentTimeMillis()-1000000000));
		mv.setPlace("Berlin");
		mv.setStartDate(new Date(System.currentTimeMillis()));
		mv.setAdditionalCosts(65);
		mv.setDuration(36);
		this.addMietvertrag(mv);
	}
}
