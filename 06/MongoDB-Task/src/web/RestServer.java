package web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.MovieService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import twitter.MovieTweetHandler;
import twitter.TweetStream;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.util.JSON;

public class RestServer {

	/**
	 * A very simple Jetty-based REST server that serves static content and
	 * handles REST API requests.
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server(5900);
		final MovieService ms = new MovieService();

		// Serve static files
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "map.html" });
		resource_handler.setBaseResource(Resource.newClassPathResource("/web"));

		// Define Resources and Actions
		ContextHandler tweetedMovies = handle("/movie_tweets", new MongoHandler() {
			@Override
			public Object getData(HttpServletRequest request) {
				int limit = 500;
				String limitParam = request.getParameter("limit");
				if (limitParam != null)
					limit = Integer.parseInt(limitParam);
				return ms.getTaggedTweets().limit(limit);
			}
		});

		ContextHandler searchSuggestions = handle("/suggestions", new MongoHandler() {
			@Override
			public Object getData(HttpServletRequest request) {
				String query = request.getParameter("query");
				return MovieService.extract(ms.suggest(query, 8), "title");
			}
		});
		
		ContextHandler comment = handle("/comment", new MongoHandler() {
			@Override
			public Object getData(HttpServletRequest request) {
				if (request.getMethod().equals("POST")) {
					String id = request.getParameter("id");
					String comment = request.getParameter("comment");
					System.out.println(id + ", " +comment);
					ms.saveMovieComment(id, comment);
				}
				return new BasicDBObject("ok", true);
			}
		});

		ContextHandler stream = handle("/stream", new MongoHandler() {
			@Override
			public Object getData(HttpServletRequest request) {
				if (request.getMethod().equals("POST")) {
					final Integer limit = Integer.valueOf(request.getParameter("limit"));
					String onlyTagged = request.getParameter("tagged");
					final boolean tagged;
					if(onlyTagged != null)
						tagged = Boolean.parseBoolean(onlyTagged);
					else
						tagged = false;
					String keywords = request.getParameter("keywords");
					String[] titles = null;
					if (keywords == null) {
						titles = MovieService.extract(ms.getBestMovies(50000, 5, 1000), "title");
					} else {
						titles = keywords.split(",");
					}
					final String[] titlesFinal = titles;
					// Run asynchronously in order not to get blocked.
					Thread thread = new Thread() {
						public void run() {
							TweetStream ts = new TweetStream();
							MovieTweetHandler handler = new MovieTweetHandler(ms, limit, titlesFinal);
							handler.setIgnoreUntagged(tagged);
							ts.listenToStream(handler);
						}
					};
					thread.start();
				}
				return new BasicDBObject("ok", true);
			}
		});

		ContextHandler movieSearch = handle("/movies", new MongoHandler() {
			@Override
			public Object getData(HttpServletRequest request) {
				String title = request.getParameter("title");
				//Prefix / Title Search
				if (title != null) {
					Boolean exact = Boolean.parseBoolean(request.getParameter("exact"));
					if (exact) {
						return ms.findMovieByTitle(title);
					} else {
						DBCursor cursor = ms.searchByPrefix(title, 1);
						//Extract a single object
						if(cursor.hasNext())
							return cursor.next();
						else
							return null;
					}
				}
				//handle different kinds of queries
				else {
					String query = request.getParameter("query");
					String type = request.getParameter("type");
					if (type == null)
						type = "";
					Integer limit = Integer.valueOf(request.getParameter("limit"));
					if (type.equals("rating-greater"))
						return ms.getBestMovies(1000, Double.parseDouble(query), limit);
					else if (type.equals("genre"))
						return ms.getByGenre(query, limit);
					else if (type.equals("geo"))
						return ms.getByTweetsKeywordRegex(query, limit);
					else if (type.equals("tweeted"))
						return ms.getTweetedMovies().limit(limit);
					else
						return ms.searchByPrefix(query, limit);
				}
			}
		});

		ContextHandler tweetSearch = handle("/tweets", new MongoHandler() {
			@Override
			public Object getData(HttpServletRequest request) {
				String query = request.getParameter("query");
				System.out.println(query);
				String type = request.getParameter("type");
				if (type == null)
					type = "";
				Integer limit = Integer.valueOf(request.getParameter("limit"));
				limit = limit > 2000 ? 2000 : limit;
				if (type.equals("geo"))
					return ms.getGeotaggedTweets(limit);
				else if (type.equals("fts")) {
					return ms.searchTweets(query);
				}
				else if (type.equals("near")) {
					String[] parts = query.split(",");
					return ms.getTweetsNear(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),
							Integer.parseInt(parts[2]));
				} else
					return ms.getNewestTweets(limit);
			}
		});
		
		ContextHandler images = handle("/images", new AbstractHandler() {
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				    throws IOException, ServletException {
				try {
				String name = request.getParameter("name");
				String url = request.getParameter("url");
				if (request.getMethod().equals("GET")) {
					//Serve from Gridfs
					if(url == null) {
						GridFSDBFile file = ms.getFile(name);
						response.setStatus(HttpServletResponse.SC_OK);
						response.setContentLength((int) file.getLength());
						response.setContentType(file.getContentType());
						file.writeTo(response.getOutputStream());
						baseRequest.setHandled(true);
						return;
					}
					//Import from IMDB
					else {
						HttpClient client = new HttpClient();
						client.start();
						ContentResponse r = client.GET(url);
						byte[] content = r.getContent();
						String contentType = r.getHeaders().get("Content-Type");
						InputStream stream = new ByteArrayInputStream(content);
						ms.saveFile(name, stream, contentType);
					}
				}
				//Upload to GridFS
				else if (request.getMethod().equals("POST")) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List<FileItem> items = upload.parseRequest(request);
					for(FileItem item : items) {
						System.out.println(item.getName());
						if(!item.isFormField()) {
						    String contentType = item.getContentType();
						    System.out.println(item.getName());
							ms.saveFile(name, item.getInputStream(), contentType);
						    return;
						}
					}
				}
				response.setContentType("application/json;charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print(JSON.serialize(new BasicDBObject("name", name)));
				baseRequest.setHandled(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		//Enable Logging
		RequestLogHandler logging = new RequestLogHandler();
		logging.setRequestLog(new NCSARequestLog());
		
		// Register all Resources
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { logging, tweetedMovies, stream, comment, movieSearch, images, searchSuggestions, tweetSearch,
				resource_handler,  new DefaultHandler() });
		server.setHandler(handlers);

		server.start();
		server.join();
	}

	/**
	 * Define a new resource
	 * 
	 * @param path
	 *            URL path of the resource
	 * @param mongoHandler
	 *            the corresponding Handler
	 * @return
	 */
	private static ContextHandler handle(String path, AbstractHandler mongoHandler) {
		ContextHandler context = new ContextHandler();
		context.setContextPath(path);
		context.setResourceBase(".");
		context.setAllowNullPathInfo(true);
		context.setClassLoader(Thread.currentThread().getContextClassLoader());
		context.setHandler(mongoHandler);
		return context;
	}

	/**
	 * An implementation of the Jetty handler. Override the abstract getData()
	 * method and return data that can be parsed as JSON.
	 */
	public abstract static class MongoHandler extends AbstractHandler {

		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			response.setContentType("application/json;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			response.getWriter().print(JSON.serialize(getData(request)));
		}

		abstract public Object getData(HttpServletRequest request);

	}

}