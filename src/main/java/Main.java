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

    private static final int PORT = 8085;

    public static void main(String[] args) {

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
                    //формируем объект Request из строки Json
                    Request request=statistic.getRequestFromJsonString(jsonString);
                    //добавляем в мапу Категорий
                    statistic.addToCategoryMap(request);
                    // ищем максимальную категорию
                    Category maxCategory=statistic.getMaxCategory();
                    //создаем объект Response
                    Response response=new Response(maxCategory);
                    //переводим объект Response  в строку json
                    String responseJsonString = statistic.getJsonStringFromResponse(response);
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
