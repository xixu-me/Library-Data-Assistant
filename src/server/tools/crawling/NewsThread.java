package server.tools.crawling;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import server.tools.DBConnection;

public class NewsThread implements Runnable {
    private String urlPath;

    public NewsThread(String urlPath) {
        this.urlPath = urlPath;
    }

    @Override
    public void run() {
        try {
            String content = CrawlerTools.get(urlPath, "GB2312");
            Document doc = Jsoup.parse(content);
            Elements elements = doc.select(".bang_wrapper .bang_list_box ul li");
            try (Connection con = DBConnection.getConnection()) {
                for (Element bookElement : elements.subList(0, Math.min(elements.size(), 20))) {
                    String title = bookElement.select(".name a").text();
                    Elements publisherInfoElements = bookElement.select(".publisher_info");
                    String author = publisherInfoElements.size() > 0 ? publisherInfoElements.get(0).select("a").text()
                            : null;
                    String publisher = publisherInfoElements.size() > 1
                            ? publisherInfoElements.get(1).select("a").text()
                            : null;
                    String oprice = bookElement.select(".price_r").text();
                    String nprice = bookElement.select(".price_n").first().text();
                    String href = bookElement.select(".name a").attr("href");
                    double originalprice = Double.parseDouble(oprice.substring(1));
                    double discountedprice = Double.parseDouble(nprice.substring(1));
                    insertBook(con, title, author, publisher, originalprice, discountedprice, href);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertBook(Connection con, String title, String author, String publisher, double originalprice,
            double discountedprice, String href) throws SQLException {
        String sql = "INSERT INTO book VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, publisher);
            ps.setDouble(4, originalprice);
            ps.setDouble(5, discountedprice);
            ps.setString(6, href);
            ps.executeUpdate();
        }
    }
}
