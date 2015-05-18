package ro.bogdan.synology;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet("/searchSeries")
public class SearchSeries extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Logger logger = Logger.getLogger(SearchSeries.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String query = req.getParameter("query");
		if (query != null) {
			query = query.replaceAll("\\s", "\\.");
		}
		String serie = req.getParameter("serie");
		if(serie!=null){
			serie = serie.trim();
		}
		String firstEpisode = req.getParameter("firstEpisode");
		String lastEpisode = req.getParameter("lastEpisode");
		String ignoreMissing = req.getParameter("ignoreMissing");
		String onlyFree = req.getParameter("onlyFree");
		String onlyAgregated = req.getParameter("onlyAgregated");
		String latestEpisode = req.getParameter("latestEpisode");
		String latestEpisodes = req.getParameter("latestEpisodes");

		String lg = "Params: query:" + query + " serie:" + serie + " firstEpisode:" + firstEpisode + " lastEpisode:"
		        + lastEpisode + " ignore missing:" + ignoreMissing + " onlyFree:" + onlyFree + " onlyAgregated:" + onlyAgregated
		        + " latestEpisode:" + latestEpisode + " latestEpisodes:" + latestEpisodes;
		// logger.info();
		
		resp.getOutputStream().print(lg);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
