package pl.dawidziak.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.dawidziak.model.Distribution;
import pl.dawidziak.model.DistributionName;
import pl.dawidziak.model.Environment;
import pl.dawidziak.model.SimParameters;

import javax.security.auth.login.CredentialNotFoundException;
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

        for(HBox hbox : paramsHBoxes){
            int paramsAmount = hbox.getChildren().size();
            Number[] params = new Number[paramsAmount];
            var array = hbox.getChildren();
            if(paramsAmount == 1){
                TextField tf = (TextField)array.get(0);
                params[0] = Double.parseDouble(tf.getText());
            }else if(paramsAmount == 2){
                TextField tf1 = (TextField)array.get(0);
                TextField tf2 = (TextField)array.get(1);
                params[0] = Double.parseDouble(tf1.getText());
                params[1] = Double.parseDouble(tf2.getText());
            }else{
                TextField tf1 = (TextField)array.get(0);
                TextField tf2 = (TextField)array.get(1);
                TextField tf3 = (TextField)array.get(2);
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

        //SimParameters
        var simParams = new SimParameters(clientAmount, postAmount, postQueueSize, counterAmount, clientDistrib, fuelChoiceDistrib, PBtankTimeDistrib, LPGtankTimeDistrib, ONtankTimeDistrib, carWashChoiceDistrib);
        //Environment
        new Environment(simParams);

        Stage stage = (Stage) gridLayout.getScene().getWindow();
        stage.close();
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
