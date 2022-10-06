package statistics;

import java.io.Serializable;

public class Request implements Serializable {
    private final String title;
    private final String date;
    private final int sum;

    public Request(String title, String date, int sum) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return String.format("название: %s  дата: %s сумма: %s", title, date, sum);
    }
}
