package exercise1;

import javafx.scene.control.TextField;

import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {

    public static boolean isTextFieldTextInt(TextField textField) {
        return isType(textField.getText(), Integer::parseInt);
    }

    public static boolean isInt(String s) {
        return isType(s, Integer::parseInt);
    }

    private static boolean isType(String s, Function<String, ?> parseFromString) {
        try {
            parseFromString.apply(s);
            return true;
        } catch (Exception e) {
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
