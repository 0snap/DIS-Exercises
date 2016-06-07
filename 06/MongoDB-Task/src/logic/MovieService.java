package logic;

import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.json.DataObjectFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;

/**
 * This class holds the data/backend logic for the Movie Web-App. It uses
 * MongoDB to perform different kinds of queries.
 */
public class MovieService extends MovieServiceBase {

	private MongoClient mongo;
	private DB db;
	private DBCollection movies;
	private DBCollection tweets;
	private GridFS fs;

	/**
	 * Create a new MovieService by connecting to MongoDB.
	 */
	public MovieService() {
		// Connect to local machine
		try {
			// TODO: connect to MongoDB
			mongo = null;
		} catch (Exception e) {
			System.out.println("No MongoDB server running on localhost");
		}
		// TODO: Select database "imdb"
		db = null;
		// Create a GriFS FileSystem Object using the db
		fs = new GridFS(db);
		// See this method on how to use GridFS
		createSampleImage();
		// Print the name of all collections in that database
		printCollections();
		// Enable Full Text Search
		enableTextSearch();

		// TODO: Take "movies" and "tweets" collection
		movies = null;
		tweets = null;

		// If movie database isn't filled (has less than 10000 documents) delete
		// everything and fill it
		if (movies.count() < 10000) {
			createMovieData();
		}

		// TODO: Index Movie attributes "title", "rating", "votes", "tweets.coordinates"

		tweets.ensureIndex(new BasicDBObject("coordinates", "2dsphere"));
	}

	/**
	 * Output all Collections known to the database.
	 */
	public void printCollections() {
		Set<String> colls = db.getCollectionNames();
		System.out.println("Connected to MongoDB\nCollections in imdb db: " + colls.size());
		for (String s : colls) {
			System.out.println("- " + s);
		}
	}

	/**
	 * Find a movie by title. Only return one match using findOne().
	 * 
	 * @param title
	 *            the title to query
	 * @return the matching DBObject
	 */
	public DBObject findMovieByTitle(String title) {
		// TODO: implement
		DBObject result = null;
		return result;
	}

	/**
	 * Find all movies that have at least one tweet in their "tweets" array
	 * which does have the "coordinates" attribute
	 * 
	 * @return the DBCursor for the query
	 */
	public DBCursor getViewableMovies() {
		DBCursor results = movies.find(new BasicDBObject("tweets.coordinates", new BasicDBObject("$exists", true)));
		return results;
	}

	/**
	 * Find the best movies, i.e. those that have a rating greater minRating and
	 * at least minVotes votes. Return the results sorted by their descending
	 * (-1) rating.
	 * 
	 * @param minVotes
	 *            number of votes required at least
	 * @param minRating
	 *            rating required at least
	 * @param limit
	 *            maximum number of records to be returned
	 * @return the DBCursor for the query
	 */
	public DBCursor getBestMovies(int minVotes, double minRating, int limit) {
		// TODO: implement
		DBCursor best = null;
		return best;
	}

	/**
	 * Find movies by genres. To achieve that, find all movies whose "genre"
	 * property contains all of the specified genres.
	 * 
	 * @param genreList
	 *            comma-separated genres
	 * @param limit
	 *            maximum number of records to be returned
	 * @return the DBCursor for the query
	 */
	public DBCursor getByGenre(String genreList, int limit) {
		String[] genres = genreList.split(",");
		//TODO: implement
		DBCursor result = null;
		return result;
	}

	/**
	 * Find movies by prefix, i.e. find movies whose "title" property begins
	 * with the given prefix. Use a regular expression similar to the
	 * {@link #suggest(String, int)} method. This method is used to display
	 * search results while typing.
	 * 
	 * @param titlePrefix
	 *            the prefix entered by the user
	 * @param limit
	 *            maximum number of records to be returned
	 * @return the DBCursor for the query
	 */
	public DBCursor searchByPrefix(String titlePrefix, int limit) {
		//TODO: implement
		DBObject prefixQuery = null;
		return movies.find(prefixQuery).limit(limit);
	}

	/**
	 * Suggest movies based on a title prefix. This is used for the search
	 * typeahead feature. The suggestions rely on a regular expression of the
	 * form:
	 * 
	 * <pre>
	 * ^prefix.*
	 * </pre>
	 * 
	 * Anchoring the prefix to the begin using "^" ensures the efficiency of the
	 * query. Perform a projection to the property "title", so no unnecessary
	 * data is transferred from MongoDB.
	 * 
	 * @param prefix
	 * @param limit
	 *            maximum number of records to be returned
	 * @return the DBCursor for the query
	 */
	public DBCursor suggest(String prefix, int limit) {
		DBObject query = new BasicDBObject("title", Pattern.compile("^" + prefix + ".*"));
		DBObject projection = new BasicDBObject("title", true);
		DBCursor suggestions = movies.find(query, projection).limit(limit);
		return suggestions;
	}

	/**
	 * Find all movies that have a "tweets" attribute, i.e. that were at least
	 * once subject of a tweet.
	 * 
	 * @return the DBCursor for the query
	 */
	public DBCursor getTweetedMovies() {
		//TODO: implement
		DBCursor results = null;
		return results;
	}

	/**
	 * Saves a changed comment for a movie, which is displayed in the movies
	 * details under the search tab.
	 * 
	 * @param id
	 *            the movie _id of the move where the comment was set.
	 * @param comment
	 *            the comment to save
	 */
	public void saveMovieComment(String id, String comment) {
		//TODO: implement

	}

	/**
	 * Find all tweets that are geotagged, i.e. have a "coordinates" attribute
	 * that is neither null nor non-existent.
	 * 
	 * @param limit
	 *            maximum number of records to be returned
	 * @return the DBCursor for the query
	 */
	public DBCursor getGeotaggedTweets(int limit) {
		//TODO : implement
		DBCursor result = null;
		return result;
	}

	/**
	 * Find all tweets that are geotagged, i.e. that have a "coordinates"
	 * attribute. Make sure that the "coordinates" attribute is indexed, so that
	 * this query is efficient. Furthermore perform a projection to the
	 * attributes "text", "movie", "user.name", "coordinates". The result is
	 * used to display markers on the map and the projection ensures, that no
	 * unnecessary data is transfered. The tweets should be order by the
	 * descending (-1) "_id" property so, the newest tweets are returned first.
	 * 
	 * @return the DBCursor for the query
	 */
	public DBCursor getTaggedTweets() {
		//TODO : implement
		DBObject projection = null;
		DBObject query = null;
		DBObject sort = null;
		DBCursor results = tweets.find(query, projection).sort(sort);
		return results;
	}

	/**
	 * Save a tweet emitted by the Twitter Stream. The tweet has to be saved
	 * twice: 1) in the movie document that has a title that matches the keyword
	 * using the "tweets" list of each movie. 2) in the separate tweets
	 * collection which stores the JSON tweets, as outputted by the Twitter REST
	 * API.<br>
	 * Add the matching movie to the tweets in the tweet collection by adding a
	 * new field "movie" to it. Also remove the "coordinates" property of the
	 * raw tweets where that property is null. This ensure that we can uses it
	 * for geospatial queries.
	 * 
	 * @param movie
	 *            the name of the movie the tweet corresponds to
	 * @param status
	 *            the tweet
	 */
	public void saveTweet(String movie, Status status) {
		// Extract information from tweet
		String user = status.getUser().getName();
		String text = status.getText();
		Date date = status.getCreatedAt();
		boolean retweet = status.isRetweet();

		// Output the Tweet
		System.out.format("%-20s %-20s %-140s%n", movie, user, text.replace("\n", " "));

		// Get raw JSON Tweet
		String rawJson = DataObjectFactory.getRawJSON(status);
		DBObject rawTweet = (DBObject) JSON.parse(rawJson);
		rawTweet.put("movie", movie);
		if (status.getGeoLocation() == null)
			rawTweet.removeField("coordinates");

		BasicDBObject tweet = new BasicDBObject().append("user", user).append("text", text).append("retweet", retweet)
				.append("date", date);

		// Add coordinates if the tweet is geotagged
		if (status.getGeoLocation() != null) {
			Double lat = status.getGeoLocation().getLatitude();
			Double lng = status.getGeoLocation().getLongitude();
			tweet.append("coordinates", new Double[] { lat, lng });
		} else if (status.getPlace() != null && status.getPlace().getBoundingBoxCoordinates() != null) {
			// If the tweet isn't explicitly tagged, try to take the user's
			// position
			GeoLocation gl = status.getPlace().getBoundingBoxCoordinates()[0][0];
			Double lat = gl.getLatitude();
			Double lng = gl.getLongitude();
			tweet.append("coordinates", new Double[] { lat, lng });
		}

		// Insert Raw Tweet
		tweets.insert(rawTweet);
		// Find matching Movie(s) and append Tweet
		movies.update(new BasicDBObject("title", movie),
				new BasicDBObject("$push", new BasicDBObject("tweets", tweet)), false, true);
	}

	/**
	 * Find all Movies, that have tweets that contain a given keyword. Solve
	 * this using a Regular Expression like this:
	 * 
	 * <pre>
	 * .*keyword.*
	 * </pre>
	 * 
	 * Regular expressions can be created in Java using the Pattern.compile
	 * method. To make the search case insensitive specify the according
	 * parameter for the pattern.<br>
	 * 
	 * <b>Remember</b>: This is an example of a powerful query that should not
	 * be done in practice. Wildcard regular expression queries always have to
	 * scan the full index which is very costly. A full text search is far more
	 * efficient (see {@link #searchTweets(String)} for an example) and would be
	 * preferred in practice.
	 * 
	 * @param keyword
	 *            the keyword to search
	 * @param limit
	 *            maximum number of records to be returned
	 * @return the DBCursor for the query
	 */
	public DBCursor getByTweetsKeywordRegex(String keyword, int limit) {
		//TODO : implement
		DBCursor result = null;
		return result;
	}

	/**
	 * Does Full Text Search (FTS) on Tweets. Docs about MongoDB FTS:
	 * http://docs.mongodb.org/manual/core/text-search/#text-search-text-command<br>
	 * 
	 * FTS search is powerful new feature of MongoDB. It allow Queries similar
	 * to those performed in Google, including negation, Stemming, Stop Words
	 * and Wildcards. To Do FTS first ensure an index of type "text" on the
	 * tweet property "text". The actual search is a query of this form:
	 * 
	 * <pre>
	 * {
	 * 	"text" : *mytextcollection*,
	 *  "search" : *search query*
	 * }
	 * </pre>
	 * 
	 * @param query
	 *            the search query
	 * @return a List of Objects returned by the FTS query
	 */
	public List<DBObject> searchTweets(String query) {
		// Create a text index on the "text" property of tweets
		tweets.ensureIndex(new BasicDBObject("text", "text").append("user.name", "text"));

		DBObject search = new BasicDBObject();
		search.put("text", "tweets");
		search.put("search", query);
		CommandResult commandResult = db.command(search);
		BasicDBList results = (BasicDBList) commandResult.get("results");
		List<DBObject> found = new LinkedList<DBObject>();
		for (Object o : results) {
			DBObject dbo = (DBObject) ((DBObject) o).get("obj");
			found.add(dbo);
		}
		return found;
	}

	/**
	 * Find the newest tweets, with respect to their insertion order. To achieve
	 * this, order the results by their "_id" attribute, descending (-1).
	 * 
	 * @param limit
	 *            maximum number of records to be returned
	 * @return the DBCursor for the query
	 */
	public DBCursor getNewestTweets(int limit) {
		//TODO : implement
		DBCursor result = null;
		return result;
	}

	/**
	 * Do a geospatial query to find all tweets in the given radius for a
	 * specified point. The syntax of such a query is the following:
	 * 
	 * <pre>
	 * db.*collection*.find({ *location field* :
	 *                          { $near :
	 *                             { $geometry :
	 *                                 { type : "Point" ,
	 *                                   coordinates : [ *lng* , *lat*] } },
	 *                               $maxDistance : *distance in meters*
	 *        } } )
	 * </pre>
	 * 
	 * Details on geospatial querys are documented online:
	 * http://docs.mongodb.org/manual/reference/operator/near/#op._S_near Note
	 * that the Radius parameter is given in km while the query takes a distance
	 * in meters.
	 * 
	 * @param lat
	 *            the latitude of the center point
	 * @param lng
	 *            the longitude of the center point
	 * @param radiusKm
	 *            the radius to search in
	 * @return
	 */
	public DBCursor getTweetsNear(double lat, double lng, int radiusKm) {
		DBObject pointQuery = new BasicDBObject("coordinates", new BasicDBObject("$near",
				new BasicDBObject("$geometry", new BasicDBObject("type", "Point").append("coordinates", new Double[] {
						lng, lat })).append("$maxDistance", radiusKm * 1000)));
		return tweets.find(pointQuery);
	}

	// GridFS Interaction

	/**
	 * Load the sample.png and store it in the database. The content type has to
	 * be set, so the file can be retrieved and displayed by web clients.
	 */
	public void createSampleImage() {
		// Create file
		GridFSInputFile file = fs.createFile(MovieService.class.getResourceAsStream("/data/sample.png"));
		// Set file name and content type
		file.setContentType("image/png");
		file.setFilename("sample.png");
		file.save();
	}

	/**
	 * Retrieves a file from GridFS. If the file is not found (==null) the file
	 * with the name "sample.png" should be loaded instead.
	 * 
	 * @param name
	 *            the name of the file
	 * @return The retrieved GridFS File
	 */
	public GridFSDBFile getFile(String name) {
		//TODO: implement
		GridFSDBFile file = null;
		return file;
	}

	/**
	 * Saves a file to GridFS. The file has the given name and is files using
	 * the provided InputStream. The given Content-Type has to be set on the
	 * file.
	 * 
	 * @param name
	 * @param inputStream
	 * @param contentType
	 */
	public void saveFile(String name, InputStream inputStream, String contentType) {
		GridFSInputFile gFile = null;
		//Remove old versions
		fs.remove(name);
		//TODO: implement
	}

	// Given Helper Functions:

	/**
	 * Fill the database using the data files in the data directory
	 */
	public void createMovieData() {
		clearDatabase();

		// Load a CSV file of IMDB titles into the database
		// List<DBObject> data =
		// loadMovies_megaNice("/data/imdb_megaNice-full.csv");
		// Smaller Dataset with less properties:
		// List<DBObject> data = loadMovies("/data/imdb_nicer-full.csv");
		loadJSON("/data/movies.json", movies);
		loadJSON("/data/tweets.json", tweets);
	}

	/**
	 * Get the movie collection
	 * 
	 * @return the movie collection
	 */
	public DBCollection getMovies() {
		return movies;
	}

	/**
	 * Get the tweets collection
	 * 
	 * @return the tweets collection
	 */
	public DBCollection getTweets() {
		return tweets;
	}

	/**
	 * Delete all documents from both the tweets and the movie collection
	 */
	public void clearDatabase() {
		movies.remove(new BasicDBObject());
		tweets.remove(new BasicDBObject());
	}

	/**
	 * Bulk insert tweets
	 * 
	 * @param movie
	 * @param stati
	 */
	public void saveTweets(String movie, List<Status> stati) {
		for (Status status : stati) {
			saveTweet(movie, status);
		}
	}

	/**
	 * Enables Full Text Search.
	 */
	protected void enableTextSearch() {
		DB admin = mongo.getDB("admin");
		DBObject cmd = new BasicDBObject();
		cmd.put("setParameter", 1);
		cmd.put("textSearchEnabled", true);
		admin.command(cmd);
	}

}
