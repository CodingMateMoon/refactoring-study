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
        throw new Exception("서브클래스에서 처리하도록 설계되었습니다.");
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
