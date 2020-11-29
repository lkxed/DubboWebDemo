package service;

import model.Account;
import tool.MySQLConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lkxed
 */
public class AccountServiceImpl implements AccountService {

    private MySQLConnectionPool pool;

    public AccountServiceImpl() {
        pool = new MySQLConnectionPool("jdbc:mysql://localhost:3306/dubbo_web_demo?useSSL=false",
                "root", "123456", 4);
        pool.initialize();
    }

    @Override
    public Account select(String username) {
        try (Connection connection = pool.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement queryAccount = connection.prepareStatement("SELECT * FROM account WHERE username = ?");
            queryAccount.setString(1, username);
            ResultSet resultSet = queryAccount.executeQuery();
            Account account = parseResultSet(resultSet);
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * spend certain amount of money with specified username and amount
     * (leave the transaction management to consumer)
     * @param username username of who spends the money
     * @param amount how much to spend
     * @return
     */
    @Override
    public void spend(String username, double amount) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement updateBalance = connection
                    .prepareStatement("UPDATE account SET balance = balance - ? WHERE username = ?");
            updateBalance.setDouble(1, amount);
            updateBalance.setString(2, username);
            updateBalance.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private Account parseResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            double balance = resultSet.getDouble("balance");
            return new Account(id, username, balance);
        }
        return null;
    }
}
