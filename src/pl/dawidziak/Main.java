package pl.dawidziak;

import dissimlab.simcore.SimControlException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.dawidziak.model.*;

import dissimlab.simcore.SimManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/loadParameters.fxml"));
        primaryStage.setTitle("Symulacja stacji Paliw - Wprowadz parametry");
        primaryStage.setScene(new Scene(root, 540, 400));
        primaryStage.show();
        primaryStage.close(); //step over window show
    }


    public static void main(String[] args) {
        launch(args);
        var environment = mockEnvironment();
        var simManager = new SimManager();

        try {
            new NewClientEvent(environment, 0);
        } catch (SimControlException e) {
            e.printStackTrace();
        }
        try {
            simManager.startSimulation();
        } catch (SimControlException e) {
            e.printStackTrace();
        }

    }

    private static Environment mockEnvironment(){
        int LAMBDA = 5;

        int clientAmount = 1500;
        int postAmount = 4;
        int postQueueSize = 35;
        int counterAmount = 3;
        //Distributions
        Number[] exponentialParam = {5};
        Number[] uniformParam = {10, 20};
        Number[] erlangParam = {5, 1};
        Number[] normalParam = {0, 1};
        Number[] chiParam = {1};
        Number[] weibullParam = {1, 3};

        Distribution clientDistrib = new Distribution(DistributionName.exponential, exponentialParam);
        Distribution PBtankTimeDistrib = new Distribution(DistributionName.uniform, uniformParam);
        Distribution ONtankTimeDistrib = new Distribution(DistributionName.erlang, erlangParam);
        Distribution LPGtankTimeDistrib = new Distribution(DistributionName.normal, normalParam);
        Distribution carWashChoiceDistrib = new Distribution(DistributionName.chisquare, chiParam);
        Distribution fuelChoiceDistrib = new Distribution(DistributionName.weibull, weibullParam);

        //SimParameters
        var simParams = new SimParameters(clientAmount, postAmount, postQueueSize, counterAmount, clientDistrib, fuelChoiceDistrib, PBtankTimeDistrib, LPGtankTimeDistrib, ONtankTimeDistrib, carWashChoiceDistrib);
        //Environment
        return new Environment(simParams);
    }
}
