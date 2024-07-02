package server.tools.crawling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTools {
	public static String get(String urlStr, String charset) throws IOException {
		StringBuilder buf = new StringBuilder();
		@SuppressWarnings("deprecation")
		URL url = new URL(urlStr);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.53");
		if (con.getResponseCode() == 200) {
			try (BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream(), charset))) {
				String info;
				while ((info = read.readLine()) != null)
					buf.append(info);
			}
		} else {
			System.out.println("Error! Response code: " + con.getResponseCode() + ".");
		}
		con.disconnect();
		return buf.toString();
	}

	public static String encodingUrl(String url) {
		String regex = "[\u4e00-\u9fa5]+";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(url);
		while (mat.find()) {
			String hanzi = mat.group();
			try {
				url = url.replace(hanzi, URLEncoder.encode(hanzi, "utf-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return url;
	}
}
