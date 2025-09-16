package doodleproject;

import javafx.scene.paint.Color;


public class PixelGrid {

    //Variabler
    private int width, height;
    private Color[][] pixels;

    //Konstruktør: Oppretter et rutenett fylt med hvite piksler
    public PixelGrid(int width, int height){
        if (width <= 0 || height <= 0) {
            throw new IllegalStateException("Width and height must be above 0");
        }

        this.width = width;
        this.height = height;
        pixels = new Color[width][height];
        fill(Color.WHITE);  // Setter alle piksler til hvit som standard
    }

    //Sjekker om koordinater er gyldige
    public boolean isValidCoordinate(int x, int y){
        return x >= 0 && x<width && y >= 0 && y<height;
    }

    //Setter pixelen til en ny farge hvis koordinatene er gyldige
    public void setPixel(int x, int y, Color color){
        if (!isValidCoordinate(x,y)) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
        pixels[x][y] = color;
            
    }

    //Henter fargen til gitt pixel hvis koordinatene er gyldige
    public Color getPixel(int x, int y){
        if (!isValidCoordinate(x, y)) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
        return pixels[x][y];
    }

    //Fyller hele rutenettet med én farge
    public void fill(Color color){
        for(int i = 0 ; i < width ; i++){
            for(int n = 0 ; n < height ; n++){
                pixels[i][n] = color;
            }
        }
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

}
