package statistics;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@DisplayName("Тест класса Statistic.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StatisticTest {
    private static Map<String, String> titleMap;
    private final Comparator<Category> categoryComparator = (o1, o2) ->
            Objects.equals(o1.getCategory(), o2.getCategory())
                    && o1.getSum() == o2.getSum()
                    ? 0 : -1;
    private final Comparator<Request> requestComparator = (o1, o2) ->
            Objects.equals(o1.getTitle(), o2.getTitle())
                    && Objects.equals(o1.getDate(), o2.getDate())
                    && o1.getSum() == o2.getSum()
                    ? 0 : -1;

    @BeforeAll
    static void setUpApp() {
        titleMap = Statistic.createTitleMapFromTSV("categories.tsv");
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
    @DisplayName("Тест: размер HashMap из tsv файла")
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
    @DisplayName("Тест: проверка карты категорий")
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
        Assertions.assertEquals(101, statistic.getCategoryMap().get("быт"));
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
        Assertions.assertEquals(101, statistic.getCategoryMap().get("другое"));
    }

    @Test
    @Order(7)
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
        Category expected = new Category("быт", 100);
        int comparing = categoryComparator.compare(expected, statistic.getMaxCategory());
        Assertions.assertEquals(0, comparing);
    }

}