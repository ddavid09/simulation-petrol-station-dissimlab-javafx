package pl.dawidziak.model;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;

public class NewClientEvent extends BasicSimEvent<Environment, Object> {

    private static int counter = 0;

    public NewClientEvent(Environment entity, double delay) throws SimControlException {
        super(entity, delay);
    }

    @Override
    protected void stateChange() throws SimControlException {
        counter++;
        var environment = getSimObj();
        if(counter < environment.simParameters.clientAmount){
            RandomGenerator RandomGen = new RandomGenerator();


            double delay = RandomGen.generate(environment.simParameters.fuelChoiceDistrib);

            System.out.println(environment.simParameters.fuelChoiceDistrib.toString() + " = " + delay);

        }

    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
