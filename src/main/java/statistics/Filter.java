package statistics;

public class Filter {
    private final String date;

    public Filter(String date) {
        this.date = date;
    }

    public String getDay() {
        return date;
    }

    public String getMonth() {
        return String.join(".",
                date.split("\\.")[0],
                date.split("\\.")[1]
        );
    }
    public String getYear() {
        return date.split("\\.")[0];
    }
}
