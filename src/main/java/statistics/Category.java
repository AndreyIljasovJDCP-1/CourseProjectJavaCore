package statistics;

public class Category {
    private final String category;
    private final int sum;

    public Category(String category, int sum) {
        this.category = category;
        this.sum = sum;
    }

    @Override
    public String toString() {
        return String.format("{category: %s sum:%d}", category, sum);
    }
}
