package pl.dawidziak;

import dissimlab.monitors.Diagram;
import dissimlab.monitors.Statistics;
import dissimlab.simcore.SimControlException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.dawidziak.model.*;

import dissimlab.simcore.SimManager;
import pl.dawidziak.model.events.NewClientEvent;
import pl.dawidziak.view.EnvironmentChangeListener;
import pl.dawidziak.view.SimAnimationController;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/loadParameters.fxml"));
        primaryStage.setTitle("Symulacja stacji Paliw - Wprowadz parametry");
        primaryStage.setScene(new Scene(root, 540, 400));
        primaryStage.show();
        //primaryStage.close(); //step over window show
    }

    public static void main(String[] args) {
        launch(args);
    }


}
