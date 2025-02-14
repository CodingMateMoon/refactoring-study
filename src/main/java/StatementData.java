import java.util.Collections;
import java.util.List;

public record StatementData(String customer, List<Performance> performances) {
    public StatementData(String customer, List<Performance> performances) {
        this.customer = customer;
        this.performances = List.copyOf(performances);
    }
}
