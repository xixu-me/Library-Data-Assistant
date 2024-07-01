package pa.tools;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//解析html格式字符串
public class HtmlParse {
	// 根据标签名称获取内容
	public static String getContent(String str, String tagName) {
		String content = "";
		String regex = "<" + tagName + ".*?>(.*?)</" + tagName + ">";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			content = matcher.group(1);
		}
		return content;
	}

	// 根据标签名，属性名获取属性值
	public static String getAttributeValue(String str, String tagName, String attributeName) {
		String value = "";
		// 标签与属性间至少有一个空格\\s+
		// 属性值可以用'或"括号起来['"]
		// >前可以有任意个空格
		String regex = "<" + tagName + ".*?" + attributeName + "=['\"](.*?)['\"].*?>";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			value = matcher.group(1);
		}
		return value;
	}

	// 根据标签名，class名获取内容
	public static ArrayList<String> getContentByClassName(String str, String tagName, String className) {
		ArrayList<String> list = new ArrayList<String>();
		String regex = "<" + tagName + ".*?class=['\"]" + className + "['\"].*?>(.*?)</" + tagName + ">";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;
	}

	public static void main(String[] args) {
		String str = "<a class=\"nbg\" href=\"https://book.douban.com/subject/27104286/\"   onclick=\"moreurl(this,{i:'0',query:'',subject_id:'27104286',from:'book_subject_search'})\">        <img class=\"\" src=\"https://img3.doubanio.com/view/subject/s/public/s29508790.jpg\"          width=\"90\">      </a>    ";
		String imageUrl = getAttributeValue(str, "a", "href");
		System.out.println(imageUrl);
	}

}
