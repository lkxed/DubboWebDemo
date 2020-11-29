package service;

import model.Merchandise;
import tool.MySQLConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lkxed
 */
public class MerchandiseServiceImpl implements MerchandiseService {

    private MySQLConnectionPool pool;

    public MerchandiseServiceImpl() {
        /* 初始化数据库连接池 */
        pool = new MySQLConnectionPool("jdbc:mysql://localhost:3306/dubbo_web_demo?useSSL=false",
                "root", "123456", 4);
        pool.initialize();
    }

    @Override
    public Merchandise select(String name) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement queryMerchandise = connection.prepareStatement("SELECT * FROM merchandise WHERE name = ?");
            queryMerchandise.setString(1, name);
            ResultSet resultSet = queryMerchandise.executeQuery();
            Merchandise merchandise = parseResultSetForOne(resultSet);
            return merchandise;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void sell(String name, int number) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement updateInventory = connection.prepareStatement("UPDATE merchandise SET inventory = inventory - ? WHERE name = ?");
            updateInventory.setInt(1, number);
            updateInventory.setString(2, name);
            updateInventory.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private Merchandise parseResultSetForOne(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double price = resultSet.getDouble("price");
            int inventory = resultSet.getInt("inventory");
            return new Merchandise(id, name, price, inventory);
        }
        return null;
    }
}
