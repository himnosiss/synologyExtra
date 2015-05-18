package ro.bogdan.synology.search.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ro.bogdan.synology.engine.Searchable;
import ro.bogdan.synology.search.Search;
import ro.bogdan.synology.utils.Utils;

public class Filelist implements Search {

	private String FILELIST_URL = "http://filelist.ro/";
	private String BROWSE_SERIES = "browse.php?searchin=1&sort=0&cat=21&search=";
	private String BROWSE_MOVIE = "browse.php?searchin=1&sort=0&search=";
	private static final int DEFAULT_ZFILL_LENGTH = 2;
	private static Logger log = Logger.getLogger(Filelist.class);

	@Override
	public List<URL> search(Searchable search) {
		try {
			String urlString = FILELIST_URL;
			if (search.getCategory().equals("serie")) {
				urlString += BROWSE_SERIES;
			} else if (search.getCategory().equals("movie")) {
				urlString += BROWSE_MOVIE;
			}
			URL url = new URL(urlString + search.getQuery());
			String content = Utils.downloadFile(url);
			List<String> links = crawlForLinks(content, search);

		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	private List<String> crawlForLinks(String page, Searchable search) {
		String[] lines = page.split(System.getProperty("line.separator"));
		List<Link> links = new ArrayList<Link>();
		String content = null;
		for (String line : lines) {
			if(line.contains("download.php")){
				content = line;
				break;
			}
		}
		lines = content.split("torrentrow");
		for (String line : lines) {
			Link link = new Link();
	        int d1 = line.indexOf("download.php");
	        int d2 = line.indexOf(".torrent",d1);
	        link.url = line.substring(d1,d2+8);
	        if(line.contains("freeleech")){
	        	link.free = true;
	        }
	        link.quality = grabQuality(link.url);
        }
		
		
		return null;

		/*
		 * 
		 * if line.find('download.php') != -1: splits = line.split('torrentrow')
		 * 
		 * if nextPage is None and line.find('pager') != -1: tmp =
		 * line.split('</span>') nextPage = tmp[-1:] # print '......', nextPage
		 * 
		 * someAdded = False for x_split in splits: freeleech = False if
		 * x_split.find('FreeLeech') != -1: freeleech = True start =
		 * x_split.find('download.php?'); end = x_split.find('.torrent', start)
		 * + 8; # print x_split[start:end] + str(freeleech) turl =
		 * x_split[start:end] quality = self.getQuality(turl.lower()) if
		 * turl.lower().find(self.pattern.lower()) != -1:
		 * self.URLS.append(URLS(turl, freeleech, quality)) someAdded = True
		 * 
		 * self.next = None if (nextPage is not None) and someAdded: startp =
		 * nextPage[0].find('browse.php') if startp != -1: endp =
		 * nextPage[0].find('><', startp) - 1 self.next =
		 * nextPage[0][startp:endp].replace('&amp;','&') # this is here to
		 * simulate a more human interaction time.sleep(random.randint(1, 10))
		 * self._parsePage(self.next)
		 */
	}

	private String grabQuality(String url) {
		
	    return url;
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
	
	private class Link{
		public String url;
		public boolean free;
		public String quality;
	}

}
