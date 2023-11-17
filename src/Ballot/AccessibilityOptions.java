package Ballot;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
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
    private final String LARGE_FONT_STYLE = "-fx-font-size: 40px;";
    private final String NORMAL_FONT_STYLE = "-fx-font-size: 20px;";
    String buttonHighContrastBackground = "-fx-background-color: white; ";
    String buttonHighContrastText = "-fx-text-fill: black; ";
    String highContrastBackground = "-fx-background-color: black; ";

    public AccessibilityOptions(String currentFontStyle, boolean highContrast) {
        this.currentFontStyle = currentFontStyle;
        this.highContrast = highContrast;

    }

    public boolean isHighContrastEnabled() {
        return highContrast;
    }

    public void setHighContrast(boolean highContrast) {
        this.highContrast = highContrast;
    }

    public String getCurrentFontStyle() {
        return this.currentFontStyle;
    }

    public void setFontStyle(String fontStyle) {
        if( fontStyle.equals("LARGE_FONT_STYLE"))
        {
            currentFontStyle = LARGE_FONT_STYLE;
        }
        else
        {
            currentFontStyle = NORMAL_FONT_STYLE;
        }
    }

    public void updateHighContrastStyle(Parent parent) {
        if (highContrast) {
            applyHighContrastStyle(parent);
        } else {
            removeHighContrastStyle(parent);
        }
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
        String highContrastColor = "-fx-text-fill: white;"; // Replace 'yellow' with your desired color
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

    public String getButtonStyle(String currentFontStyle) {
        if(this.highContrast == true) {
            return buttonHighContrastBackground + buttonHighContrastText + currentFontStyle;
        }

        else{return currentFontStyle;}
    }
    public void speakText(String text) {
        VoiceManager vm = VoiceManager.getInstance();
        Voice voice = vm.getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
            try {
                voice.speak(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
            voice.deallocate();
        } else {
            System.out.println("No voice named kevin16 found.");
        }
    }


}
