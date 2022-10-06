package statistics;

import org.junit.jupiter.api.*;

@DisplayName("Тест класса Statistic.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatisticTest {

    @Test
    @Order(1)
    @DisplayName("Тест: создание HashMap из tsv файла")
    void createTitleMapFromTSVReturnHashMap() {

        Assertions.assertEquals("java.util.HashMap",
                Statistic.createTitleMapFromTSV("categories.tsv").getClass().getName());
    }
}
