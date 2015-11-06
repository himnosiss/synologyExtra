package ro.bogdan.synology.search.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import ro.bogdan.synology.engine.beans.Responsable;
import ro.bogdan.synology.engine.beans.Searchable;
import ro.bogdan.synology.search.Search;
import ro.bogdan.synology.utils.GlobalContext;
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
        s.setLatestEpisode(false);
        s.setLatestEpisodes(5);
        s.setOnlyAgregated(false);
        s.setOnlyFreeDownloads(true);
        s.setQuality("full_hd");
        s.setQuery("the.big.bang");
        s.setSeariesNumber(8);
        s.setStartEpisode(0);
        s.setStopEpisode(99);
        s.setSleepTime(0L);
        Filelist f = new Filelist();
        Object[] x = f.search(s);
        for (Responsable link : (List<Responsable>) x[0]) {
            System.out.println(link);
        }
    }

    @Override
    public Object[] search(Searchable search) {

        Object[] res = new Object[2];

        List<Responsable> resp = Utils.searchInHistory(search);

        if (resp == null) {
            List<Responsable> result = new ArrayList<Responsable>();
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

//                    String content = Utils.downloadFile(url);
                     
                     
                    if (content == null) {
                        break;
                    }
                    List<Responsable> searched = crawlForLinks(content);
                    if (searched != null && searched.size() > 0) {
                        result.addAll(searched);
                    } else {
                        end = true;
                    }
                    i++;
                    // need this to simulate human behavior
                    try {
                        Thread.sleep(search.getSleepTime());
                    } catch (InterruptedException e) {
                    }
                }
            } catch (MalformedURLException e) {
                log.error(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            GlobalContext.history.put(search, result);
            res = filterResults(result, search);
        } else {
            res = filterResults(resp, search);
        }
        return res;
    }

    private Object[] filterResults(List<Responsable> result, Searchable search) {
        Object[] outcome = new Object[2];
        List<Responsable> filtered = new ArrayList<Responsable>();

        // let's filter a bit the results
        for (Responsable searchedItem : result) {

            if (search.getOnlyAgregated()) {
                if (!searchedItem.isAgregated()) {
                    continue;
                }
            }

            if (search.getOnlyFreeDownloads())
                if (!searchedItem.isFree()) {
                    continue;
                }

            if (search.getQuality() != null && !search.getQuality().equals(searchedItem.getQuality())) {
                continue;
            }

            // block
            boolean in = false;
            for (Integer serie : searchedItem.getSeason()) {
                if (search.getSeariesNumber().equals(serie)) {
                    in = true;
                    break;
                }
            }
            if (!in) {
                continue;
            }
            // end block
            filtered.add(searchedItem);
        }

        // deal with the episodes request
        Integer latestEp = 0;
        for (Responsable searchedItem : filtered) {
            if (searchedItem.getEpisode() != null && searchedItem.getEpisode() > latestEp) {
                latestEp = searchedItem.getEpisode();
            }
        }

        // searching for the latest episode or the last X episodes
        if (search.getLatestEpisode() && search.getLatestEpisodes() != null) {
            List<Responsable> filtered2 = new ArrayList<Responsable>();

            for (Responsable searchedItem : filtered) {
                if ((latestEp - search.getLatestEpisodes()) <= searchedItem.getEpisode()) {
                    filtered2.add(searchedItem);
                }
            }

            filtered2 = cleanDuplicates(filtered2);
            if (!search.getIgnoreMissing()) {
                if (filtered2.size() != search.getLatestEpisodes()) {
                    log.error("Missing episodes in the list");
                    outcome[1] = "Missing episodes in the list";
                    return outcome;
                }
            }
            outcome[0] = filtered2;
            return outcome;
        }

        if (!search.getOnlyAgregated()) {
            List<Responsable> filtered2 = new ArrayList<Responsable>();
            for (Responsable searchedItem : filtered) {
                if (!(searchedItem.getEpisode() != null && search.getStartEpisode() <= searchedItem.getEpisode() && searchedItem
                        .getEpisode() <= search.getStopEpisode())) {
                    continue;
                }
                filtered2.add(searchedItem);
            }
            filtered2 = cleanDuplicates(filtered2);
            if (!search.getIgnoreMissing()) {
                if (filtered2.size() != (search.getStopEpisode() - search.getStartEpisode())) {
                    log.error("Missing episodes in the list");
                    outcome[1] = "Missing episodes in the list";
                    return outcome;
                }
            }
            outcome[0] = cleanDuplicates(filtered2);
            return outcome;
        }

        outcome[0] = cleanDuplicates(filtered);

        if (outcome[0] == null) {
            outcome[1] = "There are no files...";
        }
        return outcome;
    }

    private List<Responsable> cleanDuplicates(List<Responsable> filtered2) {
        return filtered2;
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

    protected List<Responsable> crawlForLinks(String page) {
        String[] lines = page.split(System.getProperty("line.separator"));
        List<Responsable> searches = new ArrayList<Responsable>();
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
        if(content==null){
        	return null;
        }
        lines = content.split("torrentrow");
        for (String line : lines) {
            if (!line.contains("torrenttable")) {
                continue;
            }
            Responsable searched = new Responsable();
            int d1 = line.indexOf("download.php");
            int d2 = line.indexOf(".torrent", d1);
            String url = line.substring(d1, d2 + 8);
            if (line.contains("freeleech")) {
                searched.setFree(true);
            }
            try {
                searched.setUrl(new URL(FILELIST_URL + url));
            } catch (MalformedURLException e) {
                log.error(e.getMessage(), e);
            }
            searched.setId(grabId(url));
            searched.setName(grabName(url));
            searched.setSeason(grabSeason(url));
            searched.setEpisode(grabEpisode(url));
            searched.setQuality(grabQuality(url));
            searched.setSeed(grapSeed(line));
            if (searched.getSeason() != null && searched.getSeason().length > 0 && searched.getEpisode() == null) {
                searched.setAgregated(true);
            }
            searches.add(searched);
        }
        return searches;
    }

    private Integer grabId(String url) {
        String id = url.substring(url.indexOf("id")).substring(3);
        id = id.replaceFirst("&.*", "").toLowerCase();
        return Integer.parseInt(id);
    }

    private Integer grapSeed(String line) {
        int start1 = line.indexOf("/>times</");
        int start = line.indexOf("torrenttable", start1);
        int end = line.indexOf("torrenttable", start + 10);
        String temp = line.substring(start, end);
        start = temp.indexOf("<b><font color=") + 15;
        temp = temp.substring(start);

        start = temp.indexOf(">") + 1;
        end = temp.indexOf("<");
        if (end < start) {
            return 0;
        } else {
            return Integer.parseInt(temp.substring(start, end));
        }
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

    // private String zfill(String serie, int length) {
    // if (serie.length() >= length) {
    // return serie;
    // }
    // char[] result = new char[length];
    // char[] serieArray = serie.toCharArray();
    // for (int i = 0; i < result.length; i++) {
    // if (i < (result.length - serieArray.length)) {
    // result[i] = '0';
    // } else {
    // result[i] = serieArray[i - serieArray.length];
    // }
    // }
    // return String.valueOf(result);
    // }

    // private class SearchedItem {
    // public Integer id;
    // public Integer seed;
    // public URL url = null;
    // public boolean agregated;
    // public Integer episode;
    // public Integer[] season;
    // public boolean free;
    // public String quality;
    // public String name;
    //
    // @Override
    // }

}
