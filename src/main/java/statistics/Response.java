package statistics;

public class Response {
    private final Category maxCategory;
    private final Category maxDayCategory;
    private final Category maxMonthCategory;
    private final Category maxYearCategory;

    public Response(
            Category maxCategory,
            Category maxDayCategory,
            Category maxMonthCategory,
            Category maxYearCategory
    ) {
        this.maxCategory = maxCategory;
        this.maxDayCategory = maxDayCategory;
        this.maxMonthCategory = maxMonthCategory;
        this.maxYearCategory = maxYearCategory;
    }

    @Override
    public String toString() {
        return String.format("maxCategory: %s\nmaxDayCategory: %s\nmaxMonthCategory: %s\nmaxYearCategory: %s",
                maxCategory, maxDayCategory, maxMonthCategory, maxYearCategory);
    }
}
