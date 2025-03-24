public class ComedyCalculator extends PerformanceCalculator{
    public ComedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public double amount() throws Exception {
        double result = 30000;
        if (this.performance.audience() > 20) {
            result += 10000 + 500 * (this.performance.audience() - 20);
        }
        result += 300 * this.performance.audience();
        return result;
    }

    @Override
    public int volumeCredits() {
        return super.volumeCredits() + (this.performance.audience() / 5);
    }
}
