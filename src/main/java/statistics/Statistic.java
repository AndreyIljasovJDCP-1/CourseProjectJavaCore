package statistics;

import com.google.gson.Gson;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Statistic implements Serializable {
    private static final char SEPARATOR = '\t';
    private final Map<String, String> titleMap;
    private final Map<String, Integer> categoryMap;
    private final List<Request> requestList;

    public Statistic(Map<String, String> titleMap) {
        this.titleMap = titleMap;
        this.categoryMap = new TreeMap<>();
        this.requestList = new ArrayList<>();
    }

    public String processingRequest(String stringJson) {
        Gson gson = new Gson();
        Request request = gson.fromJson(stringJson, Request.class);
        requestList.add(request);
        System.out.println();
        requestList.forEach(System.out::println);
        String category = titleMap.getOrDefault(request.getTitle(), "другое");
        categoryMap.merge(category, request.getSum(), Integer::sum);
        Category maxCategory = getMaxCategory();

        String day = request.getDate();
        String year = request.getDate().split("\\.")[0];
        String month = year + "." + request.getDate().split("\\.")[1];

        Category maxDayCategory = getMaxDayCategory(day);
        Category maxMonthCategory = getMaxMonthCategory(month);
        Category maxYearCategory = getMaxYearCategory(year);
        Response response = new Response(
                maxCategory,
                maxDayCategory,
                maxMonthCategory,
                maxYearCategory
        );

        saveToBinFile(new File("data.bin"));
        return gson.toJson(response);
    }

    public Category getMaxDayCategory(String date) {
        Map<String, Integer> map = new TreeMap<>();
        for (Request request : requestList) {
            if (request.getDate().equals(date)) {
                String category = titleMap.getOrDefault(request.getTitle(), "другое");
                map.merge(category, request.getSum(), Integer::sum);
            }
        }
        return getMaxCategory(map);
    }

    public Category getMaxMonthCategory(String date){
        Map<String, Integer> map = new TreeMap<>();
        for (Request request : requestList) {
            String month = String.join(".",
                    request.getDate().split("\\.")[0],
                    request.getDate().split("\\.")[1]
            );
            if (month.equals(date)) {
                String category = titleMap.getOrDefault(request.getTitle(), "другое");
                map.merge(category, request.getSum(), Integer::sum);
            }
        }
        return getMaxCategory(map);
    }
    public Category getMaxYearCategory(String date){
        Map<String, Integer> map = new TreeMap<>();
        for (Request request : requestList) {
            String year = request.getDate().split("\\.")[0];
            if (year.equals(date)) {
                String category = titleMap.getOrDefault(request.getTitle(), "другое");
                map.merge(category, request.getSum(), Integer::sum);
            }
        }
        return getMaxCategory(map);
    }
    public Category getMaxCategory() {
        Optional<Map.Entry<String, Integer>> max = categoryMap.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));
        return max.map(kv -> new Category(kv.getKey(), kv.getValue())).orElse(null);
    }

    public Category getMaxCategory(Map<String, Integer> map) {
        Optional<Map.Entry<String, Integer>> max = map.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));
        return max.map(kv -> new Category(kv.getKey(), kv.getValue())).orElse(null);
    }

    public void saveToBinFile(File textFile) {

        try (FileOutputStream fos = new FileOutputStream(textFile);
             ObjectOutputStream statsWriter = new ObjectOutputStream(fos)) {

            statsWriter.writeObject(this);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Statistic loadFromBinFile(File textFile) {
        Statistic statistic;

        try (FileInputStream fis = new FileInputStream(textFile);
             ObjectInputStream statsReader = new ObjectInputStream(fis)) {
            statistic = (Statistic) statsReader.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return statistic;
    }

    public static Map<String, String> createTitleMapFromTSV(String path) {
        Map<String, String> map;

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(SEPARATOR)
                .build();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(path))
                .withCSVParser(parser)
                .build()) {
            map = reader.readAll()
                    .stream()
                    .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения TSV файла " + e.getMessage());
        }
        return map;
    }

    public Map<String, String> getTitleMap() {
        return titleMap;
    }

    public Map<String, Integer> getCategoryMap() {
        return categoryMap;
    }

    public List<Request> getRequestList() {
        return requestList;
    }
    /*Map<String, Integer> map = requestList.stream()
                .filter(el -> el.getDate().equals(day))
                .collect(
                        Collectors.toMap(
                                el -> titleMap.getOrDefault(request.getTitle(), "другое"),
                                Request::getSum,
                                Integer::sum)
                );*/
}
