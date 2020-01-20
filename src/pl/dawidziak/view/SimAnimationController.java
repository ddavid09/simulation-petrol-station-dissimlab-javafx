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
import pl.dawidziak.model.Environment;
import pl.dawidziak.model.Monitors;
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

        tankStandsGC = standsLayer.getGraphicsContext2D();
        tankQueueGC = standsQueueLayer.getGraphicsContext2D();
        countersQueueGC = countersQueueLayer.getGraphicsContext2D();
        countersStandsGC = countersLayer.getGraphicsContext2D();
        washQueueGC = washQueueLayer.getGraphicsContext2D();
        washStandGC = washLayer.getGraphicsContext2D();

        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                (EventHandler) event -> {
                    updateEnvironment();
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateEnvironment() {

        standsQueueStatusLabel.setText(simEnvironment.queueToFuelStands.size() + "/" + simEnvironment.simParameters.clientAmount);
        countersQueueStatusLabel.setText(LocalTime.now().toString());
        //washQueueStatusLabel;
    }

    private static void simulation(Environment environment){
        try {
            new NewClientEvent(environment, 0);
        } catch (SimControlException e) {
            e.printStackTrace();
        }

        try {
            environment.simManager.startSimulation();
        } catch (SimControlException e) {
            e.printStackTrace();
        }

        System.out.println("Liczba obsluzonych klientow: " + environment.getServicedClientAmount());
        System.out.println("Liczba straconych klientow: " + environment.getLostClientAmount());
        System.out.println("Srednia liczba klientow w kolejce do stanowisk: " + Statistics.arithmeticMean(environment.monitors.sizeQueueFuel));
        System.out.println("Srednia liczba klientow w kolejce do myjni " + Statistics.arithmeticMean(environment.monitors.sizeQueueWash));
        System.out.println("Sredni czas tankowania samochodu: " + Statistics.arithmeticMean(environment.monitors.serviceTime));
        System.out.println("Sredni czas mycia samochodu: " + Statistics.arithmeticMean(environment.monitors.washTime));
        System.out.println("Prawdopodobienstwo rezygnacji z obslugi przez kierowce: " + ((double)environment.getLostClientAmount()/environment.simParameters.clientAmount));

        Diagram diagram = new Diagram(Diagram.DiagramType.TIME_FUNCTION, "Liczba samochodow w kolejkach");
        diagram.add(environment.monitors.sizeQueueFuel, Color.BLACK, "do stanowisk");
        diagram.add(environment.monitors.sizeQueueWash, Color.RED, "do myjni");
        diagram.show();

        Diagram diagramst = new Diagram(Diagram.DiagramType.DISTRIBUTION, "Czas tankowania/mycia samochodu");
        diagramst.add(environment.monitors.serviceTime, Color.BLACK, "tankowania");
        diagramst.add(environment.monitors.washTime, Color.RED, "mycia");
        diagramst.show();

        Diagram diagramla = new Diagram(Diagram.DiagramType.TIME_FUNCTION, "Liczba rezygnacji");
        diagramla.add(environment.monitors.lostClient, Color.BLUE);
        diagramla.show();

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
