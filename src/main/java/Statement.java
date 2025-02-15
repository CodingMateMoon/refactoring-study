import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Statement {

    private final JsonObject invoice;
    private final JsonObject plays;
    private final StatementData statementData;

    public Statement(JsonObject invoice, JsonObject plays) {
        this.invoice = invoice;
        this.plays = plays;
        Gson gson = new Gson();
        this.statementData = gson.fromJson(this.invoice, new TypeToken<StatementData>() {}.getType());
    }

    public String statement() throws Exception {
        return renderPlainText();
    }

    private String renderPlainText() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("청구 내역 (고객명: %s)\n", this.statementData.customer() ));

        for(Performance performance: this.statementData.performances()){
            int perfAudience = performance.audience();

            sb.append(String.format("  %s: %s (%d석) \n", playFor(performance).get("name").getAsString(), usd(amountFor(performance)), perfAudience));
        }

        sb.append(String.format("총액: %s\n", usd(totalAmount())));
        sb.append(String.format("적립 포인트: %d점\n", totalVolumeCredits()));

        return sb.toString();
    }

    private double totalAmount() throws Exception {
        double result = 0;
        for(Performance aPerformance: this.statementData.performances()){
            result += amountFor(aPerformance);
        }
        return result;
    }

    private static String usd(double aNumber) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        currencyFormatter.setMinimumFractionDigits(2);
        return currencyFormatter.format(aNumber/100);
    }

    private int volumeCreditsFor(Performance aPerformance) {
        int volumeCredits = 0;
        int perfAudience = aPerformance.audience();
        volumeCredits += Math.max(perfAudience - 30, 0);
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy".equals(playFor(aPerformance).get("type").getAsString())) {
            volumeCredits += perfAudience / 5;
        }
        return volumeCredits;
    }

    private int totalVolumeCredits() {
       int result = 0;
       for(Performance aPerformance: this.statementData.performances()){
           result += volumeCreditsFor(aPerformance);
       }
       return result;
    }

    private JsonObject playFor(Performance aPerformance) {
        return this.plays.getAsJsonObject(aPerformance.playID());
    }

    private double amountFor(Performance aPerformance) throws Exception {
        double result;
        int aPerformanceAudience = aPerformance.audience();
        switch(playFor(aPerformance).get("type").getAsString()) {
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
                throw new Exception(String.format("알 수 없는 장르: %s", playFor(aPerformance).get("type").getAsString()));
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
