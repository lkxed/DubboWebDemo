import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import service.MerchandiseService;
import service.MerchandiseServiceImpl;
import tool.ZooKeeperConstant;

import java.util.concurrent.CountDownLatch;

/**
 * @author lkxed
 */
public class MerchandiseApplication {
    public static void main(String[] args) throws InterruptedException {
        /* register MerchandiseService provider */
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        ApplicationConfig application = new ApplicationConfig("merchandise-service");
        application.setQosPort(22222);
        bootstrap.application(application);
        bootstrap.registry(new RegistryConfig(ZooKeeperConstant.zkServers));
        bootstrap.protocol(new ProtocolConfig("dubbo", 20882));
        ServiceConfig<MerchandiseService> service = new ServiceConfig<>();
        service.setInterface(MerchandiseService.class);
        service.setRef(new MerchandiseServiceImpl());
        bootstrap.service(service);
        bootstrap.start();
        /* in order for the program to run continuously */
        new CountDownLatch(1).await();
    }
}
