package ro.bogdan.synology.search.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Default;

import ro.bogdan.synology.engine.Searchable;
import ro.bogdan.synology.search.Search;
import ro.bogdan.synology.utils.Utils;


@Default
public class Filelist implements Search {

//	@Override
//    public String search(String search) {
//	    // TODO Auto-generated method stub
//	    return "filelist used";
//    }
//	
	private String FILELIST_URL = "http://filelist.ro/";
	private String BROWSE_SERIES = "browse.php?searchin=1&sort=0&cat=21&search=";

	@Override
	public List<URL> search(Searchable search) {
		try {
			URL url = new URL(FILELIST_URL + BROWSE_SERIES + search.getQuery());
			String content = Utils.download(url);
			

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private List<String> crawlForLinks(String page){
		List<String> links = new ArrayList<String>();
		
		return null;
		
		
		
		/*

		 * if line.find('download.php') != -1:
                splits = line.split('torrentrow')
            
            if nextPage is None and line.find('pager') != -1:
                tmp = line.split('</span>')
                nextPage = tmp[-1:]
#                 print '......', nextPage

		someAdded = False
        for x_split in splits:
            freeleech = False
            if x_split.find('FreeLeech') != -1:
                freeleech = True
            start = x_split.find('download.php?');
            end = x_split.find('.torrent', start) + 8;
#             print x_split[start:end] + str(freeleech)
            turl = x_split[start:end]
            quality = self.getQuality(turl.lower())
            if turl.lower().find(self.pattern.lower()) != -1:
                self.URLS.append(URLS(turl, freeleech, quality))
                someAdded = True

        self.next = None
        if (nextPage is not None) and someAdded:
            startp = nextPage[0].find('browse.php')
            if startp != -1:
                endp = nextPage[0].find('><', startp) - 1
                self.next = nextPage[0][startp:endp].replace('&amp;','&')
                # this is here to simulate a more human interaction
                time.sleep(random.randint(1, 10))
                self._parsePage(self.next)
		 */
	}


}
