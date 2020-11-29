package tool;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
public class MySQLConnectionPool {

    final String url;
    final String user;
    final String password;
    final int maxPoolSize;

    Queue<Connection> availableConnections;
    List<Connection> usingConnections;

    public void initialize() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        availableConnections = new LinkedBlockingQueue<>(maxPoolSize);
        usingConnections = new LinkedList<>();
    }

    public Connection getConnection() throws SQLException {
        Connection connection;
        if (availableConnections.isEmpty()) {
            connection = DriverManager.getConnection(url, user, password);
//            connection.setAutoCommit(false);
            availableConnections.offer(connection);
        }
        collectConnections();
        return availableConnections.poll();
    }

    private void collectConnections() throws SQLException {
        Iterator<Connection> usingIterator = usingConnections.iterator();
        while (usingIterator.hasNext()) {
            Connection usingConnection = usingIterator.next();
            if (usingConnection.isClosed() && availableConnections.size() < maxPoolSize) {
                availableConnections.offer(usingConnection);
                usingIterator.remove();
            }
        }
    }
}
