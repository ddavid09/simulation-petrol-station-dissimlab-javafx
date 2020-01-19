package pl.dawidziak.view;

import dissimlab.monitors.Statistics;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import pl.dawidziak.model.Environment;
import pl.dawidziak.model.Monitors;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SimAnimationController implements Initializable, EnvironmentChangeListener {

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

    private GraphicsContext tankQueueGC;
    private GraphicsContext tankStandsGC;
    private GraphicsContext countersQueueGC;
    private GraphicsContext countersStandsGC;
    private GraphicsContext washQueueGC;
    private GraphicsContext washStandGC;

    @Override
    public void setEnvironment(Environment environment){
        simEnvironment = environment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carsList = new ArrayList<Sprite>();

        fuelStandImg = new Image("stand-p.png");
        counterStandImg = new Image("stand-c.png");
        washStandImg = new Image("stand-cw.png");

        tankStandsGC = standsLayer.getGraphicsContext2D();
        tankQueueGC = standsQueueLayer.getGraphicsContext2D();
        countersQueueGC = countersQueueLayer.getGraphicsContext2D();
        countersStandsGC = countersLayer.getGraphicsContext2D();
        washQueueGC = washQueueLayer.getGraphicsContext2D();
        washStandGC = washLayer.getGraphicsContext2D();

    }

    @Override
    public void reprintEnvironment() {
        standsQueueStatusLabel.setText("HUJ");
        countersQueueStatusLabel.setText(simEnvironment.queueToCounters.size() + "");
        washQueueStatusLabel.setText(simEnvironment.queueToWash.size() + "");
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
