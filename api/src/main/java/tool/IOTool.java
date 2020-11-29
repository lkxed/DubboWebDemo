package tool;

import java.io.Closeable;
import java.io.IOException;

public class IOTool {
    public static void quietlyClose(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
