package ro.bogdan.synology.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
	public static String download(URL url) {

		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
			connection.addRequestProperty("Cookie",	"__cfduid=d9356d4bc1e865ef2ce012976de7a3ba91428844281; uid=126407; pass=0ddd0366996e2a7fe8d7cb9335fe2ec2; PHPSESSID=i932kbkt6qq93mo46k35cn7813");
			connection.setReadTimeout(3000);
			connection.connect();
			DataInputStream dis = new DataInputStream(connection.getInputStream());
			String content = dis.readUTF();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws MalformedURLException {
		Utils.download(new URL("http://192.168.20.200:7070/"));
	}
}
