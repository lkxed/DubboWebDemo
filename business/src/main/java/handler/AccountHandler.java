package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BusinessService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;

/**
 * HttpHandler for path starts with "/account"
 * (serves as an example)
 * @author lkxed
 */
public class AccountHandler implements HttpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHandler.class);
    private final BusinessService businessService;

    public AccountHandler() {
        businessService = new BusinessService();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        LOGGER.info("incoming {} request for {}", requestMethod, uri.getPath());

        Headers requestHeaders = httpExchange.getRequestHeaders();
        LOGGER.info("requestHeaders: {}", requestHeaders.entrySet());

        if ("GET".equals(requestMethod) && "query".equals(uri.getPath())) {
            String query = uri.getQuery();
            LOGGER.info("requestQuery: {}", query);

            String[] pair = query.split("=");
            String username = pair[1];
            Account account = businessService.queryAccount(username);
            String content = account.toString();

            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, content.length());
            LOGGER.info("responseHeaders: {}", responseHeaders.entrySet());

            OutputStream responseBody = httpExchange.getResponseBody();
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(responseBody))) {
                writer.write(content, 0, content.length());
                writer.flush();
                LOGGER.info("responseBody: {}", content);
            }
        }
    }
}
