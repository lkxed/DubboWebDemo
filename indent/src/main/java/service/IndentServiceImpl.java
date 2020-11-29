package service;

import model.Account;
import model.Indent;
import model.Merchandise;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import tool.DubboTool;
import tool.MySQLConnectionPool;
import tool.ZooKeeperConstant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lkxed
 */
public class IndentServiceImpl implements IndentService {

    private MySQLConnectionPool pool;
    private AccountService accountService;
    private MerchandiseService merchandiseService;

    public IndentServiceImpl() {
        /* initialize MySQLConnectionPool */
        pool = new MySQLConnectionPool("jdbc:mysql://localhost:3306/dubbo_web_demo?useSSL=false",
                "root", "123456", 4);
        pool.initialize();
        /* setup application & registry configuration */
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(new ApplicationConfig("IndentService"));
        bootstrap.registry(new RegistryConfig(ZooKeeperConstant.zkServers));

        /* setup AccountService consumer instance */
        accountService = DubboTool.getInstance(AccountService.class, bootstrap);

        /* setup MerchandiseService consumer instance */
        merchandiseService = DubboTool.getInstance(MerchandiseService.class, bootstrap);
    }

    @Override
    public Indent query(int id) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement queryById = connection.prepareStatement("SELECT * FROM indent WHERE id = ?");
            queryById.setInt(1, id);
            ResultSet resultSet = queryById.executeQuery();
            Indent indent = parseResultSetForOne(resultSet);
            return indent;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Indent> queryAll() {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement queryById = connection.prepareStatement("SELECT * FROM indent");
            ResultSet resultSet = queryById.executeQuery();
            List<Indent> indents = parseResultSetForMany(resultSet);
            return indents;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Indent create(String name, String username, int number) {
        Account account = accountService.select(username);
        Merchandise merchandise = merchandiseService.select(name);
        try (Connection connection = pool.getConnection()) {
            PreparedStatement createIndent = connection.prepareStatement("INSERT INTO indent(?, ?, ?, ?) VALUES (?, ?, ?, ?)");
            createIndent.setString(1, "account_id");
            createIndent.setString(2, "merchandise_id");
            createIndent.setString(3, "number");
            createIndent.setString(4, "sales");
            createIndent.setInt(5, account.getId());
            createIndent.setInt(6, merchandise.getId());
            createIndent.setInt(7, number);
            createIndent.setDouble(8, merchandise.getPrice() * number);
            createIndent.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Indent parseResultSetForOne(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            int accountId = resultSet.getInt("account_id");
            int merchandiseId = resultSet.getInt("merchandise_id");
            int number = resultSet.getInt("number");
            double sales = resultSet.getDouble("sales");
            Timestamp createTime = resultSet.getTimestamp("create_time");
            return new Indent(id, accountId, merchandiseId, number, sales, createTime);
        }
        return null;
    }

    private List<Indent> parseResultSetForMany(ResultSet resultSet) throws SQLException {
        List<Indent> indents = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int accountId = resultSet.getInt("account_id");
            int merchandiseId = resultSet.getInt("merchandise_id");
            int number = resultSet.getInt("number");
            double sales = resultSet.getDouble("sales");
            Timestamp createTime = resultSet.getTimestamp("create_time");
            Indent indent = new Indent(id, accountId, merchandiseId, number, sales, createTime);
            indents.add(indent);
        }
        return indents;
    }
}
