package ro.bogdan.synology;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ro.bogdan.synology.engine.beans.Searchable;
import ro.bogdan.synology.search.Search;
import ro.bogdan.synology.search.impl.Filelist;

@WebServlet("/searchSeries")
public class SearchSeries extends HttpServlet {
    private static final long serialVersionUID = 1L;

    Search searchEngine = new Filelist();

    private final Logger logger = Logger.getLogger(SearchSeries.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Searchable searchable = new Searchable();

        searchable.setSleepTime(0L);

        String query = req.getParameter("query");
        String serie = req.getParameter("serie");
        String firstEpisode = req.getParameter("firstEpisode");
        String lastEpisode = req.getParameter("lastEpisode");
        String latestEpisodes = req.getParameter("latestEpisodes");
        String category = req.getParameter("category");
        String quality = req.getParameter("quality");

        if (query != null) {
            query = query.replaceAll("\\s", "\\.");
            searchable.setQuery(query);
        } else {
            return;
        }
        if (serie != null && serie.trim().length() > 0) {
            serie = serie.trim();
            searchable.setSeariesNumber(Integer.parseInt(serie));
        } else {
            return;
        }

        if (firstEpisode != null && firstEpisode.trim().length() > 0) {
            searchable.setStartEpisode(Integer.parseInt(firstEpisode.trim()));
        }
        if (lastEpisode != null && lastEpisode.trim().length() > 0) {
            searchable.setStopEpisode(Integer.parseInt(lastEpisode.trim()));
        }

        if (req.getParameter("ignoreMissing") != null && req.getParameter("ignoreMissing").equals("on")) {
            searchable.setIgnoreMissing(true);

        }
        if (category != null && category.trim().length() > 0) {
            searchable.setCategory(category.trim());
        }
        if (req.getParameter("onlyFree") != null && req.getParameter("onlyFree").equals("on")) {
            searchable.setOnlyFreeDownloads(true);
        }
        if (req.getParameter("onlyAgregated") != null && req.getParameter("onlyAgregated").equals("on")) {
            searchable.setOnlyAgregated(true);
        }
        if (req.getParameter("latestEpisode") != null && req.getParameter("latestEpisode").equals("on")) {
            searchable.setLatestEpisode(true);
        }
        if (latestEpisodes != null && latestEpisodes.trim().length() > 0) {
            searchable.setLatestEpisodes(Integer.parseInt(latestEpisodes.trim()));
        }

        if (quality != null && quality.trim().length() > 0) {
            searchable.setQuality(quality);
        }

        Object[] result = searchEngine.search(searchable);

        List<URL> links = (List<URL>) result[0];
        String error = (String) result[1];

        resp.getOutputStream().print(searchable.toString() + "<br\\><br\\><br\\><br\\>" + (links != null ? links : error));
        resp.getOutputStream().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
