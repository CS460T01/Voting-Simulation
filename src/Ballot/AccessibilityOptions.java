package Ballot;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class AccessibilityOptions {
    private String currentFontStyle;
    private boolean highContrast;
    private String highContrastBackground;

    public AccessibilityOptions(String currentFontStyle, boolean highContrast, String highContrastBackground) {
        this.currentFontStyle = currentFontStyle;
        this.highContrast = highContrast;
        this.highContrastBackground = highContrastBackground;
    }

    // Method to apply high contrast style
    public void applyHighContrastStyle(Parent parent) {
        this.highContrast = true;

        // Check if the parent is one of the container types
        if (parent instanceof Pane || parent instanceof VBox || parent instanceof HBox || parent instanceof BorderPane || parent instanceof StackPane) {
            // Apply high contrast style selectively
            String existingStyle = parent.getStyle();
            if (!(parent instanceof TextField) && !(parent instanceof CheckBox)) {
                // Preserve existing styles (like borders) and append only the background color
                parent.setStyle(existingStyle + highContrastBackground);
            }
        }

        // Recursively apply the style, skipping text fields and checkboxes
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof Parent && !(child instanceof TextField) && !(child instanceof CheckBox)) {
                applyHighContrastStyle((Parent) child);
            }
        }
    }

    public void removeHighContrastStyle(Parent parent) {
        this.highContrast = false;

        // Reset only the high contrast specific styles
        if (parent instanceof Pane || parent instanceof VBox || parent instanceof HBox || parent instanceof BorderPane || parent instanceof StackPane) {
            String existingStyle = parent.getStyle();
            // Remove only the high contrast background color, preserving other styles
            existingStyle = existingStyle.replace(highContrastBackground, "");
            parent.setStyle(existingStyle);
        }

        // Recursively reset the style for child nodes that are containers
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof Parent) {
                removeHighContrastStyle((Parent) child);
            }
        }
    }

    public void updateFontSizeRecursive(Pane parent, String fontSizeStyle) {
        String highContrastColor = "-fx-text-fill: yellow;"; // Replace 'yellow' with your desired color
        String combinedStyle;

        if(this.highContrast == true) {
            combinedStyle = fontSizeStyle + highContrastColor; // Combine font size and text color
        }
        else{combinedStyle = fontSizeStyle + "";}

        for (Node child : parent.getChildren()) {
            if (child instanceof Text) {
                ((Text) child).setStyle(combinedStyle);
            } else if (child instanceof Label) {
                ((Label) child).setStyle(combinedStyle);
            } else if (child instanceof Button) {
                // For Buttons, you might want to only change the text size and not the text color
                // If you want to change both, use combinedStyle
                ((Button) child).setStyle(combinedStyle);
            } else if (child instanceof CheckBox) {
                ((CheckBox) child).setStyle(combinedStyle);
            } else if (child instanceof TextField) {
                // For TextFields, you might want to only change the text size and not the text color
                // If you want to change both, use combinedStyle
                ((TextField) child).setStyle(combinedStyle);
            } else if (child instanceof Pane) {
                updateFontSizeRecursive((Pane) child, fontSizeStyle);
            }
        }
    }





}
