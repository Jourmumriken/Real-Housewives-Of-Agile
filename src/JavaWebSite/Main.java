package JavaWebSite;

import java.io.IOException;

public class Main {
//main
    public static void main(String[] args) throws IOException {
        MultiPageHttpServer server = new MultiPageHttpServer(); 
        server.start();
    }
}
