import java.util.Collections;
import java.util.List;

public record StatementData(String customer, List<EnrichPerformance> enrichPerformances) {
    public StatementData(String customer, List<EnrichPerformance> enrichPerformances) {
        this.customer = customer;
        this.enrichPerformances= List.copyOf(enrichPerformances);
    }
}
