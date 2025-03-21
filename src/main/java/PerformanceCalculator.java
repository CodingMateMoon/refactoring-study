public class PerformanceCalculator {
    Performance performance;
    Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public Play getPlay() {
        return play;
    }

    public double amount() throws Exception {
        double result;
        int aPerformanceAudience = this.performance.audience();
        switch(this.play.type()) {
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
                throw new Exception(String.format("알 수 없는 장르: %s", play.type()));
        }
        return result;
    }
    public int volumeCredits() {
        int volumeCredits = 0;
        int perfAudience = this.performance.audience();
        volumeCredits += Math.max(perfAudience - 30, 0);
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy".equals(this.play.type())) {
            volumeCredits += perfAudience / 5;
        }
        return volumeCredits;
    }

}
