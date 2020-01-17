package pl.dawidziak.model;

import dissimlab.simcore.BasicSimObj;

public class Environment extends BasicSimObj {
    public final SimParameters simParameters;

    public final LimitedQueue queueToFuelStands;
    public final Queue queueToCounters;
    public final Queue queueToWash;
    public final Stand[] fuelStands;
    public final Stand[] counterStands;
    public final Stand washStand;

    private int lostClientAmount;
    private int servicedClientAmount;

    public Environment(SimParameters simParameters) {
        this.simParameters = simParameters;

        queueToFuelStands = new LimitedQueue(simParameters.postQueueSize);
        queueToCounters = new Queue();
        queueToWash = new Queue();
        fuelStands = new Stand[simParameters.postAmount];
        for(int i=0; i<fuelStands.length; i++){
            fuelStands[i] = new Stand();
        }
        counterStands = new Stand[simParameters.counterAmount];
        for(int i=0; i<counterStands.length; i++){
            counterStands[i] = new Stand();
        }
        washStand = new Stand();

        log();
    }

    public void incrementLostAmount(){
        lostClientAmount++;
    }
    public void incrementServicedAmount() {servicedClientAmount++;}

    public int getServicedClientAmount() {
        return servicedClientAmount;
    }

    public int getLostClientAmount() {
        return lostClientAmount;
    }

    private void log(){
        System.out.println("Utworzono Srodowisko symulacji");
        System.out.println("Limit klientow: \t\t\t\t" + simParameters.clientAmount);
        System.out.println("Liczba stanowisk: \t\t\t\t" + simParameters.postAmount);
        System.out.println("Rozmiar kolejki: \t\t\t\t" + simParameters.postQueueSize);
        System.out.println("Liczba kas: \t\t\t\t\t" + simParameters.counterAmount);
        System.out.println("Rozklad prawdop. tworzenia klientow: \t\t" + simParameters.clientDistrib.toString());
        System.out.println("Rozklad prawdop. losowania PB: \t\t\t\t" + simParameters.PBtankTimeDistrib.toString());
        System.out.println("Rozklad prawdop. losowania ON: \t\t\t\t" + simParameters.ONtankTimeDistrib.toString());
        System.out.println("Rozklad prawdop. losowania LPG: \t\t\t" + simParameters.LPGtankTimeDistrib.toString());
        System.out.println("Rozklad prawdop. losowania myjni \t\t\t" + simParameters.carWashChoiceDistrib.toString());
        System.out.println("Rozklad prawdop. wyboru paliwa: \t\t\t" + simParameters.fuelChoiceDistrib.toString());

    }
}
