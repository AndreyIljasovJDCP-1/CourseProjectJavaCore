import com.google.gson.Gson;
import statistics.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Main {

    private static final int PORT = 8085;

    public static void main(String[] args) {
        File autoSave = new File("data.bin");
        Gson gson = new Gson();
        Statistic statistic;
        if (autoSave.exists()) {
            System.out.println("Restore history...");
            statistic = Statistic.loadFromBinFile(autoSave);
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
                    String jsonString = in.readLine();
                    Request request = gson.fromJson(jsonString, Request.class);

                    statistic.addToCategoryMap(request);
                    statistic.addToRequestList(request);

                    Filter filter = new Filter(request.getDate());

                    Category maxCategory = statistic.getMaxCategory();
                    Category maxDayCategory = statistic.getMaxCategory(filter.getDay());
                    Category maxMonthCategory = statistic.getMaxCategory(filter.getMonth());
                    Category maxYearCategory = statistic.getMaxCategory(filter.getYear());
                    Response response = new Response(
                            maxCategory,
                            maxDayCategory,
                            maxMonthCategory,
                            maxYearCategory
                    );

                    String responseJsonString = gson.toJson(response);
                    out.println(responseJsonString);

                    statistic.saveToBinFile(autoSave);
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
