package statistics;

import org.junit.jupiter.api.*;

@DisplayName("Тест класса Filter.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterTest {
    private static Filter filter;

    @BeforeAll
    static void setUpAll() {
        filter = new Filter("2022.10.13");

    }
    @AfterAll
    static void tearDownAll() {
        filter = null;
    }

    @Test
    @Order(1)
    @DisplayName("Тест: вернуть день 2022.10.13")
    void getDayReturnDay() {
        Assertions.assertEquals("2022.10.13", filter.getDay());
    }

    @Test
    @Order(2)
    @DisplayName("Тест: вернуть месяц 2022.10")
    void getMonthReturnMonth() {
        Assertions.assertEquals("2022.10", filter.getMonth());
    }

    @Test
    @Order(3)
    @DisplayName("Тест: вернуть год 2022")
    void getYearReturnYear() {
        Assertions.assertEquals("2022", filter.getYear());
    }
}