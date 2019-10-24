package exercise1.visualization;

import javafx.scene.control.TextField;

import java.util.function.Function;
import java.util.function.Predicate;

public class VisualizationUtils {

    public static boolean isTextFieldTextInt(TextField textField) {
        return isTextFieldTextType(textField, Integer::parseInt);
    }

    private static boolean isTextFieldTextType(TextField textField, Function<String, ?> parseFromString) {
        try {
            parseFromString.apply(textField.getText());
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static TextField predicateTextField(Predicate<TextField> predicate) {
        TextField textField = new TextField();
        textField.setOnKeyReleased(e -> {
            if(!textField.getText().trim().isEmpty() && !predicate.test(textField)) {
                textField.setStyle("-fx-text-inner-color: red;");
            } else {
                textField.setStyle("");
            }
        });
        return textField;
    }
}
