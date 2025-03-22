public class TragedyCalculator extends PerformanceCalculator{
    public TragedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public double amount() throws Exception {
        double result = 40000;
        if (this.performance.audience() > 30){
            result += 1000 * (this.performance.audience() - 30);
        }
        return result;
    }
}
