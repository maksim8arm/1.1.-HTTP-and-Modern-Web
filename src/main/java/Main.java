import java.io.BufferedOutputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        final var server = new Server(64);

        server.addHandler("GET", "/messages",
                new Handler() {
                    @Override
                    public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                        sendResponse("Hello from GET /message", responseStream, request);
                    }
                }
        );

        server.addHandler("POST", "/messages",
                new Handler() {
                    @Override
                    public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                        sendResponse("Hello from POST /message", responseStream, request);
                    }
                }
        );

        server.listen(9998);
    }

    private static void sendResponse(String response, BufferedOutputStream out, Request request) throws IOException {
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: " + (response.length() + request.showQueryToScreen(request.getDataQueryStr()).length() + "\r\n".length()) + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());

        out.write(response.getBytes());

        //
        out.write((
                "\r\n" + request.showQueryToScreen(request.getDataQueryStr())
        ).getBytes());

        out.flush();

    }
}
