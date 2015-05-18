package ro.bogdan.synology.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class Utils {
	private static final Logger log = Logger.getLogger(Utils.class);
	
	public static String download(URL url) throws IOException {
		log.debug("URL: "+url.toString());
		StringBuilder content = new StringBuilder();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("User-Agent",
			        "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
			connection
			        .addRequestProperty(
			                "Cookie",
			                "__cfduid=d9356d4bc1e865ef2ce012976de7a3ba91428844281; uid=126407; pass=0ddd0366996e2a7fe8d7cb9335fe2ec2; PHPSESSID=i932kbkt6qq93mo46k35cn7813");
			connection.setReadTimeout(3000);
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
				content.append(inputLine).append(System.lineSeparator());
			}
		return content.toString();

	}
	public static String downloadFile(URL url) throws IOException {
		log.debug("URL: "+url.toString());
		StringBuilder content = new StringBuilder();
		Reader r = new FileReader("/home/bbala/testList.html");
			BufferedReader in = new BufferedReader(r);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
				content.append(inputLine).append(System.lineSeparator());
			}
		return content.toString();

	}
//
//	public static void main(String[] args) throws IOException {
//		Utils.download(new URL("http://localhost:8080/synologyExtra/"));
//	}
}
