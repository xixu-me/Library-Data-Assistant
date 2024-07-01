package pa.dangdang;

public class Book {

    private String title;
    private String author; // 作者
    private String publisher; // 出版社
    private double oldprice; // 出版时间
    private double newprice; // 价格
    private String href; // 图书详情url

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

    /**
     * 获取
     * 
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置
     * 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取
     * 
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置
     * 
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取
     * 
     * @return publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * 设置
     * 
     * @param publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * 获取
     * 
     * @return oldprice
     */
    public double getOldprice() {
        return oldprice;
    }

    /**
     * 设置
     * 
     * @param oldprice
     */
    public void setOldprice(double oldprice) {
        this.oldprice = oldprice;
    }

    /**
     * 获取
     * 
     * @return newprice
     */
    public double getNewprice() {
        return newprice;
    }

    /**
     * 设置
     * 
     * @param newprice
     */
    public void setNewprice(double newprice) {
        this.newprice = newprice;
    }

    /**
     * 获取
     * 
     * @return href
     */
    public String getHref() {
        return href;
    }

    /**
     * 设置
     * 
     * @param href
     */
    public void setHref(String href) {
        this.href = href;
    }

    public String toString() {
        return "Book{title = " + title + ", author = " + author + ", publisher = " + publisher + ", oldprice = "
                + oldprice + ", newprice = " + newprice + ", href = " + href + "}";
    }
    // private String imageHref; //封面图片href地址

}
