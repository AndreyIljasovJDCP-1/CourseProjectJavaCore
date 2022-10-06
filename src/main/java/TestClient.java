import java.io.*;
import java.net.Socket;

public class TestClient {
    private static final int PORT = 8085;
    private static final String HOST = "localhost";


    public static void main(String[] args) {
        try (Socket clientSocket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     clientSocket.getInputStream()))) {

            String stringJson = "";

            try (BufferedReader reader = new BufferedReader(new FileReader("request.json"))) {
                while (reader.ready()) {
                    stringJson = reader.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            out.println(stringJson);

            System.out.println(in.readLine());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
