package de.dis2013.authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import de.dis2013.util.FormUtil;

/**
 * Authentifiziert einen Benutzer anhand der Daten aus einer Properties-Datei.
 * Die Datei muss die beiden Einträge username=... und password=... enthalten.
 */
public class PropertiesFileAuthenticator implements Authenticator {
	private String username;
	private String password;
	
	/**
	 * Konstruktor.
	 * @param propertiesFile Der Dateiname der Properties-Datei, die die
	 * Benutzerdaten enthält
	 */
	public PropertiesFileAuthenticator(String propertiesFile) {
		try {
			Properties properties = new Properties();
			URL url = ClassLoader.getSystemResource(propertiesFile);
			FileInputStream stream = new FileInputStream(new File(url.toURI()));
			properties.load(stream);
			stream.close();
			
			this.username = properties.getProperty("username");
			this.password = properties.getProperty("password");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fragt nach Benutzernamen und Passwort und vergleicht mit den Daten
	 * aus der Properties-Datei
	 */
	public boolean authenticate() {
		String username = FormUtil.readString("Benutzername");
		String password = FormUtil.readPassword("Passwort");
		
		if(this.username.equals(username) && this.password.equals(password)) {
			return true;
		} else {
			FormUtil.showMessage("Benutzername oder Passwort falsch!");
			return false;
		}
	}

}
