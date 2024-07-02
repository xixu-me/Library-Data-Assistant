package server.tools.crawling;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import server.tools.DBConnection;

public class NewsThread implements Runnable {
    private String urlPath;

    public NewsThread(String urlPath) {
        super();
        this.urlPath = urlPath;
    }

    @Override
    public void run() {
        String content = CrawlerTools.get(urlPath, "GB2312");
        Document doc = Jsoup.parse(content);
        Elements elements = doc.select(".bang_wrapper .bang_list_box ul li");
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = null;
        String sql = null;
        int index = 0;
        for (Element bookelement : elements) {
            index++;
            if (index >= 20)
                break;
            String title = bookelement.select(".name a").text();
            Elements publisherInfoElements = bookelement.select(".publisher_info");
            String author = null;
            String publisher = null;
            if (!publisherInfoElements.isEmpty()) {
                Element firstPublisherInfoElement = publisherInfoElements.get(0);
                Element secondPublisherInfoElement = publisherInfoElements.get(1);
                author = firstPublisherInfoElement.select("a").text();
                publisher = secondPublisherInfoElement.select("a").text();
            }
            String oprice = bookelement.select(".price_r").text();
            String nprice = bookelement.select(".price_n").first().text();
            String href = bookelement.select(".name a").attr("href");
            double oldprice = Double.parseDouble(oprice.substring(1));
            double newprice = Double.parseDouble(nprice.substring(1));
            sql = "insert into book values(?,?,?,?,?,?)";
            try {
                ps = con.prepareStatement(sql);
                ps.setString(1, title);
                ps.setString(2, author);
                ps.setString(3, publisher);
                ps.setDouble(4, oldprice);
                ps.setDouble(5, newprice);
                ps.setString(6, href);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(title);
            System.out.println(author);
            System.out.println(publisher);
            System.out.println(oldprice);
            System.out.println(newprice);
            System.out.println(href);
        }
        try {
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
