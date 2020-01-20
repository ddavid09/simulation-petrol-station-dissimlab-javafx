package pl.dawidziak.model.events;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.dawidziak.model.Client;
import pl.dawidziak.model.Environment;
import pl.dawidziak.model.Stand;
import pl.dawidziak.view.EnvironmentChangeListener;

public class FinishWashEvent extends BasicSimEvent<Environment, Object> {

    public FinishWashEvent(Environment entity, double delay) throws SimControlException {
        super(entity, delay);
    }

    @Override
    protected void stateChange() throws SimControlException {
        Environment environment = getSimObj();
        Stand stand = environment.washStand;
        Client client = stand.getStoredClient();
        environment.monitors.washTime.setValue(simTime()-client.getStartWashTime());
        System.out.println(String.format("%-14.4f", simTime()) + "Klient nr " + client.idNumber + " zakończył mycie w myjni");
        environment.incrementServicedAmount();

        if(!environment.queueToWash.isEmpty()){
            stand.setStoredClient(environment.queueToWash.remove(0));
            environment.monitors.sizeQueueWash.setValue(environment.queueToWash.size());
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
