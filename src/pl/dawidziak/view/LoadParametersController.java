package pl.dawidziak.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import pl.dawidziak.model.Distribution;
import pl.dawidziak.model.DistributionName;
import pl.dawidziak.model.Environment;
import pl.dawidziak.model.SimParameters;

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
        int clientAmount = Integer.parseInt(clientAmountInput.getText());
        int postAmount = Integer.parseInt(postAmountInput.getText());
        int postQueueSize = Integer.parseInt(postQueueSizeInput.getText());
        int counterAmount = Integer.parseInt(counterAmountInput.getText());



    }

    public void distributionOnChange(ActionEvent actionEvent) {
        ComboBox<DistributionName> comboBox = (ComboBox<DistributionName>)actionEvent.getSource();
        int row = gridLayout.getRowIndex(comboBox);
        int paramsAmount = determineParamsAmount(comboBox.getValue());
        askForParams(paramsAmount, row);
    }

    private int determineParamsAmount(DistributionName distribution){
        switch(distribution){
            case exponential:
            case laplace:
            case chisquare:
            case student:
            case poisson:
            case geometric:
            case triangular:
                return 1;
            case uniform:
            case erlang:
            case gamma:
            case normal:
            case beta:
            case lognormal:
            case fdistribution:
            case weibull:
            case binomial:
            case negativebinomial:
                return 2;
            default:
                return 3;

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
