package pl.dawidziak.model;

import dissimlab.simcore.BasicSimObj;

public class Environment extends BasicSimObj {
    private SimParameters simParameters;

    public Environment(SimParameters simParameters) {
        this.simParameters = simParameters;

        log();
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
