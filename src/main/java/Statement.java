import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import netscape.javascript.JSObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;

public class Statement {
    public static void main(String[] args) throws Exception {
        try {
            JsonArray invoices = loadJsonArray("json/invoices.json");
            JsonObject plays = loadJson("json/plays.json");

            for (JsonElement jsonElement : invoices) {

                JsonObject invoice = jsonElement.getAsJsonObject();
                new Statement().statement(invoice, plays);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void statement(JsonObject invoice, JsonObject plays) throws Exception {
        double totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("청구 내역 (고객명: %s)\n", invoice.get("customer").getAsString() ));

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        currencyFormatter.setMinimumFractionDigits(2);

        for(JsonElement perfElement: invoice.getAsJsonArray("performances")){
            JsonObject perf = perfElement.getAsJsonObject();
            JsonObject play = plays.getAsJsonObject(perf.get("playID").getAsString());
            double thisAmount = 0;
            String playType = play.get("type").getAsString();
            int perfAudience = perf.get("audience").getAsInt();

            switch(playType) {
                case "tragedy": // 비극
                    thisAmount = 40000;
                    if (perfAudience > 30) {
                        thisAmount += 1000 * (perfAudience - 30);
                    }
                    break;
                case "comedy": // 희극
                    thisAmount = 30000;
                    if (perfAudience > 20) {
                        thisAmount += 10000 + 500 * (perfAudience - 20);
                    }
                    thisAmount += 300 * perfAudience;
                    break;

                default:
                    throw new Exception(String.format("알 수 없는 장르: %s", playType));
            }

            // 포인트를 적립한다.
            volumeCredits += Math.max(perfAudience - 30, 0);
            // 희극 관객 5명마다 추가 포인트를 제공한다.
            if ("comedy".equals(playType)) {
                volumeCredits += perfAudience / 5;
            }

            sb.append(String.format("  %s: %s (%d석) \n", play.get("name").getAsString(), currencyFormatter.format(thisAmount/100), perfAudience));
            totalAmount += thisAmount;
        }


        sb.append(String.format("총액: %s\n", currencyFormatter.format(totalAmount/100)));
        sb.append(String.format("적립 포인트: %d점\n", volumeCredits));

        System.out.println(sb.toString());
    }

    private static JsonObject loadJson(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private static JsonArray loadJsonArray(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonArray();
        }
    }
}
