package ro.bogdan.synology.search.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import ro.bogdan.synology.engine.beans.Searchable;
import ro.bogdan.synology.search.Search;
import ro.bogdan.synology.utils.Utils;

public class Filelist implements Search {

    private String FILELIST_URL = "http://filelist.ro/";
    private String BROWSE_SERIES = "browse.php?searchin=1&sort=0&cat=21&search=";
    private String BROWSE_MOVIE = "browse.php?searchin=1&sort=0&search=";
    private static final int DEFAULT_ZFILL_LENGTH = 2;
    private static Logger log = Logger.getLogger(Filelist.class);

    public static void main(String[] args) {
        Searchable s = new Searchable();
        s.setCategory("serie");
        s.setIgnoreMissing(true);
        s.setLatestEpisode(true);
        s.setLatestEpisodes(5);
        s.setOnlyAgregated(true);
        s.setOnlyFreeDownloads(true);
        s.setQuality("hd");
        s.setQuery("the.big.bang");
        s.setSeariesNumber(9);
        s.setStartEpisode(1);
        s.setStopEpisode(10);
        Filelist f = new Filelist();
        f.search(s);
    }

    @Override
    public List<URL> search(Searchable search) {
        List<SearchedItem> result = new ArrayList<Filelist.SearchedItem>();
        try {
            String urlString = FILELIST_URL;
            if (search.getCategory().equals("serie")) {
                urlString += BROWSE_SERIES;
            } else if (search.getCategory().equals("movie")) {
                urlString += BROWSE_MOVIE;
            }
            boolean end = false;
            int i = 0;
            while (!end && i < 10) {
                URL url = grabNextPage(urlString + search.getQuery(), i);
                String content = Utils.download(url);
                if (content == null) {
                    break;
                }
                List<SearchedItem> searched = crawlForLinks(content);
                if (searched != null && searched.size() > 0) {
                    result.addAll(searched);
                } else {
                    end = true;
                }
                i++;

                // need this to simulate human behavior
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                }
            }
            for (SearchedItem link : result) {
                System.out.println(link);
            }

        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        List<URL> filtered = filterResults(result, search);
        return filtered;
    }

    private List<URL> filterResults(List<SearchedItem> result, Searchable search) {

        List<SearchedItem> filtered = new ArrayList<SearchedItem>();

        // let's filter a bit the results
        for (SearchedItem searchedItem : result) {

            if (search.getOnlyAgregated()) {
                if (!searchedItem.agregated) {
                    continue;
                }
            }

            if (search.getOnlyFreeDownloads())
                if (!searchedItem.free) {
                    continue;
                }

            if (!search.getQuality().equals(searchedItem.quality)) {
                continue;
            }

            // block
            boolean in = false;
            for (Integer serie : searchedItem.season) {
                if (search.getSeariesNumber().equals(serie)) {
                    in = true;
                    break;
                }
            }
            if (!in) {
                continue;
            }
            // end block

            if (!(search.getStartEpisode() <= searchedItem.episode && searchedItem.episode <= search.getStopEpisode())) {
                continue;
            }
            filtered.add(searchedItem);
        }

        // get latest EP
        Integer latestEp = filtered.get(0).episode;
        for (SearchedItem searchedItem : filtered) {
            if (searchedItem.episode > latestEp) {
                latestEp = searchedItem.episode;
            }
        }
        
        List<SearchedItem> filtered2 = new ArrayList<SearchedItem>();
        if (search.getLatestEpisodes()!=null) {
            for (SearchedItem searchedItem : filtered) {
                if (((latestEp-search.getLatestEpisodes())<=searchedItem.episode) && (searchedItem.episode <= latestEp)) {
                    filtered2.add(searchedItem);
                }
            }
            
        }else if (search.getLatestEpisode()) {
            for (SearchedItem searchedItem : filtered) {
                if (searchedItem.episode == latestEp) {
                    filtered2.add(searchedItem);
                }
            }
        }

        return null;
    }

    private URL grabNextPage(String url, int counter) {
        String q = url + "&page=" + counter;
        URL result = null;
        try {
            result = new URL(q);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    protected List<SearchedItem> crawlForLinks(String page) {
        String[] lines = page.split(System.getProperty("line.separator"));
        List<SearchedItem> searches = new ArrayList<SearchedItem>();
        String content = null;
        for (String line : lines) {
            if (line.contains("torrentrow")) {
                content = line;
                break;
            }
            if (line.contains("Nu s-a gasit nimic!")) {
                log.info("Finished searching!");
                return null;
            }
        }
        lines = content.split("torrentrow");
        for (String line : lines) {
            if (!line.contains("torrenttable")) {
                continue;
            }
            SearchedItem searched = new SearchedItem();
            int d1 = line.indexOf("download.php");
            int d2 = line.indexOf(".torrent", d1);
            String url = line.substring(d1, d2 + 8);
            if (line.contains("freeleech")) {
                searched.free = true;
            }
            try {
                searched.url = new URL(FILELIST_URL + url);
            } catch (MalformedURLException e) {
                log.error(e.getMessage(), e);
            }
            searched.name = grabName(url);
            searched.season = grabSeason(url);
            searched.episode = grabEpisode(url);
            searched.quality = grabQuality(url);
            if (searched.season != null && searched.episode == null) {
                searched.agregated = true;
            }
            searches.add(searched);
        }
        return searches;
    }

    private String grabName(String url) {
        String name = url.substring(url.indexOf("file")).substring(5);
        name = name.replaceFirst("\\.[Ss]\\d{1,2}.*", "").toLowerCase();
        return name;
    }

    private Integer[] grabSeason(String url) {
        Pattern pattern = Pattern.compile("[sS]\\d{1,2}");
        Matcher matcher = pattern.matcher(url);
        Integer[] seasons = new Integer[0];

        while (matcher.find()) {
            String season = matcher.group();
            Integer[] seasons2 = new Integer[seasons.length + 1];
            System.arraycopy(seasons, 0, seasons2, 0, seasons.length);
            seasons2[seasons.length] = Integer.parseInt(season.substring(1));
            seasons = seasons2;
        }
        return seasons;
    }

    private Integer grabEpisode(String url) {
        Pattern pattern = Pattern.compile("[eE]\\d{1,2}");
        Matcher matcher = pattern.matcher(url);
        Integer ep = null;
        while (matcher.find()) {
            String episode = matcher.group();
            ep = Integer.parseInt(episode.substring(1));
        }
        return ep;
    }

    private String grabQuality(String url) {

        if (url.indexOf("1080") >= 0) {
            return "full_hd";
        }
        if (url.indexOf("740") >= 0) {
            return "half_hd";
        }
        if (url.indexOf("HDTV") >= 0) {
            return "half_hd";
        }
        return "unknown";
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

    private class SearchedItem {
        public URL url = null;
        public boolean agregated;
        public Integer episode;
        public Integer[] season;
        public boolean free;
        public String quality;
        public String name;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            // sb.append(" Url: ").append(url);
            sb.append(" Name: ").append(name);
            sb.append(" Season: ");
            for (Integer integer : season) {
                sb.append(integer).append(", ");
            }
            sb.append(" Episode: ").append(episode);
            sb.append(" Free: ").append(free);
            sb.append(" Quality: ").append(quality);
            sb.append(" Agregated: ").append(agregated);
            return sb.toString();

        }
    }

}
