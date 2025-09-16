package doodleproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class FrontPageController {
    @FXML private TextField widthField;
    @FXML private TextField heightField;

    @FXML
    private void loadDrawingField() {
        try {
            // Hent verdiene fra tekstfeltene og sørg for at de ikke overskrider maksverdiene
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            // Sjekk at bredden ikke overskrider maksverdien (27) og høyden ikke overskrider maksverdien (15)
            if (width > 27) {
                showAlert("Input Error", "Width cannot be greater than 27.");
                return;
            }

            if (height > 15) {
                showAlert("Input Error", "Height cannot be greater than 15.");
                return;
            }

            // Sette grid size og bytte til tegnesiden
            AppController.setGridSize(width, height); // Setter verdiene i AppController
            App.setRoot("App"); // Bytter til tegnesiden

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for width and height.");
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    // Hjelpemetode for å vise en alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
