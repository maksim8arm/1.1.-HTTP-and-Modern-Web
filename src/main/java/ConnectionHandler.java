import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {

    private final Socket socket;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers;

    public ConnectionHandler(Socket socket, ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers) {
        this.socket = socket;
        this.handlers = handlers;
    }

    public void run() throws IOException {

        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {

            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                return;
            }

            final String quaryParams;
            if (!parts[1].contains("?")) {
                quaryParams = null;
            } else {
                int beginQueryStr = parts[1].indexOf('?') + 1;
                quaryParams = parts[1].substring(beginQueryStr, parts[1].length());
            }

            var request = new Request(parts[0], parts[1], parts[2], null, null, quaryParams);

            System.out.println(request.getQueryParam("name"));
            System.out.println(request.getQueryParams());

            if (!handlers.containsKey(request.getMethod())) {
                send404notFound(out);
                return;
            }

            var pathHandlers = handlers.get(request.getMethod());

            if (!pathHandlers.containsKey(request.getPath())) {
                send404notFound(out);
                return;
            }

            var handler = pathHandlers.get(request.getPath());

            try {
                handler.handle(request, out);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                send500InternalServerError(out);
            }
        }

    }


    private void send404notFound(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    private void send500InternalServerError(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 500 Internal Server Error\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }
}

