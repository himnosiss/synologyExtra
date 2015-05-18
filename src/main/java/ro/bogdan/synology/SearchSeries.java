package ro.bogdan.synology;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ro.bogdan.synology.search.Search;

@WebServlet("/searchSeries")
public class SearchSeries extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_ZFILL_LENGTH = 2;

	@Inject
	Search searchEngine;

	private final Logger logger = Logger.getLogger(SearchSeries.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String query = req.getParameter("query");
		String serie = req.getParameter("serie");
		String firstEpisode = req.getParameter("firstEpisode");
		String lastEpisode = req.getParameter("lastEpisode");
		Boolean ignoreMissing = false;
		Boolean onlyFree = false;
		Boolean onlyAgregated = false;
		Boolean latestEpisode = true;
		String latestEpisodes = req.getParameter("latestEpisodes");

		if (query != null) {
			query = query.replaceAll("\\s", "\\.");
		} else {
			return;
		}
		if (serie != null) {
			serie = serie.trim();
			serie = zfill(serie, DEFAULT_ZFILL_LENGTH);
		} else {
			return;
		}
		if (firstEpisode != null && !firstEpisode.equals("")) {
			firstEpisode = zfill(firstEpisode, DEFAULT_ZFILL_LENGTH);
		}
		if (lastEpisode != null && !lastEpisode.equals("")) {
			lastEpisode = zfill(lastEpisode, DEFAULT_ZFILL_LENGTH);
		}

		if (req.getParameter("ignoreMissing") != null && req.getParameter("ignoreMissing").equals("on")) {
			ignoreMissing = true;
		}
		if (req.getParameter("onlyFree") != null && req.getParameter("onlyFree").equals("on")) {
			onlyFree = true;
		}
		if (req.getParameter("onlyAgregated") != null && req.getParameter("onlyAgregated").equals("on")) {
			onlyAgregated = true;
		}
		if (req.getParameter("latestEpisode") != null && req.getParameter("latestEpisode").equals("on")) {
			latestEpisode = true;
		}

		String lg = "Params: query:" + query + " serie:" + serie + " firstEpisode:" + firstEpisode + " lastEpisode:"
		        + lastEpisode + " ignore missing:" + ignoreMissing + " onlyFree:" + onlyFree + " onlyAgregated:" + onlyAgregated
		        + " latestEpisode:" + latestEpisode + " latestEpisodes:" + latestEpisodes;

		String x = searchEngine.search("sdsds");
		resp.getOutputStream().print(lg + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + x);
	}

	private String zfill(String serie, int length) {
		if (serie.length() >= length) {
			return serie;
		}
		char[] result = new char[length];
		char[] serieArray = serie.toCharArray();
		for (int i = 0; i < result.length; i++) {
			if (i < (result.length - serieArray.length)) {
				result[i] = '0';
			} else {
				result[i] = serieArray[i - serieArray.length];
			}
		}
		return String.valueOf(result);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
