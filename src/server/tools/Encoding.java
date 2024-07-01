package server.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encoding {
	// 对指定的字符串进行md5编码
	public static String md5(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 将指定的文件进行base64编码，返回编码字符串
	public static String base64FromFile(String filePath) {
		try {
			byte[] fileBytes = Files.readAllBytes(Path.of(filePath));
			return Base64.getEncoder().encodeToString(fileBytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 将base64编码字符串还原为文件,文件路径及文件名为filePath
	public static void base64ToFile(String base64Str, String filePath) {
		try {
			byte[] fileBytes = Base64.getDecoder().decode(base64Str);
			Files.write(Path.of(filePath), fileBytes, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
