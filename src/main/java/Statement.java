import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Statement {

    private final Invoice invoice;
    private final Map<String, Play> plays;
    private final StatementData statementData;

    public Statement(JsonObject jsonInvoice, JsonObject plays) {
        Gson gson = new Gson();
        this.invoice= gson.fromJson(jsonInvoice, new TypeToken<Invoice>() {}.getType());
        this.plays = gson.fromJson(plays, new TypeToken<Map<String, Play>>() {}.getType());
        this.statementData = generateStatement(this.invoice, this.plays);
    }

    public StatementData generateStatement(Invoice invoice, Map<String, Play> plays) {
        List<EnrichPerformance> enrichedPerformances = invoice.performances().stream()
                .map(performance -> new EnrichPerformance(
                        performance.playID(),
                        performance.audience(),
                        playFor(performance)))
                .collect(Collectors.toList());

        return new StatementData(invoice.customer(), enrichedPerformances);
    }

    public String statement() throws Exception {
        return renderPlainText();
    }

    private String renderPlainText() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("청구 내역 (고객명: %s)\n", this.statementData.customer() ));

        for(EnrichPerformance performance: this.statementData.enrichPerformances()){
            int perfAudience = performance.audience();

            sb.append(String.format("  %s: %s (%d석) \n", performance.play().name(), usd(amountFor(performance)), perfAudience));
        }

        sb.append(String.format("총액: %s\n", usd(totalAmount())));
        sb.append(String.format("적립 포인트: %d점\n", totalVolumeCredits()));

        return sb.toString();
    }

    private double totalAmount() throws Exception {
        double result = 0;
        for(EnrichPerformance aPerformance: this.statementData.enrichPerformances()){
            result += amountFor(aPerformance);
        }
        return result;
    }

    private static String usd(double aNumber) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        currencyFormatter.setMinimumFractionDigits(2);
        return currencyFormatter.format(aNumber/100);
    }

    private int volumeCreditsFor(EnrichPerformance aPerformance) {
        int volumeCredits = 0;
        int perfAudience = aPerformance.audience();
        volumeCredits += Math.max(perfAudience - 30, 0);
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy".equals(aPerformance.play().type())) {
            volumeCredits += perfAudience / 5;
        }
        return volumeCredits;
    }

    private int totalVolumeCredits() {
       int result = 0;
       for(EnrichPerformance aPerformance: this.statementData.enrichPerformances()){
           result += volumeCreditsFor(aPerformance);
       }
       return result;
    }

    private Play playFor(Performance aPerformance) {
        return this.plays.get(aPerformance.playID());
    }

    private double amountFor(EnrichPerformance aPerformance) throws Exception {
        double result;
        int aPerformanceAudience = aPerformance.audience();
        switch(aPerformance.play().type()) {
            case "tragedy": // 비극
                result = 40000;
                if (aPerformanceAudience > 30) {
                    result += 1000 * (aPerformanceAudience - 30);
                }
                break;
            case "comedy": // 희극
                result = 30000;
                if (aPerformanceAudience > 20) {
                    result += 10000 + 500 * (aPerformanceAudience - 20);
                }
                result += 300 * aPerformanceAudience;
                break;

            default:
                throw new Exception(String.format("알 수 없는 장르: %s", aPerformance.play().type()));
        }
        return result;
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
