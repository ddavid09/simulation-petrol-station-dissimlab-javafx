package pl.dawidziak.model.events;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.dawidziak.model.Client;
import pl.dawidziak.model.ClientType;
import pl.dawidziak.model.Environment;
import pl.dawidziak.model.RandomGenerator;
import pl.dawidziak.view.EnvironmentChangeListener;

public class NewClientEvent extends BasicSimEvent<Environment, Object>  {

    private static int clientCounter = 0;

    public NewClientEvent(Environment entity, double delay) throws SimControlException  {
        super(entity, delay);
    }

    @Override
    protected void stateChange() throws SimControlException {
        var environment = getSimObj();
        if(clientCounter < environment.simParameters.clientAmount){
            clientCounter++;
            Client mannedClient = new Client(clientCounter, simTime());
            RandomGenerator RandomGen = new RandomGenerator();
            mannedClient.DrawLotClientType(RandomGen, environment.simParameters.carWashChoiceDistrib);
            if(mannedClient.getClientType() != ClientType.ONLY_WASH){
                mannedClient.DrawLotFuelType(RandomGen, environment.simParameters.PBtankTimeDistrib,
                        environment.simParameters.ONtankTimeDistrib,
                        environment.simParameters.LPGtankTimeDistrib);
            }
            System.out.println(String.format("%-14.4f", simTime()) + "Utworzono klienta nr " + mannedClient.idNumber + " " + mannedClient.fuelTypeToString());
            serveClient(environment, mannedClient);
            double dalay = RandomGen.generate(environment.simParameters.clientDistrib);
            new NewClientEvent(environment, dalay);
        }
        environment.simManager.pauseSimulation();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        environment.simManager.resumeSimulation();
    }

    private void serveClient(Environment environment, Client client) throws SimControlException {
        switch (client.getClientType()){
            case ONLY_FUEL:
            case FUEL_WASH:
                if(environment.queueToFuelStands.isEmpty()){
                    for(int i=0; i<environment.fuelStands.length; i++){
                        if(environment.fuelStands[i].getStoredClient() == null){
                            environment.fuelStands[i].setStoredClient(client);
                            new StartTankEvent(environment, 0, environment.fuelStands[i]);
                            break;
                        }else if(i == environment.fuelStands.length-1){
                            environment.queueToFuelStands.add(client);
                            environment.monitors.sizeQueueFuel.setValue(environment.queueToFuelStands.size());
                            System.out.println(String.format("%-14.4f", simTime()) + "Dodano klienta nr " + client.idNumber + " do kolejki oczekiwania do stanowisk\t\t\t stan kolejki: " + environment.queueToFuelStands.size() + "/" + environment.simParameters.postQueueSize);
                        }
                    }
                }else{
                    if(environment.queueToFuelStands.add(client)){
                        environment.monitors.sizeQueueFuel.setValue(environment.queueToFuelStands.size());
                        System.out.println(String.format("%-14.4f", simTime()) + "Dodano klienta nr " + client.idNumber + " do kolejki oczekiwania do stanowisk\t\t\t stan kolejki: " + environment.queueToFuelStands.size() + "/" + environment.simParameters.postQueueSize);
                    }else{
                        environment.incrementLostAmount();
                        environment.monitors.lostClient.setValue(environment.getLostClientAmount());
                        System.out.println(String.format("%-14.4f", simTime()) + "Utracono klienta nr " + client.idNumber);
                    }
                }
                break;
            case ONLY_WASH:
                if(environment.queueToCounters.isEmpty()){
                    for(int i=0; i<environment.counterStands.length; i++){
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
        }
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }


}
