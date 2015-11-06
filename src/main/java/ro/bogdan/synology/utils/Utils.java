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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import ro.bogdan.synology.engine.beans.Responsable;
import ro.bogdan.synology.engine.beans.Searchable;

public class Utils {
	private static final Logger log = Logger.getLogger(Utils.class);
	private static String cookie = "__cfduid=d9356d4bc1e865ef2ce012976de7a3ba91428844281; uid=126407; pass=aadb525ec41b8fde1988a4f668ea5832; fl=itaod9ggmv2304mcpbpt4o5lf0";

	public static String download(URL url) throws IOException {

		login();

		log.debug("URL: " + url.toString());
		StringBuilder content = new StringBuilder();

		// Proxy proxy = new Proxy(Proxy.Type.HTTP, new
		// InetSocketAddress("proxy.bu.avira.com", 3128));
		// HttpURLConnection connection = (HttpURLConnection)
		// url.openConnection(proxy);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
		connection.addRequestProperty("Cookie", cookie);
		connection.setReadTimeout(3000);
		connection.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine).append(System.lineSeparator());
		}

		File backup = new File("/volume1/misc/" + url.getQuery());
		FileWriter fw = new FileWriter(backup);
		fw.write(content.toString());
		fw.flush();
		fw.close();

		return content.toString();

	}

	public static void login() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://filelist.ro/takelogin.php");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "himnosiss"));
		nvps.add(new BasicNameValuePair("password", "Nokiac61"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpPost.addHeader("Accept-Language", "en-US,en;q=0.5");
		httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
		
		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		cookie = "";
		Header[] headers = response2.getAllHeaders();
		for (Header header : headers) {
			if (header.getName().equals("Set-Cookie")) {
				cookie += header.getValue().substring(0, (header.getValue().indexOf(";") + 1));
			}
		}

		StringBuilder content = new StringBuilder();
		try {
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			BufferedReader in = new BufferedReader(new InputStreamReader(entity2.getContent()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine).append(System.lineSeparator());
			}
			EntityUtils.consume(entity2);
		} finally {
			response2.close();
		}
		System.out.println(content.toString());

	}

	/**
	 * return the bloody cookie
	 */
	// public static String login() throws IOException {
	//
	// // Proxy proxy = new Proxy(Proxy.Type.HTTP, new
	// // InetSocketAddress("proxy.bu.avira.com", 3128));
	// // HttpURLConnection connection = (HttpURLConnection)
	// // url.openConnection(proxy);
	//
	// String urlParameters = "password=Nokiac61&username=himnosiss";
	//
	// URL urlForm = new URL("http://filelist.ro/takelogin.php");
	// HttpURLConnection connection = (HttpURLConnection)
	// urlForm.openConnection();
	// connection.setRequestMethod("POST");
	// connection.addRequestProperty("User-Agent",
	// "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
	// // connection.addRequestProperty("Cookie", cookie);
	// connection.setReadTimeout(3000);
	// connection.setUseCaches(false);
	// connection.setDoOutput(true);
	// connection.setRequestProperty("Content-Type",
	// "application/x-www-form-urlencoded");
	// connection.setRequestProperty("charset", "utf-8");
	// connection.setRequestProperty("Content-Length",
	// String.valueOf(urlParameters.getBytes().length));
	// connection.connect();
	// try (DataOutputStream wr = new
	// DataOutputStream(connection.getOutputStream())) {
	// wr.writeBytes(urlParameters);
	// }
	//
	// BufferedReader in = new BufferedReader(new
	// InputStreamReader(connection.getInputStream()));
	//
	// StringBuilder content = new StringBuilder();
	// String inputLine;
	// while ((inputLine = in.readLine()) != null) {
	// content.append(inputLine).append(System.lineSeparator());
	// }
	//
	// if (content.indexOf("FileList :: Login ") == -1) {
	// // not login page, we are doing it wrong but fuck-it just log it
	// log.warn("NOT LOGIN PAGE!");
	// }
	//
	// cookie = connection.getRequestProperty("Set-Cookie");
	// if (cookie == null) {
	// cookie = connection.getRequestProperty("Cookie");
	// }
	// connection.disconnect();
	// return content.toString();
	//
	// }

	public static void main(String[] args) {
		try {
			login();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
