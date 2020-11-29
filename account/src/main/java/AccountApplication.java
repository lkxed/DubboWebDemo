import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import service.AccountService;
import service.AccountServiceImpl;
import tool.ZooKeeperConstant;

import java.util.concurrent.CountDownLatch;

/**
 * @author lkxed
 */
public class AccountApplication {
    public static void main(String[] args) throws InterruptedException {
        /* register AccountService provider */
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        ServiceConfig<AccountService> service = new ServiceConfig<>();
        service.setInterface(AccountService.class);
        service.setRef(new AccountServiceImpl());
        ApplicationConfig application = new ApplicationConfig("account-service");
        application.setQosPort(22221);
        bootstrap.application(application);
        bootstrap.registry(new RegistryConfig(ZooKeeperConstant.zkServers));
        bootstrap.protocol(new ProtocolConfig("dubbo", 20881));
        bootstrap.service(service);
        bootstrap.start();
        /* for program to run continuously */
        new CountDownLatch(1).await();
    }
}
