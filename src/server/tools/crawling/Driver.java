package server.tools.crawling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Driver {
    private static final String URL_PREFIX = "http://bang.dangdang.com/books/bestsellers/01.00.00.00.00.00-24hours-0-0-1-";

    public static void crawl() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        try {
            for (int i = 1; i <= 5; i++) {
                String url = URL_PREFIX + i;
                executor.submit(new NewsThread(url));
                if (i > 1)
                    TimeUnit.SECONDS.sleep(10);
            }
            System.out.println("Crawling finished!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread was interrupted, Failed to complete operation");
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS))
                    executor.shutdownNow();
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }
}
