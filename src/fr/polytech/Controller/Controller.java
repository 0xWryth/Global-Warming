package fr.polytech.Controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Slider yearSlider;

    @FXML
    Label yearIndicatorLbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yearIndicatorLbl.textProperty().bind(
            Bindings.format(
                    "%.0f",
                    yearSlider.valueProperty()
            )
        );

    }
}
