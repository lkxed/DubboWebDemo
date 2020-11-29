package server;

import com.sun.net.httpserver.HttpServer;
import handler.AccountHandler;
import handler.BusinessHandler;
import handler.IndentHandler;
import handler.MerchandiseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * A HTTP server
 * @author lkxed
 */
public class BusinessServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServer.class);

    private final String host;
    private final int port;

    private final HttpServer server;

    public BusinessServer(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(host, port), 0);
        initContext();
    }

    private void initContext() {
        server.createContext("/account", new AccountHandler());
        server.createContext("/merchandise", new MerchandiseHandler());
        server.createContext("/indent", new IndentHandler());
        server.createContext("/business", new BusinessHandler());
    }

    public void start() {
        server.start();
        LOGGER.info("server started at {}:{}", host, port);
    }

    public void stop() {
        server.stop(5);
        LOGGER.info("server stopped");
    }
}
