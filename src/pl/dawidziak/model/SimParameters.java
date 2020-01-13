package pl.dawidziak.model;


public class SimParameters {
    public final int clientAmount;
    public final int postAmount;
    public final int postQueueSize;
    public final int counterAmount;
    public final Distribution clientDistrib;
    public final Distribution fuelChoiceDistrib;
    public final Distribution PBtankTimeDistrib;
    public final Distribution LPGtankTimeDistrib;
    public final Distribution ONtankTimeDistrib;
    public final Distribution carWashChoiceDistrib;

    public SimParameters(int clientAmount, int postAmount, int postQueueSize, int counterAmount, Distribution clientDistrib, Distribution fuelChoiceDistrib, Distribution PBtankTimeDistrib, Distribution LPGtankTimeDistrib, Distribution ONtankTimeDistrib, Distribution carWashChoiceDistrib) {
        this.clientAmount = clientAmount;
        this.postAmount = postAmount;
        this.postQueueSize = postQueueSize;
        this.counterAmount = counterAmount;
        this.clientDistrib = clientDistrib;
        this.fuelChoiceDistrib = fuelChoiceDistrib;
        this.PBtankTimeDistrib = PBtankTimeDistrib;
        this.LPGtankTimeDistrib = LPGtankTimeDistrib;
        this.ONtankTimeDistrib = ONtankTimeDistrib;
        this.carWashChoiceDistrib = carWashChoiceDistrib;
    }
}
