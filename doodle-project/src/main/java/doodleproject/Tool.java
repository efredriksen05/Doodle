package doodleproject;

import javafx.scene.paint.Color;

public interface Tool {
    void apply(PixelGrid grid, int x, int y, Color color);
}
