import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    ExecutorService executorService;
    int port;

    public Server(int size, int port) {
        this.executorService = Executors.newFixedThreadPool(size);
        this.port = port;
    }

    public void start() {
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                executorService.submit(new Pre_server(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
