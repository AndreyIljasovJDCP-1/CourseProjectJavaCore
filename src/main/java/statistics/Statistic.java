package statistics;

import com.google.gson.Gson;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Statistic {
    private static final char SEPARATOR = '\t';
    private final Map<String, String> titleMap;
    private  Map<String, Integer> categoryMap;

    public Statistic(Map<String, String> titleMap) {
        this.titleMap = titleMap;
        this.categoryMap = new TreeMap<>();
    }

    public Request getRequestFromJsonString(String requestJsonString) {
        Gson gson = new Gson();
        return gson.fromJson(requestJsonString, Request.class);
    }

    public void addToCategoryMap(Request request) {
        String category = titleMap.getOrDefault(request.getTitle(), "другое");
        categoryMap.merge(category, request.getSum(), Integer::sum);
    }

    public Category getMaxCategory() {
        Optional<Map.Entry<String, Integer>> max = categoryMap.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));
        return max.map(kv -> new Category(kv.getKey(), kv.getValue())).orElse(null);
    }

    public String getJsonStringFromResponse(Response response) {
        Gson gson = new Gson();
        return gson.toJson(response);
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

    public Map<String, Integer> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map<String, Integer> categoryMap) {
        this.categoryMap = new TreeMap<>(categoryMap);
    }
}
