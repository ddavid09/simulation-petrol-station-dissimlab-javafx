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
        SimManager simManager = new SimManager();
        Environment environment = mockEnvironment();

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

        int clientAmount = 1500;
        int postAmount = 4;
        int postQueueSize = 35;
        int counterAmount = 3;

        Distribution clientDistrib = new Distribution(DistributionName.exponential, 1000);
        Distribution PBtankTimeDistrib = new Distribution(DistributionName.uniform, 0, 100);
        Distribution ONtankTimeDistrib = new Distribution(DistributionName.erlang, 100, 2);
        Distribution LPGtankTimeDistrib = new Distribution(DistributionName.normal, 50, 50);
        Distribution carWashChoiceDistrib = new Distribution(DistributionName.beta, 4, 4);
        Distribution fuelChoiceDistrib = new Distribution(DistributionName.gamma, 5, 2);

        //SimParameters
        var simParams = new SimParameters(clientAmount, postAmount, postQueueSize, counterAmount, clientDistrib, fuelChoiceDistrib, PBtankTimeDistrib, LPGtankTimeDistrib, ONtankTimeDistrib, carWashChoiceDistrib);
        //Environment
        return new Environment(simParams);
    }

//    private static void testDistributions(){
//        RandomGenerator testRandom = new RandomGenerator();
//        double val1 = testRandom.generate(new Distribution(DistributionName.uniform, 2, 5));
//        double val2 = testRandom.generate(new Distribution(DistributionName.exponential, 4));
//        double val3 = testRandom.generate(new Distribution(DistributionName.erlang, 100, 3));
//        double val4 = testRandom.generate(new Distribution(DistributionName.gamma, 5, 2));
//        double val5 = testRandom.generate(new Distribution(DistributionName.normal, 50, 50));
//        double val6 = testRandom.generate(new Distribution(DistributionName.chisquare, 4));
//        double val7 = testRandom.generate(new Distribution(DistributionName.beta, 4, 4));//<0;1>
//        double val8 = testRandom.generate(new Distribution(DistributionName.student, 3));
//        double val9 = testRandom.generate(new Distribution(DistributionName.lognormal, 2, 2));
//        double val10 = testRandom.generate(new Distribution(DistributionName.fdistribution, 2, 2));
//        double val11 = testRandom.generate(new Distribution(DistributionName.weibull, 2, 2));
//        double val12 = testRandom.generate(new Distribution(DistributionName.poisson, 4));//{0, 1, 2 ..}
//        double val13 = testRandom.generate(new Distribution(DistributionName.geometric, 0.15));//-||- param < 1 !
//        double bp = 0;
//    }
}
