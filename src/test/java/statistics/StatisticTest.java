package statistics;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.Map;
import java.util.stream.Stream;

@DisplayName("Тест класса Statistic.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StatisticTest {
    private static Map<String, String> titleMap;

    @BeforeAll
    static void setUpApp() {
        titleMap = Statistic.createTitleMapFromTSV("categories.tsv");
    }

    @AfterAll
    static void tearDownAll() {
        titleMap = null;
    }

    @Test
    @Order(1)
    @DisplayName("Тест: создание HashMap из tsv файла")
    void createTitleMapFromTSVReturnHashMap() {
        Assertions.assertEquals("java.util.HashMap",
                titleMap.getClass().getName());
    }

    @Test
    @Order(2)
    @DisplayName("Тест: размер HashMap из tsv файла = 8")
    void createTitleMapFromTSVReturnMapSize() {
        Assertions.assertEquals(8, titleMap.size());
    }

    @Test
    @Order(3)
    @DisplayName("Тест: файла categories.tsv не существует")
    void createTitleMapFromTSVFileNotFoundThrowException() {
        Assertions.assertThrows(RuntimeException.class,
                () -> Statistic.createTitleMapFromTSV("nofile.tsv"));
    }

    @Order(4)
    @DisplayName("Тест: проверка карты категорий titleMap")
    @ParameterizedTest
    @MethodSource("getArguments")
    void createTitleMapFromTSVReturnRightHashMap(String input, String expected) {
        Assertions.assertEquals(expected, titleMap.get(input));
    }

    private static Stream<Arguments> getArguments() {
        return Stream.of(
                Arguments.of("булка", "еда"),
                Arguments.of("колбаса", "еда"),
                Arguments.of("сухарики", "еда"),
                Arguments.of("курица", "еда"),
                Arguments.of("тапки", "одежда"),
                Arguments.of("шапка", "одежда"),
                Arguments.of("мыло", "быт"),
                Arguments.of("акции", "финансы")
        );
    }

    @Test
    @Order(5)
    @DisplayName("Тест: добавить данные из запроса в статистику по категориям")
    void addToCategoryMapUpdateCategoryMap() {
        Request request = new Request("мыло", "2022.02.10", 1);
        Statistic statistic = new Statistic(titleMap);
        statistic.setCategoryMap(
                Map.of(
                        "еда", 100,
                        "финансы", 100,
                        "одежда", 100,
                        "быт", 100
                )
        );
        statistic.addToCategoryMap(request);
        int expected = 101;
        int actual = statistic.getCategoryMap().get("быт");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(6)
    @DisplayName("Тест: добавить новый продукт в категорию \"другое\"")
    void addToCategoryMapAnotherCategory() {
        Request request = new Request("новый продукт", "2022.02.10", 101);
        Statistic statistic = new Statistic(titleMap);
        statistic.setCategoryMap(
                Map.of(
                        "еда", 100,
                        "финансы", 100,
                        "одежда", 100,
                        "быт", 100
                )
        );
        statistic.addToCategoryMap(request);
        int expected = 101;
        int actual = statistic.getCategoryMap().get("другое");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(7)
    @DisplayName("Тест: добавить запрос в список")
    void addToRequestListCheckListSize() {
        Request request = new Request("мыло", "2022.02.10", 1);
        Statistic statistic = new Statistic(titleMap);
        statistic.addToRequestList(request);
        int expected = 1;
        int actual = statistic.getRequestList().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(8)
    @DisplayName("Тест: найти max категорию (в алфавитном порядке).")
    void getMaxCategoryReturnMaxCategory() {
        Statistic statistic = new Statistic(titleMap);
        statistic.setCategoryMap(
                Map.of(
                        "еда", 100,
                        "финансы", 100,
                        "одежда", 100,
                        "быт", 100
                )
        );
        String expected = "быт";
        String actual = statistic.getMaxCategory().getCategory();
        Assertions.assertEquals(expected, actual);
    }

    @Order(9)
    @DisplayName("Тест: найти мах категорию по фильтру")
    @ParameterizedTest
    @MethodSource("getArguments1")
    void getMaxCategoryByFilter(String expected, String date) {
        Statistic statistic = new Statistic(titleMap);

        statistic.addToCategoryMap(new Request("булка", "2022.02.10", 10));
        statistic.addToRequestList(new Request("булка", "2022.02.10", 10));
        statistic.addToCategoryMap(new Request("акции", "2022.01.11", 10));
        statistic.addToRequestList(new Request("акции", "2022.01.11", 10));
        statistic.addToCategoryMap(new Request("курица", "2022.02.13", 10));
        statistic.addToRequestList(new Request("курица", "2022.02.13", 10));
        statistic.addToCategoryMap(new Request("мыло", "2022.02.13", 10));
        statistic.addToRequestList(new Request("мыло", "2022.02.13", 10));

        String maxCategory = statistic.getMaxCategoryByFilter(date).getCategory();

        Assertions.assertEquals(expected, maxCategory);
    }

    private static Stream<Arguments> getArguments1() {
        return Stream.of(
                Arguments.of("еда", "2022"),
                Arguments.of("финансы", "2022.01"),
                Arguments.of("быт", "2022.02.13")
        );
    }

    @Test
    @Order(10)
    @DisplayName("Тест: сохранить и загрузить файл data.bin")
    void saveToBinFileLoadFromBinFileReturnRightStatisticObject() {
        File autoSave = new File("src/test/resources/data.bin");
        Statistic statistic = new Statistic(titleMap);

        statistic.addToCategoryMap(new Request("булка", "2022.02.10", 10));
        statistic.addToRequestList(new Request("булка", "2022.02.10", 10));
        statistic.addToCategoryMap(new Request("акции", "2022.01.11", 10));
        statistic.addToRequestList(new Request("акции", "2022.01.11", 10));
        statistic.addToCategoryMap(new Request("курица", "2022.02.13", 10));
        statistic.addToRequestList(new Request("курица", "2022.02.13", 10));
        statistic.addToCategoryMap(new Request("мыло", "2022.02.13", 10));
        statistic.addToRequestList(new Request("мыло", "2022.02.13", 10));

        statistic.saveToBinFile(autoSave);

        Statistic restoredStatistic = Statistic.loadFromBinFile(autoSave);

        int expected = restoredStatistic.getCategoryMap().get("еда");
        int actual = statistic.getCategoryMap().get("еда");

        Assertions.assertEquals(expected, actual);
    }

}