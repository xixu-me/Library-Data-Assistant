package server.tools.crawling;

public class Driver {
    public static void crawl() {
        try {
            String url = "http://bang.dangdang.com/books/bestsellers/01.00.00.00.00.00-24hours-0-0-1-1";
            Thread thread1 = new Thread(new NewsThread(url));
            thread1.start();

            for (int i = 2; i <= 5; i++) {

                String url2 = "http://bang.dangdang.com/books/bestsellers/01.00.00.00.00.00-24hours-0-0-1-" + i;
                Thread thread2 = new Thread(new NewsThread(url2));
                thread2.start();
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
