package statistics;

public class Response {
    private Category maxCategory;
    private final Category maxDayCategory;
    private final Category maxMonthCategory;
    private final Category maxYearCategory;

    public Response(Category maxCategory, Category maxDayCategory, Category maxMonthCategory, Category maxYearCategory) {
        this.maxCategory = maxCategory;
        this.maxDayCategory = maxDayCategory;
        this.maxMonthCategory = maxMonthCategory;
        this.maxYearCategory = maxYearCategory;
    }

    public Category getMaxCategory() {
        return maxCategory;
    }

    public void setMaxCategory(Category maxCategory) {
        this.maxCategory = maxCategory;
    }

    @Override
    public String toString() {
        return "maxCategory: " + maxCategory
                + "\nmaxDayCategory: " + maxDayCategory
                + "\nmaxMonthCategory: " + maxMonthCategory
                + "\nmaxYearCategory: " + maxYearCategory;
    }
}
