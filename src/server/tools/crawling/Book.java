package server.tools.crawling;

public class Book {
    private String title;
    private String author;
    private String publisher;
    private double originalprice;
    private double discountedprice;
    private String href;

    public Book() {
    }

    public Book(String title, String author, String publisher, double originalprice, double discountedprice,
            String href) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.originalprice = originalprice;
        this.discountedprice = discountedprice;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getOriginalPrice() {
        return originalprice;
    }

    public void setOriginalPrice(double originalprice) {
        this.originalprice = originalprice;
    }

    public double getDiscountedPrice() {
        return discountedprice;
    }

    public void setDiscountedPrice(double discountedprice) {
        this.discountedprice = discountedprice;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "Title: " + this.title + ", Author: " + this.author + ", Publisher: " + this.publisher
                + ", Original Price: " + this.originalprice + ", Discounted Price: " + this.discountedprice + ", URL: "
                + this.href;
    }
}
