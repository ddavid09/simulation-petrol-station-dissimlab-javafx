package pl.dawidziak.model.events;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.dawidziak.model.Client;
import pl.dawidziak.model.ClientType;
import pl.dawidziak.model.Environment;
import pl.dawidziak.model.Stand;
import pl.dawidziak.view.EnvironmentChangeListener;

import java.util.Arrays;
import java.util.List;

public class FinishPayEvent extends BasicSimEvent<Environment, Stand> {

    private EnvironmentChangeListener listener;

    public FinishPayEvent(Environment entity, double delay, Stand o) throws SimControlException {
        super(entity, delay, o);
    }

    @Override
    protected void stateChange() throws SimControlException {
        Environment environment = getSimObj();
        Stand stand = transitionParams;
        Client client = stand.getStoredClient();
        List<Stand> standsList = Arrays.asList(environment.counterStands);
        int standIndex = standsList.indexOf(stand)+1;
        System.out.println(String.format("%-14.4f", simTime()) + "Klient nr " + client.idNumber + " zakończył placenie w kasie nr " + standIndex);

        if(client.getClientType() != ClientType.ONLY_FUEL){
            if(environment.washStand.getStoredClient() == null){
                environment.washStand.setStoredClient(client);
                new StartWashEvent(environment, 0);
            }else{
                environment.queueToWash.add(client);
                environment.monitors.sizeQueueWash.setValue(environment.queueToWash.size());
                System.out.println(String.format("%-14.4f", simTime()) + "Dodano klienta nr " + client.idNumber + " do kolejki oczekiwania do myjni\t\t\t stan kolejki: " + environment.queueToWash.size());
            }
        }else{
            environment.incrementServicedAmount();

        }

        if(!environment.queueToCounters.isEmpty()){
            stand.setStoredClient(environment.queueToCounters.remove(0));
            new StartPayEvent(environment, 0, stand);
        }else{
            stand.setStoredClient(null);
        }
    }

    public void setListener(EnvironmentChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
