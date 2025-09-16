package doodleproject;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;

public class AppController {
    @FXML private GridPane gridPane;
    @FXML private ColorPicker colorPicker;
    @FXML private Button brushButton;
    @FXML private Button eraseButton;
    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button clearButton;

    private static int gridWidth;
    private static int gridHeight;
    private PixelGrid pixelGrid;
    private Tool currentTool = new BrushTool();
    private boolean isDrawing = false; // Sporer om brukeren tegner

    public static void setGridSize(int width, int height) {
        gridWidth = width;
        gridHeight = height;
    }

    //Blir kalt automatisk når fxml filen lastes inn
    @FXML
    public void initialize() {

        //Oppretter rutenettet
        pixelGrid = new PixelGrid(gridWidth, gridHeight);
        createGrid();

        // Knappene for å bytte verktøy
        brushButton.setOnAction(event -> currentTool = new BrushTool());
        eraseButton.setOnAction(event -> currentTool = new EraseTool());

        // Save og Load-knapper
        saveButton.setOnAction(event -> handleSave());
        loadButton.setOnAction(event -> handleLoad());

        // Clear-knapp
        clearButton.setOnAction(event -> handleClear());

        // Legger mouse drag på hele gridPane
        gridPane.setOnMousePressed(event -> isDrawing = true);
        gridPane.setOnMouseReleased(event -> isDrawing = false);
        gridPane.setOnMouseDragged(event -> handleDrag(event.getX(), event.getY()));
    }

    private void createGrid() {

        //Fjerner standard rutene i gridPane
        gridPane.getChildren().clear();

        //Gjør rutene synlige
        gridPane.setGridLinesVisible(true);

        //Går gjennom høyden og bredden og legger til et rektangel for hver pizel i pixelGrid
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                Rectangle rect = new Rectangle(20, 20, pixelGrid.getPixel(col, row));
                rect.setStroke(Color.BLACK);

                //Setter variablene til final for å kunne bruke dem i lamda uttrykket nedenfor
                final int finalRow = row;
                final int finalCol = col;

                //fargelegger rektangelet når musen presses ned
                rect.setOnMousePressed(event -> colorPixel(rect, finalCol, finalRow));

                //legger til det fargede rektangelet/pikselen i gridPane
                gridPane.add(rect, finalCol, finalRow);
            }
        }
    }

    private void handleDrag(double mouseX, double mouseY) {
        if (!isDrawing) return;

        int col = (int) (mouseX / 20); // Finn kolonnen basert på musens X-posisjon
        int row = (int) (mouseY / 20); // Finn raden basert på musens Y-posisjon

        //sjekker om musepekeren fremdeles er innenfor tegneområdet
        if (col >= 0 && col < gridWidth && row >= 0 && row < gridHeight) {

            //henter ut rektangelet musepekeren befinner seg i og fargelegger det
            Rectangle rect = (Rectangle) getNodeFromGridPane(gridPane, col, row);
            if (rect != null) {
                colorPixel(rect, col, row);
            }
        }
    }

    //fargelegger pixel ved å sjekke valgt farge og fylle rektangelet med gitt farge
    private void colorPixel(Rectangle rect, int x, int y) {
        Color selectedColor = colorPicker.getValue();
        currentTool.apply(pixelGrid, x, y, selectedColor);
        rect.setFill(pixelGrid.getPixel(x, y));
    }

    private Rectangle getNodeFromGridPane(GridPane gridPane, int col, int row) {
        //går gjennom alle rutene i gridpane
        for (javafx.scene.Node node : gridPane.getChildren()) {
            //sjekker om den ligger i ønsket rad og col, hvis ja så returneres ruten
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (Rectangle) node;
            }
        }
        //hvis den ikke finner noen returnerer den null
        return null;
    }

    //lager og viser en alert, kalles når feil skal varsles
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSave() {
        FileChooser fileChooser = new FileChooser(); //lager et filvelger vindu
        fileChooser.setTitle("Save Drawing"); //setter tittelen på vinduet til "Save Drawing"

        //setter filter slik at det kun vises JSON filer
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON filer (*.json)", "*.json"));

        //åpner fillagringsvinduet som et frittstående vindu (pga null)
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                DrawingSession.saveToFile(pixelGrid, file.getAbsolutePath()); //henter pathen til filen/velger fil
            } catch (IOException e) {
                showAlert("Error occured while saving",e.getMessage());
            }
        }
    }

    @FXML
    private void handleLoad() {
        FileChooser fileChooser = new FileChooser(); //lager et filvelger vindu
        fileChooser.setTitle("Open drawing"); //setter tittelen på vinduet til "Open Drawing"

        //setter filter slik at det kun vises JSON filer
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON filer (*.json)", "*.json"));

        //åpner fillagringsvinduet som et frittstående vindu (pga null)
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                PixelGrid loadedGrid = DrawingSession.loadFromFile(file.getAbsolutePath());

                gridWidth = loadedGrid.getWidth();
                gridHeight = loadedGrid.getHeight();
                pixelGrid = loadedGrid;

                createGrid();

            } catch (IOException e) {
                showAlert("Load error", "There was an error loading the file: " + e.getMessage());
            }
        }
    }

    //Går gjennom hver piksel og fargelegger den hvit
    @FXML
    private void handleClear() {
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                pixelGrid.setPixel(col, row, Color.WHITE);
            }
        }
        updateGrid();
    }

    //Oppdaterer rutene i gridPane til å være lik pikslene i pixelGrid
    private void updateGrid() {
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                Rectangle rect = (Rectangle) getNodeFromGridPane(gridPane, col, row);
                if (rect != null) {
                    rect.setFill(pixelGrid.getPixel(col, row));
                }
            }
        }
    }

}
