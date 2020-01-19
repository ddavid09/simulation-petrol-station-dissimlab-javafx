package pl.dawidziak.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import pl.dawidziak.model.Environment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SimAnimationController implements Initializable, EnvironmentChangeListener {

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

        



    }

    @Override
    public void reprintEnvironment() {
        int clientsAmount = simEnvironment.simParameters.clientAmount;
    }
}
