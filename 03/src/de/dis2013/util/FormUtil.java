package de.dis2013.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {
	/**
	 * Liest einen String vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Zeile
	 */
	public static String readString(String label) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Liest ein Passwort vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Zeile
	 */
	public static String readPassword(String label) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Zeigt eine Nachricht an und wartet auf Bestätigung des Benutzers
	 * @param msg Nachricht
	 */
	public static void showMessage(String msg) {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print(msg);
			stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Liest einen Integer vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Integer
	 */
	public static int readInt(String label) {
		int ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			
			try {
				ret = Integer.parseInt(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		
		return ret;
	}
	
	/**
	 * Liest ein Datum vom standard input im Format dd.MM.yyyy ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesenes Datum
	 */
	public static Date readDate(String label) {
		SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy");
		Date ret = null;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			

			
			try {
				ret = parser.parse(line);
				finished = true;
			} catch (ParseException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie ein Datum im Format dd.MM.yyyy an!");
			}
		}
		
		return ret;
	}
	
	/**
	 * Stellt eine Ja/Nein-Frage und gibt das Ergebnis zurück
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return true, falls ja, false, falls nein
	 */
	public static boolean readBoolean(String label) {
		String line = null;
		boolean finished = false;
		boolean ret = false;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			while(!finished) {
				System.out.print(label+" [j/n]: ");
				line = stdin.readLine().toLowerCase();
				
				if(line.equals("j") || line.equals("ja")) {
					ret = true;
					finished = true;
				} else if(line.equals("n") || line.equals("nein")) {
					ret = false;
					finished = true;
				} else {
					System.err.println("Bitte geben Sie ja oder nein bzw. j oder n ein!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
