package statistics;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

@DisplayName("Тест класса Statistic.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StatisticTest {
     private static Map<String, String> testMap;

    @BeforeAll
    static void setUp() {
        testMap = Statistic.createTitleMapFromTSV("categories.tsv");

    }

    @Test
    @Order(1)
    @DisplayName("Тест: создание HashMap из tsv файла")
    void createTitleMapFromTSVReturnHashMap() {

        Assertions.assertEquals("java.util.HashMap",
                testMap.getClass().getName());
    }

    @Test
    @Order(2)
    @DisplayName("Тест: размер HashMap из tsv файла")
    void createTitleMapFromTSVReturnMapSize() {

        Assertions.assertEquals(8, testMap.size());
    }

    @Test
    @Order(3)
    @DisplayName("Тест: файла categories.tsv не существует")
    void createTitleMapFromTSVFileNotFoundThrowException() {

        Assertions.assertThrows(RuntimeException.class, ()->Statistic.createTitleMapFromTSV("nofile.tsv"));
    }

    @Order(4)
    @DisplayName("Тест: проверка категорий")
    @ParameterizedTest
    @MethodSource("getArguments")
    void createTitleMapFromTSVReturnRightHashMap(String input, String expected) {

        Assertions.assertEquals(expected, testMap.get(input));
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
}
