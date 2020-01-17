package pl.dawidziak.model;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

import java.util.Arrays;
import java.util.List;

public class FinishWashEvent extends BasicSimEvent<Environment, Object> {

    public FinishWashEvent(Environment entity, double delay) throws SimControlException {
        super(entity, delay);
    }

    @Override
    protected void stateChange() throws SimControlException {
        Environment environment = getSimObj();
        Stand stand = environment.washStand;
        Client client = stand.getStoredClient();
        System.out.println(String.format("%-14.4f", simTime()) + "Klient nr " + client.idNumber + " zakończył mycie w myjni");
        environment.incrementServicedAmount();

        if(!environment.queueToWash.isEmpty()){
            stand.setStoredClient(environment.queueToWash.remove(0));
            new StartWashEvent(environment, 0);
        }else{
            stand.setStoredClient(null);
        }
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
