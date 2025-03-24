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
        return Math.max(this.performance.audience() - 30, 0);
    }
}
