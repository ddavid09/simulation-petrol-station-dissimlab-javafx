package pl.dawidziak.view;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import pl.dawidziak.model.Environment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SimAnimationController implements Initializable, EnvironmentChangeListener {

    private ArrayList<Sprite> carsList;
    private Image fuelStandImg;
    private Image counterStandImg;
    private Image washStandImg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carsList = new ArrayList<Sprite>();
        fuelStandImg = new Image("stand-p.png");
        counterStandImg = new Image("stand-c.png");
        washStandImg = new Image("stand-cw.png");
    }

    @Override
    public void reprintEnvironment(Environment environment) {
        int clientsAmount = environment.simParameters.clientAmount;
    }
}
