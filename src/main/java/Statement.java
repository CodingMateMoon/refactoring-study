import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Statement {

    private final Invoice invoice;
    private final Map<String, Play> plays;
    private final StatementData statementData;

    public Statement(JsonObject jsonInvoice, JsonObject plays) throws Exception {
        Gson gson = new Gson();
        this.invoice= gson.fromJson(jsonInvoice, new TypeToken<Invoice>() {}.getType());
        this.plays = gson.fromJson(plays, new TypeToken<Map<String, Play>>() {}.getType());
        this.statementData = createStatementData(this.invoice, this.plays);
    }

    public StatementData createStatementData(Invoice invoice, Map<String, Play> plays) throws Exception {
        List<EnrichPerformance> enrichPerformances = new ArrayList<>();

        for (Performance performance : invoice.performances()) {
            enrichPerformances.add(enrichPerformance(performance));
        }
        StatementData statementData = new StatementData(invoice.customer(), enrichPerformances);
        statementData.setTotalAmount(totalAmount(statementData));
        statementData.setTotalVolumeCredits(totalVolumeCredits(statementData));
        return statementData;
    }

    private EnrichPerformance enrichPerformance(Performance performance) throws Exception {
        PerformanceCalculator calculator = new PerformanceCalculator(performance, playFor(performance));
        EnrichPerformance enrichPerformance = new EnrichPerformance(performance.playID(), performance.audience(), calculator.getPlay(), calculator.amount(), 0);
        enrichPerformance.setVolumeCredits(volumeCreditsFor(enrichPerformance));
        return enrichPerformance;
    }

    public String statement() throws Exception {
        return renderPlainText();
    }

    private String renderPlainText() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("청구 내역 (고객명: %s)\n", this.statementData.getCustomer() ));

        for(EnrichPerformance performance: this.statementData.getEnrichPerformances()){
            int perfAudience = performance.getAudience();

            sb.append(String.format("  %s: %s (%d석) \n", performance.getPlay().name(), usd(performance.getAmount()), perfAudience));
        }

        sb.append(String.format("총액: %s\n", usd(this.statementData.getTotalAmount())));
        sb.append(String.format("적립 포인트: %d점\n", this.statementData.getTotalVolumeCredits()));

        return sb.toString();
    }

    private double totalAmount(StatementData statementData) throws Exception {
        return statementData.getEnrichPerformances().stream().mapToDouble(EnrichPerformance::getAmount).sum();
    }

    private static String usd(double aNumber) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        currencyFormatter.setMinimumFractionDigits(2);
        return currencyFormatter.format(aNumber/100);
    }

    private int volumeCreditsFor(EnrichPerformance aPerformance) {
        int volumeCredits = 0;
        int perfAudience = aPerformance.getAudience();
        volumeCredits += Math.max(perfAudience - 30, 0);
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy".equals(aPerformance.getPlay().type())) {
            volumeCredits += perfAudience / 5;
        }
        return volumeCredits;
    }

    private int totalVolumeCredits(StatementData statementData) {
       return statementData.getEnrichPerformances().stream().mapToInt(EnrichPerformance::getVolumeCredits).sum();
    }

    private Play playFor(Performance aPerformance) {
        return this.plays.get(aPerformance.playID());
    }

    private double amountFor(Performance aPerformance) throws Exception {
        return new PerformanceCalculator(aPerformance, playFor(aPerformance)).amount();
    }

    public static JsonObject loadJson(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    public static JsonArray loadJsonArray(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonArray();
        }
    }
}
