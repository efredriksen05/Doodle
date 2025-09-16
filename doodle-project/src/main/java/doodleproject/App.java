package doodleproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    //Variabel for scenen som skal vises
    private static Scene scene;


    //Setter opp scenen med tittel, scenevalg og icon
    @Override
    public void start(Stage stage) throws IOException {
        try{
            scene = new Scene(loadFXML("frontPage")); //lager en ny scene og bruker metoden nedenfor for å velge scenen ved å skrive inn filnavn
            stage.setScene(scene); 
            stage.setTitle("Doodles");
            
            Image icon = new Image(getClass().getResource("/doodleproject/heart.png").toExternalForm());
            stage.getIcons().add(icon);
    
            stage.show();
            
        } catch (IOException e) {
            System.out.println("Det oppstod en feil ved lasting av FXML: " + e.getMessage());
        } 
    }

    //Metode som bytter scene 
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    //Metode som gjør det enklere å åpne fxml scener ved at man bare må skrive inn navnet på filen
    private static Parent loadFXML(String fxml) throws IOException {
        //henter filen som har samme navn som fxml parameteren, forutsatt av at den ligger i samme katalog (begge må ligge i src og filen må være i resources)
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        Application.launch();
    }
                
}
