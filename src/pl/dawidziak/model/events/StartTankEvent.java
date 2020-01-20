package pl.dawidziak.model.events;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.dawidziak.model.*;
import pl.dawidziak.view.EnvironmentChangeListener;

import java.util.Arrays;
import java.util.List;

public class StartTankEvent extends BasicSimEvent<Environment, Stand> {

    public StartTankEvent(Environment entity, double delay, Stand o) throws SimControlException {
        super(entity, delay, o);
    }

    @Override
    protected void stateChange() throws SimControlException, IllegalStateException {
        Environment environment = getSimObj();
        Stand stand = transitionParams;
        Client client = stand.getStoredClient();
        List<Stand>  standsList = Arrays.asList(environment.fuelStands);
        int standIndex = standsList.indexOf(stand)+1;
        System.out.println(String.format("%-14.4f", simTime()) + "Klient nr " + client.idNumber + " rozpoczal tankowanie " + client.getFuel().name() + " na stanowisku nr " + standIndex);
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
        new FinishTankEvent(environment, delay, stand);
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
