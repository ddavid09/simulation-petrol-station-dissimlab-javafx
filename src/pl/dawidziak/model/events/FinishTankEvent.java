package pl.dawidziak.model.events;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.dawidziak.model.*;
import pl.dawidziak.view.EnvironmentChangeListener;

import java.util.Arrays;
import java.util.List;

public class FinishTankEvent extends BasicSimEvent<Environment, Stand> {

    public FinishTankEvent(Environment entity, double delay, Stand o) throws SimControlException {
        super(entity, delay, o);
    }

    @Override
    protected void stateChange() throws SimControlException {
        Environment environment = getSimObj();
        Stand stand = transitionParams;
        Client client = stand.getStoredClient();
        environment.monitors.serviceTime.setValue(simTime()-client.getStartTime());
        client.setStartWashTime(simTime());
        List<Stand> standsList = Arrays.asList(environment.fuelStands);
        int standIndex = standsList.indexOf(stand)+1;
        System.out.println(String.format("%-14.4f", simTime()) + "Klient nr " + client.idNumber + " zakończył tankowanie " + client.getFuel().name() + " na stanowisku nr " + standIndex);

        Distribution distribution;
        switch (client.getFuel()){
            case ON:
                distribution = environment.simParameters.ONtankTimeDistrib;
                break;
            case PB:
                distribution = environment.simParameters.PBtankTimeDistrib;
                break;
            case LPG:
                distribution = environment.simParameters.LPGtankTimeDistrib;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + client.getFuel());
        }
        RandomGenerator RandomGen = new RandomGenerator();
        double delay = RandomGen.generate(distribution);

        if(environment.queueToCounters.isEmpty()){
            for(int i=0; i<environment.simParameters.counterAmount;i++){
                if(environment.counterStands[i].getStoredClient() == null){
                    environment.counterStands[i].setStoredClient(client);

                    new StartPayEvent(environment, 0, environment.counterStands[i]);
                    break;
                }else if(i == environment.counterStands.length-1){
                    environment.queueToCounters.add(client);
                    System.out.println(String.format("%-14.4f", simTime()) + "Dodano klienta nr " + client.idNumber + " do kolejki oczekiwania do kas\t\t\t stan kolejki: " + environment.queueToCounters.size());
                }
            }
        }else{
            environment.queueToCounters.add(client);
            System.out.println(String.format("%-14.4f", simTime()) + "Dodano klienta nr " + client.idNumber + " do kolejki oczekiwania do kas\t\t\t stan kolejki: " + environment.queueToCounters.size());
        }

        if(!environment.queueToFuelStands.isEmpty()){
            stand.setStoredClient(environment.queueToFuelStands.remove(0));
            environment.monitors.sizeQueueFuel.setValue(environment.queueToFuelStands.size());
            new StartTankEvent(environment, 0, stand);
        }else{
            stand.setStoredClient(null);
        }

        slowSim(environment);
    }

    private void slowSim(Environment environment) {
        environment.simManager.pauseSimulation();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        environment.simManager.resumeSimulation();
    }


    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }

}
