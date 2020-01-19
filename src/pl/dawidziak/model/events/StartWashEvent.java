package pl.dawidziak.model.events;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.dawidziak.model.*;
import pl.dawidziak.view.EnvironmentChangeListener;

public class StartWashEvent extends BasicSimEvent<Environment, Object> {

    private EnvironmentChangeListener listener;

    public StartWashEvent(Environment entity, double delay, EnvironmentChangeListener listener) throws SimControlException {
        super(entity, delay);
        this.listener = listener;
    }

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
        //rozklad dla czasu tankowania benzyny
        RandomGenerator RandomGen = new RandomGenerator();
        Distribution distribution = environment.simParameters.PBtankTimeDistrib;
        double delay = RandomGen.generate(distribution);
        new FinishWashEvent(environment, delay, environment.environmentChangeListener);
        listener.reprintEnvironment();
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
