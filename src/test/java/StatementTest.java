import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatementTest {

    @Test
    void statement() throws Exception {
        JsonArray invoices = Statement.loadJsonArray("json/invoices.json");
        JsonObject plays = Statement.loadJson("json/plays.json");
        Gson gson = new Gson();
        int index = 0;

        for (JsonElement jsonElement : invoices) {

            JsonObject invoice = jsonElement.getAsJsonObject();
            String expected = """
청구 내역 (고객명: BigCo)
  Hamlet: $650.00 (55석)\s
  As You Like it: $580.00 (35석)\s
  Othello: $500.00 (40석)\s
총액: $1,730.00
적립 포인트: 47점
""";

            Assertions.assertThat(new Statement(invoice, plays).statement()).isEqualTo(expected);
        }
    }
}