package logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class MovieServiceBase {

	public void print(DBCollection collection, int limit) {
		print(collection.find(), limit);
	}

	public static void print(DBCursor cursor, int limit) {
		cursor.limit(limit);
		for (DBObject dbo : cursor)
			System.out.println(dbo);
	}

	public static String[] extract(DBCursor cursor, String fieldName) {
		String[] result = new String[cursor.size()];
		int pos = 0;
		for (DBObject obj : cursor) {
			result[pos] = obj.get(fieldName).toString();
			pos++;
		}
		return result;
	}

	public List<DBObject> loadMovies(String fileName) {
		String[] names = new String[] { "title", "year", "rating", "votes", "genre" };
		List<DBObject> results = new LinkedList<DBObject>();
		Scanner scanner = new Scanner(MovieService.class.getResourceAsStream(fileName));
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			BasicDBObject dbo = new BasicDBObject();
			String[] fields = line.split("\t");
			for (int i = 0; i < fields.length - 1; i++) {
				if (i == 2) {
					dbo.append(names[i], Double.valueOf(fields[i]));
				} else if (i == 3) {
					dbo.append(names[i], Integer.valueOf(fields[i]));
				} else {
					dbo.append(names[i], fields[i]);
				}
			}
			dbo.append(names[fields.length - 1], fields[fields.length - 1].split(","));
			results.add(dbo);
		}
		scanner.close();
		return results;
	}
	
	public static void loadJSON(String fileName, DBCollection col) {
		Scanner scanner = new Scanner(MovieService.class.getResourceAsStream(fileName));
        while (scanner.hasNextLine()) {
        	String line = scanner.nextLine();
        	col.insert((DBObject) JSON.parse(line));
        }
        scanner.close();
	}
	
	public static List<DBObject> loadMovies_megaNice(String fileName) {
        String[] names = new String[] { "_id", "title", "year", "rating",
                "votes", "runtime", "genre", "actors", "releases", "plot",
                "movie" };
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<DBObject> results = new ArrayList<DBObject>(100000);
        Scanner scanner = new Scanner(MovieService.class.getResourceAsStream(fileName));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            BasicDBObject dbo = new BasicDBObject();
            String[] fields = line.split("\t");
            for (int i = 0; i < fields.length - 1; i++) {
                if (fields[i] == null || fields[i].length() == 0) {
                    dbo.append(names[i], "");
                } else if (i == 3) {// rating
                    dbo.append(names[i], Double.valueOf(fields[i]));
                } else if (i == 4) {// votes
                    dbo.append(names[i], Integer.valueOf(fields[i]));
                } else if (i == 5) {// runtime
                    dbo.append(names[i], fields[i]);
                } else if (i == 6) {// genre
                    dbo.append(names[i], fields[i].split(Pattern.quote("|")));
                } else if (i == 7) {// actors
                    dbo.append(names[i], fields[i].split(Pattern.quote("|")));
                } else if (i == 8) {// releases
                    List<BasicDBObject> releases = new LinkedList<BasicDBObject>();
                    for(String release : fields[i].split(Pattern.quote("|"))) {
                    	String[] parts = release.split(":");
                    	if(parts.length > 1) {
	                    	try {
	                    		releases.add(new BasicDBObject("country", parts[0]).append("date", dateFormat.parse(parts[1])));
							} catch (ParseException e) {
								releases.add(new BasicDBObject("country", parts[0]).append("date", parts[1]));
							}
                    	} else {
                    		releases.add(new BasicDBObject("date", parts[0]));
                    	}
                    }
                    dbo.append(names[i], releases);
                } else if (i == 9) {// plots
                    dbo.append(names[i], fields[i].replaceAll("       ", "\t")
                            .replaceAll("        ", "\n"));
                } else if (i == 10) {// movie
                    dbo.append(names[i], Boolean.parseBoolean(fields[i]));
                } else {
                    dbo.append(names[i], fields[i]);
                }
            }
            dbo.append(names[fields.length - 1],
                    fields[fields.length - 1].split(","));
            //Limit the amount of Movies by dropping those with < 10 Votes
            if( dbo.get("votes")!= null && !dbo.get("votes").equals("") && dbo.getInt("votes") > 5 ) {
            	results.add(dbo);
            } else {
            	//results.add(dbo);
            }
        }
        scanner.close();
        return results;
    }
	

	protected void insertDummyData(DBCollection movies, int inserts) {
		long start = System.nanoTime();
		for (int i = 0; i < inserts; i++) {
			BasicDBObject doc = new BasicDBObject("Name", "Indiana Jones " + i).append("Director", "George Lucas")
					.append("Year", 1981);
			movies.insert(doc);
		}
		long stop = System.nanoTime();
		System.out.println("Elapsed Time for " + inserts + " objects: " + (stop - start) / 1000000 + " ms");
	}
	

}
