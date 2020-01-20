package pl.dawidziak.view;

import dissimlab.monitors.Diagram;
import dissimlab.monitors.Statistics;
import dissimlab.simcore.SimControlException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import pl.dawidziak.model.Client;
import pl.dawidziak.model.Environment;
import pl.dawidziak.model.Monitors;
import pl.dawidziak.model.Stand;
import pl.dawidziak.model.events.NewClientEvent;

import java.awt.*;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SimAnimationController implements Initializable {

    @FXML
    public Label standsQueueStatusLabel;
    @FXML
    public Label countersQueueStatusLabel;
    @FXML
    public Label washQueueStatusLabel;
    @FXML
    public VBox simResultsInfoVBox;

    private Environment simEnvironment;

    @FXML
    public Canvas standsQueueLayer;
    @FXML
    public Canvas standsLayer;
    @FXML
    public Canvas countersQueueLayer;
    @FXML
    public Canvas countersLayer;
    @FXML
    public Canvas washQueueLayer;
    @FXML
    public Canvas washLayer;
    @FXML
    public VBox standsQueueStatus;
    @FXML
    public VBox standsStatus;
    @FXML
    public VBox counterQueueStatus;
    @FXML
    public VBox countersStatus;
    @FXML
    public VBox washQueueStatus;
    @FXML
    public VBox washStatus;

    private ArrayList<Sprite> carsList;
    private Image fuelStandImg;
    private Image counterStandImg;
    private Image washStandImg;
    private Image clientof;
    private Image clientwf;
    private Image clientow;

    private GraphicsContext tankQueueGC;
    private GraphicsContext tankStandsGC;
    private GraphicsContext countersQueueGC;
    private GraphicsContext countersStandsGC;
    private GraphicsContext washQueueGC;
    private GraphicsContext washStandGC;

    public void setEnvironment(Environment environment){
        simEnvironment = environment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carsList = new ArrayList<Sprite>();

        fuelStandImg = new Image("stand-p.png");
        counterStandImg = new Image("stand-c.png");
        washStandImg = new Image("stand-cw.png");
        clientof = new Image("car-of.png");
        clientow = new Image("car-ow.png");
        clientwf = new Image("car-fw.png");

        tankStandsGC = standsLayer.getGraphicsContext2D();
        tankQueueGC = standsQueueLayer.getGraphicsContext2D();
        countersQueueGC = countersQueueLayer.getGraphicsContext2D();
        countersStandsGC = countersLayer.getGraphicsContext2D();
        washQueueGC = washQueueLayer.getGraphicsContext2D();
        washStandGC = washLayer.getGraphicsContext2D();

        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50),
                (EventHandler) event -> {
                    updateEnvironment();
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateEnvironment() {

        standsQueueStatusLabel.setText(simEnvironment.queueToFuelStands.size() + "/" + simEnvironment.simParameters.postQueueSize);
        countersQueueStatusLabel.setText(simEnvironment.queueToCounters.size() + "");
        washQueueStatusLabel.setText(simEnvironment.queueToWash.size() + "");
        tankQueueGC.clearRect(0, 0, standsQueueLayer.getWidth(), standsQueueLayer.getHeight());
        int i = 0;
        for (Client client : simEnvironment.queueToFuelStands) {

            switch (client.getClientType()){
                case ONLY_FUEL:
                    tankQueueGC.drawImage(clientof, 10+(80*i), 20);
                    break;
                case FUEL_WASH:
                    tankQueueGC.drawImage(clientwf, 10+(80*i), 20);
                    break;
                case ONLY_WASH:
                    tankQueueGC.drawImage(clientow, 10+(80*i), 20);
            }
            i++;
        }
        i = 0;
        for (Stand stand : simEnvironment.fuelStands){
            if(stand.getStoredClient() == null){
                tankStandsGC.clearRect(80+(150*i), 20, 64, 64);
            }else{
                switch (stand.getStoredClient().getClientType()){
                    case ONLY_FUEL:
                        tankStandsGC.clearRect(80+(150*i), 20, 64, 64);
                        tankStandsGC.drawImage(clientof, 80+(150*i), 20);
                        break;
                    case FUEL_WASH:
                        tankStandsGC.clearRect(80+(150*i), 20, 64, 64);
                        tankStandsGC.drawImage(clientwf, 80+(150*i), 20);
                        break;
                    case ONLY_WASH:
                        tankStandsGC.clearRect(80+(150*i), 20, 64, 64);
                        tankStandsGC.drawImage(clientow, 80+(150*i), 20);
                }
            }
            i++;
        }

        i=0;
        countersQueueGC.clearRect(0, 0, standsQueueLayer.getWidth(), standsQueueLayer.getHeight());
        for (Client client : simEnvironment.queueToCounters) {

            switch (client.getClientType()){
                case ONLY_FUEL:
                    countersQueueGC.drawImage(clientof, 10+(80*i), 20);
                    break;
                case FUEL_WASH:
                    countersQueueGC.drawImage(clientwf, 10+(80*i), 20);
                    break;
                case ONLY_WASH:
                    countersQueueGC.drawImage(clientow, 10+(80*i), 20);
            }
            i++;
        }

        i = 0;
        for (Stand stand : simEnvironment.counterStands){
            if(stand.getStoredClient() == null){
                countersStandsGC.clearRect(80+(150*i), 20, 64, 64);
            }else{
                switch (stand.getStoredClient().getClientType()){
                    case ONLY_FUEL:
                        countersStandsGC.clearRect(80+(150*i), 20, 64, 64);
                        countersStandsGC.drawImage(clientof, 80+(150*i), 20);
                        break;
                    case FUEL_WASH:
                        countersStandsGC.clearRect(80+(150*i), 20, 64, 64);
                        countersStandsGC.drawImage(clientwf, 80+(150*i), 20);
                        break;
                    case ONLY_WASH:
                        countersStandsGC.clearRect(80+(150*i), 20, 64, 64);
                        countersStandsGC.drawImage(clientow, 80+(150*i), 20);
                }
            }
            i++;
        }

        i=0;
        washQueueGC.clearRect(0, 0, standsQueueLayer.getWidth(), standsQueueLayer.getHeight());
        for (Client client : simEnvironment.queueToWash) {

            switch (client.getClientType()){
                case ONLY_FUEL:
                    washQueueGC.drawImage(clientof, 10+(80*i), 20);
                    break;
                case FUEL_WASH:
                    washQueueGC.drawImage(clientwf, 10+(80*i), 20);
                    break;
                case ONLY_WASH:
                    washQueueGC.drawImage(clientow, 10+(80*i), 20);
            }
            i++;
        }


        if(simEnvironment.washStand.getStoredClient() == null){
            washStandGC.clearRect(80, 20, 64, 64);
        }else{
            switch (simEnvironment.washStand.getStoredClient().getClientType()){
                case ONLY_FUEL:
                    washStandGC.clearRect(80, 20, 64, 64);
                    washStandGC.drawImage(clientof, 80, 20);
                    break;
                case FUEL_WASH:
                    washStandGC.clearRect(80, 20, 64, 64);
                    washStandGC.drawImage(clientwf, 80, 20);
                    break;
                case ONLY_WASH:
                    washStandGC.clearRect(80, 20, 64, 64);
                    washStandGC.drawImage(clientow, 80, 20);
            }
        }

        if(simEnvironment.isFinished){
            printSimResults(simEnvironment.monitors);
            simEnvironment.isFinished = false;
        }

    }


    public void drawParameters() {
        for(int i=0; i<simEnvironment.simParameters.postAmount; i++){
            tankStandsGC.drawImage(fuelStandImg, 10+(150*i), 20);
        }

        for(int i=0; i<simEnvironment.simParameters.counterAmount; i++){
            countersStandsGC.drawImage(counterStandImg, 10+(150*i), 20);
        }

        washStandGC.drawImage(washStandImg, 10, 20);
    }


    public void printSimResults(Monitors monitored){
        var container = simResultsInfoVBox.getChildren();
        container.add(new Label("Liczba obsluzonych klientow: " + simEnvironment.getServicedClientAmount()));
        container.add(new Label("Liczba straconych klientow: " + simEnvironment.getLostClientAmount()));
        container.add(new Label("Srednia liczba klientow w kolejce do stanowisk: " + Statistics.arithmeticMean(monitored.sizeQueueFuel)));
        container.add(new Label("Srednia liczba klientow w kolejce do myjni " + Statistics.arithmeticMean(monitored.sizeQueueWash)));
        container.add(new Label("Sredni czas tankowania samochodu: " + Statistics.arithmeticMean(monitored.serviceTime)));
        container.add(new Label("Sredni czas mycia samochodu: " + Statistics.arithmeticMean(monitored.washTime)));
        container.add(new Label("Prawdopodobienstwo rezygnacji z obslugi przez kierowce: " + ((double)simEnvironment.getLostClientAmount()/simEnvironment.simParameters.clientAmount)));
    }
}
