package Ballot;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

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




}
