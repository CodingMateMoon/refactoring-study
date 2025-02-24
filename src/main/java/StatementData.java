import java.util.Collections;
import java.util.List;

public class StatementData {
    String customer;
    List<EnrichPerformance> enrichPerformances;
    double totalAmount;
    int totalVolumeCredits;

    public StatementData(String customer, List<EnrichPerformance> enrichPerformances) {
        this.customer = customer;
        this.enrichPerformances = enrichPerformances;
    }

    public StatementData(String customer, List<EnrichPerformance> enrichPerformances, double totalAmount, int totalVolumeCredits) {
        this.customer = customer;
        this.enrichPerformances = List.copyOf(enrichPerformances);
        this.totalAmount = totalAmount;
        this.totalVolumeCredits = totalVolumeCredits;
    }

    public int getTotalVolumeCredits() {
        return totalVolumeCredits;
    }

    public void setTotalVolumeCredits(int totalVolumeCredits) {
        this.totalVolumeCredits = totalVolumeCredits;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<EnrichPerformance> getEnrichPerformances() {
        return enrichPerformances;
    }

    public void setEnrichPerformances(List<EnrichPerformance> enrichPerformances) {
        this.enrichPerformances = enrichPerformances;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
