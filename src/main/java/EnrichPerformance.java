public class EnrichPerformance {

    String playID;
    int audience;
    Play play;
    double amount;

    public EnrichPerformance(){
    }

    public EnrichPerformance(String playID, int audience, Play play, double amount) {
        this.playID = playID;
        this.audience = audience;
        this.play = play;
        this.amount = amount;
    }

    public String getPlayID() {
        return playID;
    }

    public void setPlayID(String playID) {
        this.playID = playID;
    }

    public int getAudience() {
        return audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
