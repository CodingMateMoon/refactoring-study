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
        int totalAmount = 0;
        int volumeCredits = 0;
        String result = String.format("청구 내역 (고객명: %s)\n", invoice.get("customer").getAsString() );
        System.out.println(result);
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
