package doodleproject;

import javafx.scene.paint.Color;

public class BrushTool implements Tool {
    @Override
    public void apply(PixelGrid grid, int x, int y, Color color){
        grid.setPixel(x, y, color);
    }
}