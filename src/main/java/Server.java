import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    ExecutorService executorService;

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap<>();

    public Server(int size) {
        this.executorService = Executors.newFixedThreadPool(size);
    }

    public void addHandler(String method, String path, Handler handler) {
        if (!handlers.containsKey(method))
            handlers.put(method, new ConcurrentHashMap<>());
        handlers.get(method).put(path, handler);
    }

    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                final var socket = serverSocket.accept();
                executorService.submit(() -> {
                    try {
                        new ConnectionHandler(socket, handlers).run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
