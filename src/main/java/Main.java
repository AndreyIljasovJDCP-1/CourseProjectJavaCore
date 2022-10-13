import com.google.gson.Gson;
import statistics.Category;
import statistics.Request;
import statistics.Response;
import statistics.Statistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Main {

    private static final int PORT = 8989;

    public static void main(String[] args) {
        Gson gson = new Gson();
        Map<String, String> titleMap = Statistic.createTitleMapFromTSV("categories.tsv");
        Statistic statistic = new Statistic(titleMap);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(
                             clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(
                             clientSocket.getInputStream()))) {

                    String jsonString = in.readLine();
                    Request request = gson.fromJson(jsonString, Request.class);

                    statistic.addToCategoryMap(request);

                    Category maxCategory = statistic.getMaxCategory();
                    Response response = new Response(maxCategory);

                    String responseJsonString = gson.toJson(response);
                    out.println(responseJsonString);

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
