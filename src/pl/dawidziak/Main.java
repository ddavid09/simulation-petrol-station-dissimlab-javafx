package pl.dawidziak;

import dissimlab.monitors.Diagram;
import dissimlab.monitors.Statistics;
import dissimlab.simcore.SimControlException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.dawidziak.model.*;

import dissimlab.simcore.SimManager;
import pl.dawidziak.model.events.NewClientEvent;
import pl.dawidziak.view.EnvironmentChangeListener;
import pl.dawidziak.view.SimAnimationController;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("view/loadParameters.fxml"));
//        primaryStage.setTitle("Symulacja stacji Paliw - Wprowadz parametry");
//        primaryStage.setScene(new Scene(root, 540, 400));
//        primaryStage.show();
//        //primaryStage.close(); //step over window show

        Parent simRoot;
        try{
            SimManager simManager = new SimManager();
            Monitors monitored = new Monitors(simManager);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/simAnimation.fxml"));
            simRoot = loader.load();
            SimAnimationController controller = loader.getController();
            Stage simStage = new Stage();
            simStage.setScene(new Scene(simRoot, 1300, 838));
            simStage.setTitle("Symulacja");

            Environment environment = mockEnvironment(monitored, controller, simManager);
            controller.setEnvironment(environment);
            controller.drawParameters();

            simStage.show();

            simulation(controller, environment, simManager, monitored);

            controller.printSimResults(monitored);



        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

    private static void simulation(EnvironmentChangeListener listener, Environment environment, SimManager simManager, Monitors monitored){
        try {
            new NewClientEvent(environment, 0, environment.environmentChangeListener);
        } catch (SimControlException e) {
            e.printStackTrace();
        }

        try {
            simManager.startSimulation();
        } catch (SimControlException e) {
            e.printStackTrace();
        }

        System.out.println("Liczba obsluzonych klientow: " + environment.getServicedClientAmount());
        System.out.println("Liczba straconych klientow: " + environment.getLostClientAmount());
        System.out.println("Srednia liczba klientow w kolejce do stanowisk: " + Statistics.arithmeticMean(monitored.sizeQueueFuel));
        System.out.println("Srednia liczba klientow w kolejce do myjni " + Statistics.arithmeticMean(monitored.sizeQueueWash));
        System.out.println("Sredni czas tankowania samochodu: " + Statistics.arithmeticMean(monitored.serviceTime));
        System.out.println("Sredni czas mycia samochodu: " + Statistics.arithmeticMean(monitored.washTime));
        System.out.println("Prawdopodobienstwo rezygnacji z obslugi przez kierowce: " + ((double)environment.getLostClientAmount()/environment.simParameters.clientAmount));

        Diagram diagram = new Diagram(Diagram.DiagramType.TIME_FUNCTION, "Liczba samochodow w kolejkach");
        diagram.add(monitored.sizeQueueFuel, Color.BLACK, "do stanowisk");
        diagram.add(monitored.sizeQueueWash, Color.RED, "do myjni");
        diagram.show();

        Diagram diagramst = new Diagram(Diagram.DiagramType.DISTRIBUTION, "Czas tankowania/mycia samochodu");
        diagramst.add(monitored.serviceTime, Color.BLACK, "tankowania");
        diagramst.add(monitored.washTime, Color.RED, "mycia");
        diagramst.show();

        Diagram diagramla = new Diagram(Diagram.DiagramType.TIME_FUNCTION, "Liczba rezygnacji");
        diagramla.add(monitored.lostClient, Color.BLUE);
        diagramla.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static Environment mockEnvironment(Monitors monitors, EnvironmentChangeListener listener, SimManager simManager){

        int clientAmount = 500;
        int postAmount = 7; //wizualizacja max 7
        int postQueueSize = 80;
        int counterAmount = 3;

        Distribution clientDistrib = new Distribution(DistributionName.exponential, 5);
        Distribution PBtankTimeDistrib = new Distribution(DistributionName.uniform, 0, 100);
        Distribution ONtankTimeDistrib = new Distribution(DistributionName.erlang, 100, 2);
        Distribution LPGtankTimeDistrib = new Distribution(DistributionName.normal, 50, 50);
        Distribution carWashChoiceDistrib = new Distribution(DistributionName.normal, 30, 40);
        Distribution fuelChoiceDistrib = new Distribution(DistributionName.gamma, 5, 2);

        //SimParameters
        var simParams = new SimParameters(clientAmount, postAmount, postQueueSize, counterAmount, clientDistrib, fuelChoiceDistrib, PBtankTimeDistrib, LPGtankTimeDistrib, ONtankTimeDistrib, carWashChoiceDistrib);
        //Environment
        return new Environment(simParams, simManager, monitors, listener);
    }

}
