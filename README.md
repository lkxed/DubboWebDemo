# dubbo-web-demo
A web application with Dubbo as RPC framework &amp; ZooKeeper as its registry center yet **without Spring**.

Nowadays, more and more Java applications see the Spring Framework as one of their must-depend libraries. The framework itself claims to be lightweight while sometimes our applications ought to be lighte, by "lighter" I mean "without it". Thus, I write this demo to explore the world without Spring which might be a better place, especially when each microservice is too simple to wear even that "lightweight" armor. 

I hope this could be enlightening in a way.

## Register Services without Spring

### Provider

```java
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
```
### Consumer

```java
/* initialize application & registry configuration */
DubboBootstrap bootstrap = DubboBootstrap.getInstance();
bootstrap.application(new ApplicationConfig("IndentService"));
bootstrap.registry(new RegistryConfig(ZooKeeperConstant.zkServers));
/* initialize AccountService consumer instance */
accountService = DubboTool.getInstance(AccountService.class, bootstrap);
/* initialize MerchandiseService consumer instance */
merchandiseService = DubboTool.getInstance(MerchandiseService.class, bootstrap);
```

```java
public class DubboTool {
    public static <T> T getInstance(Class<T> clazz, DubboBootstrap bootstrap) {
        ReferenceConfig<T> reference = new ReferenceConfig<>();
        reference.setBootstrap(bootstrap);
        reference.setInterface(clazz);
        reference.setCheck(false);
        return reference.get();
    }
}
```

## Setup HTTP Server without Spring

```java
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
```

```java
BusinessServer server = new BusinessServer("localhost", 8080);
server.start();
```
