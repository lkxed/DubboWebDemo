package tool;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;

public class DubboTool {
    public static <T> T getInstance(Class<T> clazz, DubboBootstrap bootstrap) {
        ReferenceConfig<T> reference = new ReferenceConfig<>();
        reference.setBootstrap(bootstrap);
        reference.setInterface(clazz);
        reference.setCheck(false);
        return reference.get();
    }
}
