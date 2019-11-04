package exercise1.model;

import exercise1.model.CellStateObjects.StateSpace;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import static exercise1.Utils.isInt;
import static java.lang.Integer.parseInt;

public class PropertiesUtils {

    private static final String GRID = "grid";
    private static final String WIDTH = GRID + ".width";
    private static final String HEIGHT = GRID + ".height";
    private static final String X = "x";
    private static final String Y = "y";

    public static Properties toProperties(StateSpace[][] state) {
        Properties properties = new Properties();
        if (state == null || state[0] == null) return null;
        Integer width = state.length;
        Integer height = state[0].length;
        properties.put(WIDTH, width.toString());
        properties.put(HEIGHT, height.toString());
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

    public static Properties loadProperties() {
        FileChooser fileChooser = createFileChooser("Choose properties");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                FileReader fileReader = new FileReader(file);
                Properties properties = new Properties();
                properties.load(fileReader);
                return properties;
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Error while loading!");
                alert.show();
                return null;
            }
        }
        return null;
    }

    public static void saveProperties(Properties properties) {
        FileChooser fileChooser = createFileChooser("Save properties");

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                properties.store(fileWriter, null);
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Error while saving!");
                alert.show();
            }
        }
    }

    private static FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/config"));
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter prop = new FileChooser.ExtensionFilter("Properties", "*.properties", "*.prop");
        FileChooser.ExtensionFilter all = new FileChooser.ExtensionFilter("All files", "*");
        fileChooser.getExtensionFilters().addAll(prop, all);
        return fileChooser;
    }
}
