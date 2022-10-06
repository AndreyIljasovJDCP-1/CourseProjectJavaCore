import statistics.Statistic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Main {

    private static final int PORT = 8085;

    public static void main(String[] args) {
        File autoSavesData = new File("data.bin");
        Statistic statistic;
        if (autoSavesData.exists()) {
            System.out.println("Restore history...");
            statistic = Statistic.loadFromBinFile(autoSavesData);
            statistic.getRequestList().forEach(System.out::println);
        } else {
            Map<String, String> titleMap = Statistic.createTitleMapFromTSV("categories.tsv");
            statistic = new Statistic(titleMap);
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(
                             clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(
                             clientSocket.getInputStream()))) {
                    String request = in.readLine();
                    String response = statistic.processingRequest(request);
                    out.println(response);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
