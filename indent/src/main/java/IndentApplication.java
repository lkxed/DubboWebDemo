import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import service.IndentService;
import service.IndentServiceImpl;
import tool.ZooKeeperConstant;

import java.util.concurrent.CountDownLatch;

/**
 * @author lkxed
 */
public class IndentApplication {
    public static void main(String[] args) throws InterruptedException {
        /* register IndentService provider */
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        ApplicationConfig application = new ApplicationConfig("indent-service");
        application.setQosPort(22223);
        bootstrap.application(application);
        bootstrap.registry(new RegistryConfig(ZooKeeperConstant.zkServers));
        bootstrap.protocol(new ProtocolConfig("dubbo", 20883));
        ServiceConfig<IndentService> service = new ServiceConfig<>();
        service.setInterface(IndentService.class);
        service.setRef(new IndentServiceImpl());
        bootstrap.service(service);
        bootstrap.start();
        /* in order for the program to run continuously */
        new CountDownLatch(1).await();
    }
}
