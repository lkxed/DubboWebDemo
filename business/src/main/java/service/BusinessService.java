package service;

import model.Account;
import model.Indent;
import model.Merchandise;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import tool.DubboTool;
import tool.ZooKeeperConstant;

import java.util.List;

/**
 * @author lkxed
 */
public class BusinessService {

    private AccountService accountService;
    private MerchandiseService merchandiseService;
    private IndentService indentService;

    public BusinessService() {
        /* initialize application & registry configuration */
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(new ApplicationConfig("IndentService"));
        bootstrap.registry(new RegistryConfig(ZooKeeperConstant.zkServers));

        /* setup AccountService consumer instance */
        accountService = DubboTool.getInstance(AccountService.class, bootstrap);

        /* setup MerchandiseService consumer instance */
        merchandiseService = DubboTool.getInstance(MerchandiseService.class, bootstrap);

        /* setup IndentService consumer instance */
        indentService = DubboTool.getInstance(IndentService.class, bootstrap);
    }

    /**
     * certain account buys certain number of certain merchandise
     * (global transaction management should be involved here in order for this method to work correctly)
     * @param username username of the buyer
     * @param name name of the merchandise
     * @param number number of the merchandise to buy
     * @return the created indent
     */
    //@GlobalTransactional
    public Indent buy(String username, String name, int number) {
        Merchandise merchandise = merchandiseService.select(name);
        merchandiseService.sell(name, number);
        accountService.spend(username, merchandise.getPrice());
        Indent indent = indentService.create(name, username, number);
        return indent;
    }

    public Account queryAccount(String username) {
        return accountService.select(username);
    }

    public Merchandise queryMerchandise(String name) {
        return merchandiseService.select(name);
    }

    public Indent queryIndent(int id) {
        return indentService.query(id);
    }

    public List<Indent> queryIndentList() {
        return indentService.queryAll();
    }
}
