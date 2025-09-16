package doodleproject;

import javafx.scene.paint.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;


public class DrawingSession {

    //Konverterer farger til HEX
    public static String toHex(Color color){
        return String.format("#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
    }

    //Lagrer tegning til fil
    public static void saveToFile(PixelGrid grid, String filename) throws IOException{

        //Oppretter et JSON objekt som man kan lagre informasjonen i
        JSONObject json = new JSONObject();

        //Legger til bredde og høyde
        json.put("width", grid.getWidth());
        json.put("height", grid.getHeight());

        //Konverterer 2D matrise til JSON
        JSONArray pixelsArray = new JSONArray();
        for(int i = 0 ; i < grid.getWidth(); i++){

            //Opppretter en ny rad i JSON objektet
            JSONArray row = new JSONArray();

            //Legger til hver rute i kollonen 
            for(int j = 0 ; j < grid.getHeight(); j++){
                row.put(toHex(grid.getPixel(i, j)));
            }

            //Legger raden med tekst/kollonen med piksler til i pixelArrayen 
            pixelsArray.put(row);
        }

        //Legger pixelArrayen i json objektet med nøkkelen "pixels"
        json.put("pixels",pixelsArray);

        // Skriver til fil
        try (FileWriter file = new FileWriter(filename)) {
            file.write(json.toString(4));  // `4` gir ryddig formatering
        }
    }
    // Konverterer en HEX-farge (#RRGGBB) til JavaFX `Color`
    public static Color fromHex(String hex) {
        return Color.web(hex);
    }

    //Laster inn tegning fra fil
    public static PixelGrid loadFromFile(String filename) throws IOException{

        //Leser hele filen som tekst og lagrer det som en streng
        String content = new String(Files.readAllBytes(Paths.get(filename)));

        //Gjør teksten om til et JSON objekt sånn at vi kan hente ut informasjon
        JSONObject json = new JSONObject(content);

        //Oppretter rutenettet 
        int width = json.getInt("width");
        int height = json.getInt("height");
        PixelGrid grid = new PixelGrid(width, height);

        //Henter fargene
        JSONArray pixelsArray = json.getJSONArray("pixels");
        for(int i = 0; i < width ; i++){

            //Henter raden med tekst i JSON objektet
            JSONArray row = pixelsArray.getJSONArray(i);

            //Fargelegger hver piksel i raden fra objektet/kollonnen 
            for(int j = 0; j < height ; j++){
                grid.setPixel(i, j, fromHex(row.getString(j)));
            }
        }

        return grid;
    } 
}
