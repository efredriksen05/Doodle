package doodleproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

public class DoodleTest {
    
    @Test
    //Tester konstruktøren
    public void testConstructor(){
        //Tester om unntaket utløses ved negative verdier
        assertThrows(IllegalStateException.class, () -> new PixelGrid(-2,10));
        assertThrows(IllegalStateException.class, () -> new PixelGrid(10, -1));
        assertThrows(IllegalStateException.class, () -> new PixelGrid(0, 0));

        //Tester om konstruktøren gir riktig høyde og bredde
        PixelGrid grid = new PixelGrid(10,15);
        assertEquals(10, grid.getWidth());
        assertEquals(15, grid.getHeight());
    }

    //Tester funksjonen som vurderer om et koordinat er gyldig
    @Test 
    public void testValidCoordinate(){
        PixelGrid grid = new PixelGrid(20, 10);
        //Riktig
        assertEquals(true, grid.isValidCoordinate(15, 2));

        //For høyre verdier
        assertEquals(false, grid.isValidCoordinate(21, 2));
        assertEquals(false, grid.isValidCoordinate(10, 15));

        //For lave verdier/negative verdier
        assertEquals(false, grid.isValidCoordinate(-2, 2));
        assertEquals(false, grid.isValidCoordinate(10, -2));
    }

    @Test 
    //Tester fargeleggingen av en pixel
    public void testSetPixel(){
        PixelGrid grid = new PixelGrid(15, 15);

        //Fargelegging med gyldige parametere
        grid.setPixel(10, 10, Color.PINK);
        assertEquals(Color.PINK, grid.getPixel(10, 10));

        //Fargelegging med ugyldige (negative) parametere
        assertThrows(IllegalArgumentException.class, ()-> grid.setPixel(-2, 5, Color.PINK));
        assertThrows(IllegalArgumentException.class, ()-> grid.setPixel(5, -2, Color.PINK));
    }

    @Test
    public void testGetPixel(){
        PixelGrid grid = new PixelGrid(15, 15);

        grid.setPixel(10, 10, Color.PINK);
        assertEquals(Color.PINK, grid.getPixel(10, 10));

        //Get pixel med ugyldige parametere (negative)
        assertThrows(IllegalArgumentException.class, ()-> grid.getPixel(-2, 5));
        assertThrows(IllegalArgumentException.class, ()-> grid.getPixel(5, -2));
    }

    @Test
    public void testFill() {
        // Opprett et 5x5 PixelGrid
        PixelGrid grid = new PixelGrid(5, 5);

        // Fyll hele gridet med hvite farge
        grid.fill(Color.WHITE);

        // Sjekk at ALLE pikslene er blitt hvite
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(Color.WHITE, grid.getPixel(i, j));
            }
        }
    }

    @Test
    public void testToHex() {
        // Test standardfarger
        assertEquals("#FF0000", DrawingSession.toHex(Color.RED));   // Rødn
        assertEquals("#0000FF", DrawingSession.toHex(Color.BLUE));  // Blå
        assertEquals("#FFFFFF", DrawingSession.toHex(Color.WHITE)); // Hvit
        assertEquals("#000000", DrawingSession.toHex(Color.BLACK)); // Svart

        // Test en blandingsfarge
        assertEquals("#7F7F7F", DrawingSession.toHex(Color.color(0.5, 0.5, 0.5))); // Grå (50%)

        // Test en annen tilfeldig farge
        assertEquals("#FFA500", DrawingSession.toHex(Color.ORANGE)); // Oransje
    }

    @Test
    public void testFromHex() {
        // Test standardfarger
        assertEquals(Color.RED, DrawingSession.fromHex("#FF0000"));   // Rød
        assertEquals(Color.BLUE, DrawingSession.fromHex("#0000FF"));  // Blå
        assertEquals(Color.WHITE, DrawingSession.fromHex("#FFFFFF")); // Hvit
        assertEquals(Color.BLACK, DrawingSession.fromHex("#000000")); // Svart

        // Test en tilfeldig farge (oransje)
        assertEquals(Color.ORANGE, DrawingSession.fromHex("#FFA500"));
    }

    @Test
    public void testSaveToFile() throws IOException {
        String filename = "test_drawing.json";

        // Opprett en PixelGrid og sett noen piksler
        PixelGrid grid = new PixelGrid(3, 3);
        grid.setPixel(0, 0, Color.RED);
        grid.setPixel(1, 1, Color.GREEN);
        grid.setPixel(2, 2, Color.BLUE);

        // Lagre rutenettet til fil
        DrawingSession.saveToFile(grid, filename);

        // Sjekk at filen eksisterer
        File file = new File(filename);
        assertTrue(file.exists(), "Filen ble ikke opprettet");

        // Last inn filen igjen og sjekk at innholdet er likt originalen
        PixelGrid loadedGrid = DrawingSession.loadFromFile(filename);
        assertEquals(grid.getWidth(), loadedGrid.getWidth());
        assertEquals(grid.getHeight(), loadedGrid.getHeight());
        assertEquals(grid.getPixel(0, 0), loadedGrid.getPixel(0, 0));
        assertEquals(grid.getPixel(1, 1), loadedGrid.getPixel(1, 1));
        assertEquals(grid.getPixel(2, 2), loadedGrid.getPixel(2, 2));

        // Rydd opp: slett testfilen
        file.delete();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        String filename = "test_load_drawing.json";

        // 1. Opprett og lagre en PixelGrid
        PixelGrid originalGrid = new PixelGrid(3, 3);
        originalGrid.setPixel(0, 0, Color.RED);
        originalGrid.setPixel(1, 1, Color.GREEN);
        originalGrid.setPixel(2, 2, Color.BLUE);
        DrawingSession.saveToFile(originalGrid, filename);

        // 2. Last inn rutenettet fra fil
        PixelGrid loadedGrid = DrawingSession.loadFromFile(filename);

        // 3. Sjekk at dimensjonene stemmer
        assertEquals(originalGrid.getWidth(), loadedGrid.getWidth());
        assertEquals(originalGrid.getHeight(), loadedGrid.getHeight());

        // 4. Sjekk at fargene stemmer
        assertEquals(originalGrid.getPixel(0, 0), loadedGrid.getPixel(0, 0));
        assertEquals(originalGrid.getPixel(1, 1), loadedGrid.getPixel(1, 1));
        assertEquals(originalGrid.getPixel(2, 2), loadedGrid.getPixel(2, 2));

        // 5. Rydd opp: slett testfilen
        File file = new File(filename);
        assertTrue(file.delete(), "Kunne ikke slette testfilen");
    }


}
