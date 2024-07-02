package server.tools.crawling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTools {
	public static String get(String urlStr, String charset) {
		StringBuffer buf = new StringBuffer();
		HttpURLConnection con = null;
		InputStream in = null;
		BufferedReader read = null;
		try {
			URL url = new URL(urlStr);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.53");
			int code = con.getResponseCode();
			if (code == 200) {
				in = con.getInputStream();
				read = new BufferedReader(new InputStreamReader(in, charset));
				String info = "";
				while ((info = read.readLine()) != null)
					buf.append(info);
			} else
				System.out.println("Error! Response code: " + code + ".");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (read != null)
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (con != null)
				con.disconnect();
		}
		return buf.toString();
	}

	public static String encodingUrl(String url) {
		String regex = "[\u4e00-\u9fa5]+";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(url);
		while (mat.find()) {
			String hanzi = mat.group();
			System.out.println(hanzi);
			String encodehanzi = "";
			try {
				encodehanzi = URLEncoder.encode(hanzi, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			url = url.replaceAll(hanzi, encodehanzi);
		}
		return url;
	}
}
