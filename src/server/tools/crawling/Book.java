package server.tools.crawling;

public class Book {
    private String title;
    private String author;
    private String publisher;
    private double oldprice;
    private double newprice;
    private String href;

    public Book() {
    }

    public Book(String title, String author, String publisher, double oldprice, double newprice, String href) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.oldprice = oldprice;
        this.newprice = newprice;
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

    public double getOldprice() {
        return oldprice;
    }

    public void setOldprice(double oldprice) {
        this.oldprice = oldprice;
    }

    public double getNewprice() {
        return newprice;
    }

    public void setNewprice(double newprice) {
        this.newprice = newprice;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String toString() {
        return "Book{title = " + title + ", author = " + author + ", publisher = " + publisher + ", oldprice = "
                + oldprice + ", newprice = " + newprice + ", href = " + href + "}";
    }
}
