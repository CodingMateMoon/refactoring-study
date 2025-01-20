import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StatementTest {

    @Test
    void statement() throws Exception {
        JsonArray invoices = Statement.loadJsonArray("json/invoices.json");
        JsonObject plays = Statement.loadJson("json/plays.json");

        for (JsonElement jsonElement : invoices) {

            JsonObject invoice = jsonElement.getAsJsonObject();
            String expected = """
청구 내역 (고객명: BigCo)
  Hamlet: $650.00 (55석)
  As You Like it: $580.00 (35석)
  Othello: $500.00 (40석)
총액: $1,730.00
적립 포인트: 47점
""";
            Assertions.assertThat(new Statement().statement(invoice, plays).stripTrailing()).isEqualTo(expected.stripTrailing());
        }
    }
}