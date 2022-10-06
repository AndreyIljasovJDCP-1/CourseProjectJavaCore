package statistics;

public class Category {
    private String category;
    private int sum;

    public Category(String category, int sum) {
        this.category = category;
        this.sum = sum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "\ncategory: " + category + "\nsum: " + sum;
    }
}
