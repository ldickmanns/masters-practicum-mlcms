package exercise1.model;

import exercise1.model.CellStateObjects.StateSpace;

import java.util.Properties;

import static exercise1.Utils.isInt;
import static java.lang.Integer.parseInt;

public class GridPropertiesParser {

    private static final String GRID = "grid";
    private static final String WIDTH = GRID + ".width";
    private static final String HEIGHT = GRID + ".height";
    private static final String X = "x";
    private static final String Y = "y";

    public static Properties toProperties(Grid grid) {
        Properties properties = new Properties();
        StateSpace[][] state = grid.getState();
        if (state == null || state[0] == null) return null;
        int width = state.length;
        int height = state[0].length;
        properties.put(WIDTH, width);
        properties.put(HEIGHT, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String positionKey = X + x + Y + y;
                String positionValue = state[x][y].toString();
                properties.put(positionKey, positionValue);
            }
        }

        return properties;
    }

    public static Grid fromProperties(Properties properties) {
        String widthValue = properties.getProperty(WIDTH);
        String heightValue = properties.getProperty(HEIGHT);
        if (widthValue == null || heightValue == null || !isInt(widthValue) || !isInt(widthValue)) return null;
        int width = parseInt(widthValue);
        int height = parseInt(heightValue);
        Grid grid = new Grid(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String positionKey = X + x + Y + y;
                String positionValue = properties.getProperty(positionKey);
                if (positionValue == null) return null;
                StateSpace stateSpace = StateSpace.valueOf(positionValue);
                switch (stateSpace) {
                    case P:
                        grid.addPedestrian(x, y);
                        break;
                    case O:
                        grid.addObstacle(x, y);
                        break;
                    case T:
                        grid.addTarget(x, y);
                        break;
                    default:
                        break;
                }
            }
        }

        return grid;
    }
}
