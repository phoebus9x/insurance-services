package serverSettings;

public class Main {
    public static void main(String[] args) {
        ConfigRead.initialize();
        ThreadServer threadServer = new ThreadServer(ConfigRead.PORT);
        new Thread(threadServer).start();
    }
}
