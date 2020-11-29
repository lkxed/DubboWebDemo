import server.BusinessServer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author lkxed
 */
public class BusinessApplication {
    public static void main(String[] args) throws InterruptedException, IOException {
        BusinessServer server = new BusinessServer("localhost", 8080);
        server.start();

        /* in order for the program to run continuously */
        new CountDownLatch(0).await();
    }
}
