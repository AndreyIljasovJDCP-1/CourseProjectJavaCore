package statistics;

public class Response {
    private Category maxCategory;

    public Response(Category maxCategory) {
        this.maxCategory = maxCategory;
    }

    public Category getMaxCategory() {
        return maxCategory;
    }

    public void setMaxCategory(Category maxCategory) {
        this.maxCategory = maxCategory;
    }

    @Override
    public String toString() {
        return "maxCategory: " + maxCategory;
    }
}
