package pl.dawidziak.model;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

import java.util.Arrays;
import java.util.List;

public class StartWashEvent extends BasicSimEvent<Environment, Object> {

    public StartWashEvent(Environment entity, double delay) throws SimControlException {
        super(entity, delay);
    }

    @Override
    protected void stateChange() throws SimControlException {
        Environment environment = getSimObj();
        Stand stand = environment.washStand;
        Client client = stand.getStoredClient();
        System.out.println(String.format("%-14.4f", simTime()) + "Klient nr " + client.idNumber + " rozpoczal mycie w myjni ");
        //identycznie jak z czasem obslugi przy kasie - nie zostal on sparametryzowany zadnym rozkladem, wykorzystuje w takim wypadku
        //rozklad dla czasu tankowania benzyny a wartosc wygenerowana mnoze razy 2
        RandomGenerator RandomGen = new RandomGenerator();
        Distribution distribution = environment.simParameters.PBtankTimeDistrib;
        double delay = RandomGen.generate(distribution);
        new FinishWashEvent(environment, delay*2);
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
