package pl.dawidziak.view;

import dissimlab.monitors.Diagram;
import dissimlab.monitors.Statistics;
import dissimlab.simcore.SimControlException;
import dissimlab.simcore.SimManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.dawidziak.model.*;
import pl.dawidziak.model.events.NewClientEvent;

import javax.security.auth.login.CredentialNotFoundException;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoadParametersController implements Initializable {

    @FXML
    public ComboBox<DistributionName> clientDistribComboBox;
    @FXML
    public ComboBox<DistributionName> PBtimeDistribComboBox;
    @FXML
    public ComboBox<DistributionName> ONtimeDistribComboBox;
    @FXML
    public ComboBox<DistributionName> LPGtimeDistribComboBox;
    @FXML
    public ComboBox<DistributionName> washDistribComboBox;
    @FXML
    public ComboBox<DistributionName> fuelTypeDistribComboBox;
    @FXML
    public GridPane gridLayout;
    @FXML
    public Button defaultBtn;
    @FXML
    private TextField clientAmountInput;
    @FXML
    private TextField postAmountInput;
    @FXML
    private TextField counterAmountInput;
    @FXML
    private TextField postQueueSizeInput;

    private ArrayList<HBox> paramsHBoxes;

    private ArrayList<Number[]> DistribParameters;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientDistribComboBox.getItems().setAll(DistributionName.values());
        PBtimeDistribComboBox.getItems().setAll((DistributionName.values()));
        ONtimeDistribComboBox.getItems().setAll((DistributionName.values()));
        LPGtimeDistribComboBox.getItems().setAll((DistributionName.values()));
        washDistribComboBox.getItems().setAll((DistributionName.values()));
        fuelTypeDistribComboBox.getItems().setAll((DistributionName.values()));

        paramsHBoxes = new ArrayList<HBox>();
        for(int i = 0; i < 6; i++){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            paramsHBoxes.add(hBox);
            gridLayout.add(hBox, 2, 4+i);
        }

    }

    public void startButtonOnAction(ActionEvent actionEvent) {

        DistribParameters = new ArrayList<>();

        for (HBox hbox : paramsHBoxes) {
            int paramsAmount = hbox.getChildren().size();
            Number[] params = new Number[paramsAmount];
            var array = hbox.getChildren();
            if (paramsAmount == 1) {
                TextField tf = (TextField) array.get(0);
                params[0] = Double.parseDouble(tf.getText());
            } else if (paramsAmount == 2) {
                TextField tf1 = (TextField) array.get(0);
                TextField tf2 = (TextField) array.get(1);
                params[0] = Double.parseDouble(tf1.getText());
                params[1] = Double.parseDouble(tf2.getText());
            } else {
                TextField tf1 = (TextField) array.get(0);
                TextField tf2 = (TextField) array.get(1);
                TextField tf3 = (TextField) array.get(2);
                params[0] = Double.parseDouble(tf1.getText());
                params[1] = Double.parseDouble(tf2.getText());
                params[2] = Double.parseDouble(tf3.getText());
            }
            DistribParameters.add(params);
        }

        int clientAmount = Integer.parseInt(clientAmountInput.getText());
        int postAmount = Integer.parseInt(postAmountInput.getText());
        int postQueueSize = Integer.parseInt(postQueueSizeInput.getText());
        int counterAmount = Integer.parseInt(counterAmountInput.getText());
        //Distributions
        Distribution clientDistrib = new Distribution(clientDistribComboBox.getValue(), DistribParameters.get(0));
        Distribution PBtankTimeDistrib = new Distribution(PBtimeDistribComboBox.getValue(), DistribParameters.get(1));
        Distribution ONtankTimeDistrib = new Distribution(ONtimeDistribComboBox.getValue(), DistribParameters.get(2));
        Distribution LPGtankTimeDistrib = new Distribution(LPGtimeDistribComboBox.getValue(), DistribParameters.get(3));
        Distribution carWashChoiceDistrib = new Distribution(washDistribComboBox.getValue(), DistribParameters.get(4));
        Distribution fuelChoiceDistrib = new Distribution(fuelTypeDistribComboBox.getValue(), DistribParameters.get(5));

        //MOCK
//        int clientAmount = 150;
//        int postAmount = 4;
//        int postQueueSize = 10;
//        int counterAmount = 3;
//
//        Distribution clientDistrib = new Distribution(DistributionName.exponential, 5);
//        Distribution PBtankTimeDistrib = new Distribution(DistributionName.uniform, 0, 100);
//        Distribution ONtankTimeDistrib = new Distribution(DistributionName.erlang, 100, 2);
//        Distribution LPGtankTimeDistrib = new Distribution(DistributionName.normal, 50, 50);
//        Distribution carWashChoiceDistrib = new Distribution(DistributionName.normal, 30, 40);
//        Distribution fuelChoiceDistrib = new Distribution(DistributionName.gamma, 5, 2);

        var simParams = new SimParameters(clientAmount, postAmount, postQueueSize, counterAmount, clientDistrib, fuelChoiceDistrib, PBtankTimeDistrib, LPGtankTimeDistrib, ONtankTimeDistrib, carWashChoiceDistrib);
        SimManager simManager = new SimManager();
        Monitors monitored = new Monitors(simManager);

        Parent simRoot;
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("simAnimation.fxml"));
            simRoot = loader.load();
            SimAnimationController controller = loader.getController();
            Stage simStage = new Stage();
            simStage.setScene(new Scene(simRoot, 1300, 838));
            simStage.setTitle("Symulacja");

            Environment environment = new Environment(simParams, simManager, monitored);
            controller.setEnvironment(environment);
            controller.drawParameters();

            startTask(environment);

            simStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) gridLayout.getScene().getWindow();
        stage.close();

    }

    public void startTask(Environment sim)
    {
        // Create a Runnable
        Runnable task = new Runnable()
        {
            public void run()
            {
                simulation(sim);
            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    private static void simulation(Environment environment) {
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


        try{
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
        }catch (ArrayIndexOutOfBoundsException e){

        }
        finally {
            environment.isFinished = true;
        }

    }

    public void loadDefaultParams(ActionEvent actionEvent){
        clientDistribComboBox.setValue(DistributionName.exponential);
        PBtimeDistribComboBox.setValue(DistributionName.uniform);
        ONtimeDistribComboBox.setValue(DistributionName.erlang);
        LPGtimeDistribComboBox.setValue(DistributionName.normal);
        washDistribComboBox.setValue(DistributionName.normal);
        fuelTypeDistribComboBox.setValue(DistributionName.gamma);
        clientAmountInput.setText("1000");
        postAmountInput.setText("2");
        counterAmountInput.setText("1");
        postQueueSizeInput.setText("10");

    }

    public void distributionOnChange(ActionEvent actionEvent) {
        ComboBox<DistributionName> comboBox = (ComboBox<DistributionName>)actionEvent.getSource();
        int row = GridPane.getRowIndex(comboBox);
        int paramsAmount = determineParamsAmount(comboBox.getValue());
        askForParams(paramsAmount, row);
    }

    private int determineParamsAmount(DistributionName distribution){
        switch(distribution){
            case exponential:
            case chisquare:
            case student:
            case poisson:
            case geometric:
                return 1;
            case uniform:
            case erlang:
            case gamma:
            case normal:
            case beta:
            case lognormal:
            case fdistribution:
            case weibull:
                return 2;
            default:
                return 0;
        }
    }

    private void askForParams(int amount, int row){
        var array = paramsHBoxes.get(row-4).getChildren();
        array.clear();

        for(int i=0; i<amount; i++){
            TextField textField = new TextField();
            textField.setPromptText("p" + (i+1));
            textField.setMaxWidth(50);
            textField.setAlignment(Pos.CENTER_LEFT);
            array.add(textField);
        }
    }

}
