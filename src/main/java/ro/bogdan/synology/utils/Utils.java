package ro.bogdan.synology.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import ro.bogdan.synology.engine.beans.Responsable;
import ro.bogdan.synology.engine.beans.Searchable;

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
        File backup = new File("/home/bbala/test/" + url.getQuery());
        FileWriter fw = new FileWriter(backup);
        fw.write(content.toString());
        fw.flush();
        fw.close();
        return content.toString();

    }

    public static String downloadFile(URL url) throws IOException {
        log.debug("URL: " + url.toString());

        // //TO STOP AT THE SECOND PAGE
        // if (url.toString().indexOf("page=2")!=-1) {
        // return null;
        // }
        StringBuilder content = new StringBuilder();
        Reader r = new FileReader("/home/bbala/test/searchin=1&sort=0&cat=21&search=the.big.bang&page="
                + url.getQuery().substring(url.getQuery().length() - 1));
        BufferedReader in = new BufferedReader(r);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append(System.lineSeparator());
        }
        in.close();
        return content.toString();

    }

    //
    // public static void main(String[] args) throws IOException {
    // Utils.download(new URL("http://localhost:8080/synologyExtra/"));
    // }

    public static void processResponse(String error, List<Responsable> responses, PrintWriter writer) {
        if (!isBlank(error)) {
            writer.write(error);
            return;
        }

        writer.write("<table id = 'urllist'>");
        writer.write("<tr><th>Select</th><th>Name</th><th>Quality</th><th>Seeds</th><th>Full</th><th>Free</th><th>Season</th><th>Episode</th><th>Link</th></tr>");

        for (Responsable url : responses) {
            writer.write("<tr>");
            writer.write("<td>");
            writer.write("<input type = 'checkbox' name='" + url.getId() + "'>");
            writer.write("</td>");
            writer.write("<td>");
            writer.write(url.getName());
            writer.write("</td>");
            writer.write("<td>");
            writer.write(url.getQuality());
            writer.write("</td>");
            writer.write("<td>");
            writer.write(url.getSeed().toString());
            writer.write("</td>");
            writer.write("<td>");
            writer.write(String.valueOf(url.isAgregated()));
            writer.write("</td>");
            writer.write("<td>");
            writer.write(String.valueOf(url.isFree()));
            writer.write("</td>");
            writer.write("<td>");
            writer.write(arrayToString(url.getSeason()));
            writer.write("</td>");
            writer.write("<td>");
            writer.write(String.valueOf(url.getEpisode()));
            writer.write("</td>");

            writer.write("<td> <a href='");
            writer.write(url.getUrl().toString());
            writer.write("'>");
            writer.write(url.getUrl().getQuery());
            writer.write("</a></td>");
            writer.write("</tr>");
        }
        writer.write("</table>");
        writer.write("<input value='Download selected' type='button' onclick=downloadSelected() >");
    }

    public static boolean isBlank(String input) {
        if (input == null || input.trim().length() == 0 || input.length() == 0) {
            return true;
        }
        return false;
    }

    public static List<Responsable> searchInHistory(Searchable searchable) {
        List<Searchable> toBeRemoved = new ArrayList<Searchable>();
        List<Responsable> returnable = null;
        // Responsable item = GlobalContext.history.get(searchable);
        // if(item!=null){
        //
        // }

        for (Entry<Searchable, List<Responsable>> item : GlobalContext.history.entrySet()) {
            if (item.getKey().equals(searchable)) {
                if (item.getKey().getTimestamp() > System.currentTimeMillis() - searchable.getExpInterval()) {
                    returnable = item.getValue();
                } else {
                    toBeRemoved.add(item.getKey());
                }
            }
        }
        return returnable;
    }

    public static String arrayToString(Object[] in) {
        StringBuilder sb = new StringBuilder();
        for (Object elem : in) {
            sb.append(elem);
        }
        return sb.toString();
    }
}
