package pl.dawidziak.model.events;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.dawidziak.model.*;
import pl.dawidziak.view.EnvironmentChangeListener;

import java.util.Arrays;
import java.util.List;

public class StartPayEvent extends BasicSimEvent<Environment, Stand> {

    private EnvironmentChangeListener listener;

    public StartPayEvent(Environment entity, double delay, Stand o) throws SimControlException {
        super(entity, delay, o);
    }

    @Override
    protected void stateChange() throws SimControlException {
        Environment environment = getSimObj();
        Stand stand = transitionParams;
        Client client = stand.getStoredClient();
        List<Stand> standsList = Arrays.asList(environment.counterStands);
        int standIndex = standsList.indexOf(stand)+1;
        System.out.println(String.format("%-14.4f", simTime()) + "Klient nr " + client.idNumber + " rozpoczal placenie w kasie nr " + standIndex);
        //Poniewaz w poleceniu nie zostal sparametryzowany czas placenia w kasie, czasy zostaly wylosowane z rozkladem takim jak czas tankowania PB oraz podzielone przez 3
        //poniewaz czas placenia jest zawsze krotszy niz czas tankowania
        RandomGenerator RandomGen = new RandomGenerator();
        Distribution distribution= environment.simParameters.PBtankTimeDistrib;
        double delay = RandomGen.generate(distribution);
        new FinishPayEvent(environment, delay/3, stand);
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
