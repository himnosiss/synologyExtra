package ro.bogdan.synology.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.apache.log4j.Logger;

public class Utils {
    private static final Logger log = Logger.getLogger(Utils.class);

    public static String download(URL url) throws IOException {
        log.debug("URL: " + url.toString());
        StringBuilder content = new StringBuilder();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.bu.avira.com", 3128));
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
        connection
                .addRequestProperty(
                        "Cookie",
                        "__cfduid=d602bf5817f4907388cd1ebb18beed1871431952822; uid=126407; pass=cefc55f5a95562c981ffc6d7c252fc2a; fl=fq8l6iean611vu2jksoubu2837");
        connection.setReadTimeout(3000);
        connection.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        
        
        
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append(System.lineSeparator());
        }
        File backup = new File("/home/bbala/test/"+url.getQuery());
        FileWriter fw = new FileWriter(backup);
        fw.write(content.toString());
        fw.flush();
        fw.close();
        return content.toString();

    }

    public static String downloadFile(URL url) throws IOException {
        log.debug("URL: " + url.toString());

//        //TO STOP AT THE SECOND PAGE
//        if (url.toString().indexOf("page=2")!=-1) {
//            return null;
//        }
        StringBuilder content = new StringBuilder();
        Reader r = new FileReader("/home/bbala/test/searchin=1&sort=0&cat=21&search=the.big.bang&page="+url.getQuery().substring(url.getQuery().length()-1));
        BufferedReader in = new BufferedReader(r);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append(System.lineSeparator());
        }
        return content.toString();

    }
    //
    // public static void main(String[] args) throws IOException {
    // Utils.download(new URL("http://localhost:8080/synologyExtra/"));
    // }
}
