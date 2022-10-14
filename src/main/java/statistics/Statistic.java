package statistics;

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
    private Map<String, Integer> categoryMap;
    private final List<Request> requestList;

    public Statistic(Map<String, String> titleMap) {
        this.titleMap = titleMap;
        this.categoryMap = new TreeMap<>();
        this.requestList = new ArrayList<>();
    }

    public void addToCategoryMap(Request request) {
        String category = titleMap.getOrDefault(request.getTitle(), "другое");
        categoryMap.merge(category, request.getSum(), Integer::sum);
    }

    public void addToRequestList(Request request) {
        requestList.add(request);
    }

    public Category getMaxCategory() {
        Optional<Map.Entry<String, Integer>> max = categoryMap.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));
        return max.map(kv -> new Category(kv.getKey(), kv.getValue())).orElse(null);
    }

    public Category getMaxCategoryByFilter(String date) {
        Map<String, Integer> map = new TreeMap<>();
        for (Request request : requestList) {
            if (request.getDate().contains(date)) {
                String category = titleMap.getOrDefault(request.getTitle(), "другое");
                map.merge(category, request.getSum(), Integer::sum);
            }
        }
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

    public Map<String, Integer> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map<String, Integer> categoryMap) {
        this.categoryMap = new TreeMap<>(categoryMap);
    }

    public List<Request> getRequestList() {
        return requestList;
    }
}
