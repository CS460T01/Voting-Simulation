package Ballot;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class VoteFormGUI extends Application {
    private AccessibilityOptions accessibilityOptions;
    private VotingController controller;
    VBox contentBox;
    int currentPage;
    Button submitButton;
    Button prevButton;
    Button nextButton;
    VBox submitPromptBox;
    VBox accessibilityPromptBox;
    HBox buttonBox;
    BorderPane root;
    private final String LARGE_FONT_STYLE = "-fx-font-size: 40px;";
    private final String NORMAL_FONT_STYLE = "-fx-font-size: 20px;";
    private String currentFontStyle = NORMAL_FONT_STYLE;
    Map<String, VBox> optionElements = new HashMap<>();
    boolean textToSpeech = false;

    @Override
    public void start(Stage primaryStage) {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        controller = new VotingController();
        controller.initializeOffices();
        accessibilityOptions = new AccessibilityOptions(NORMAL_FONT_STYLE, false);
        root = new BorderPane();
        contentBox = new VBox(5);
        contentBox.setPadding(new Insets(10));
        contentBox.setFillWidth(true);
        currentPage = 1;

        buttonBox = new HBox();

        prevButton = new Button("Previous");
        prevButton.setStyle("-fx-font-size: 20px;");
        nextButton = new Button("Next");
        nextButton.setStyle("-fx-font-size: 20px;");
        HBox.setHgrow(prevButton, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(nextButton, javafx.scene.layout.Priority.ALWAYS);
        prevButton.setMaxWidth(Double.MAX_VALUE);
        nextButton.setMaxWidth(Double.MAX_VALUE);

        submitButton = new Button("Submit");
        submitButton.setStyle("-fx-font-size: 20px;");
        submitButton.setMaxWidth(Double.MAX_VALUE);
        submitButton.setVisible(false);
        submitButton.setOnAction(e -> controller.handleSubmit());

        prevButton.setVisible(false);
        prevButton.setDisable(true);
        nextButton.setVisible(false);
        nextButton.setDisable(true);

        buttonBox.getChildren().addAll(prevButton, nextButton, submitButton);

        prevButton.setOnAction(e -> {
            System.out.println("Before Prev Action: " + currentPage);

            if (currentPage == controller.getOffices().size() + 1) {
                currentPage = controller.getOffices().size();
                createVotingPage(currentPage - 1);
                buttonBox.getChildren().clear();
                buttonBox.getChildren().addAll(prevButton,nextButton);
            } else if (currentPage > 1) {
                extractSaveData(controller.getOffices().get(currentPage - 1).getKey());
                currentPage -= 2;
                createVotingPage(currentPage);
            }

            System.out.println("After Prev Action: " + currentPage);
            updateButtonVisibility();
        });

        nextButton.setOnAction(e -> {
            System.out.println("Before Next Action: " + currentPage);

            if (currentPage <= controller.getOffices().size()) {
                extractSaveData(controller.getOffices().get(currentPage - 1).getKey());
                if (currentPage < controller.getOffices().size()) {
                    createVotingPage(currentPage);
                }

                else {
                    createSubmitPrompt();
                    currentPage++;
                    buttonBox.getChildren().clear();
                    buttonBox.getChildren().addAll(prevButton,submitButton);
                    submitButton.setStyle(accessibilityOptions.getButtonStyle(currentFontStyle));
                    updateButtonVisibility();
                }
            }

            System.out.println("After Next Action: " + currentPage);
            updateButtonVisibility();
        });

        createAccessibilityOptionsPage(primaryStage, root);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 600, 700);

        primaryStage.setTitle("Ballot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createAccessibilityOptionsPage(Stage primaryStage, Pane rootPane) {
        contentBox.getChildren().clear();
        buttonBox.getChildren().clear();

        Button confirmButton = new Button("Confirm");

        accessibilityPromptBox = new VBox(20);
        accessibilityPromptBox.setAlignment(Pos.CENTER);
        accessibilityPromptBox.setFillWidth(true);
        accessibilityPromptBox.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        accessibilityPromptBox.setMaxWidth(Double.MAX_VALUE);
        CheckBox largerFontCheckbox = new CheckBox("Use Larger Font");
        CheckBox highContrastCheckbox = new CheckBox("Use High Contrast");
        CheckBox textToSpeechCheckbox = new CheckBox("Enable Text to Speech");

        Label accessibilityLabel = new Label("Select Accessibility Options:");
        accessibilityLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        largerFontCheckbox.setStyle(currentFontStyle);
        largerFontCheckbox.setSelected(currentFontStyle.equals(LARGE_FONT_STYLE));
        largerFontCheckbox.setOnAction(e -> {

            if (largerFontCheckbox.isSelected()) {
                largerFontCheckbox.setStyle(LARGE_FONT_STYLE);
                highContrastCheckbox.setStyle(LARGE_FONT_STYLE);
                textToSpeechCheckbox.setStyle(LARGE_FONT_STYLE);
                confirmButton.setStyle(accessibilityOptions.getButtonStyle(LARGE_FONT_STYLE));
                applyCurrentFontStyleToUI(LARGE_FONT_STYLE, "");
            }
            else {
                largerFontCheckbox.setStyle(NORMAL_FONT_STYLE);
                highContrastCheckbox.setStyle(NORMAL_FONT_STYLE);
                textToSpeechCheckbox.setStyle(NORMAL_FONT_STYLE);
                confirmButton.setStyle(accessibilityOptions.getButtonStyle(NORMAL_FONT_STYLE));
                applyCurrentFontStyleToUI(NORMAL_FONT_STYLE, "");
            }


        });

        highContrastCheckbox.setStyle(currentFontStyle);
        highContrastCheckbox.setOnAction(e -> {
            if (highContrastCheckbox.isSelected()) {
                accessibilityOptions.setHighContrast(true);
                confirmButton.setStyle(accessibilityOptions.getButtonStyle(currentFontStyle));
                updateButtonVisibility();
                accessibilityOptions.applyHighContrastStyle(root);
                accessibilityLabel.setTextFill(Color.WHITE);
                largerFontCheckbox.setTextFill(Color.WHITE);
                highContrastCheckbox.setTextFill(Color.WHITE);
                textToSpeechCheckbox.setTextFill(Color.WHITE);

            } else {
                accessibilityOptions.setHighContrast(false);
                confirmButton.setStyle(accessibilityOptions.getButtonStyle(currentFontStyle));
                updateButtonVisibility();
                contentBox.setStyle("");
                accessibilityOptions.removeHighContrastStyle(root);
                accessibilityLabel.setTextFill(Color.BLACK);
                largerFontCheckbox.setTextFill(Color.BLACK);
                highContrastCheckbox.setTextFill(Color.BLACK);
                textToSpeechCheckbox.setTextFill(Color.BLACK);

            }
        });

        textToSpeechCheckbox.setStyle(currentFontStyle);

        textToSpeechCheckbox.setOnAction(e -> {
            if (textToSpeechCheckbox.isSelected()) {
                textToSpeech = true;
                accessibilityOptions.speakText("Text to speech enabled");
            }

            else{textToSpeech = false;}
        });

        confirmButton.setStyle(currentFontStyle);
        confirmButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(confirmButton, Priority.ALWAYS);

        confirmButton.setOnAction(e -> {
            if (largerFontCheckbox.isSelected()) {
                // Apply larger font settings across the UI
            }

            if (textToSpeechCheckbox.isSelected()) {
                // Activate text to speech functionality
            }

            createVotingPage(0);
            updateButtonVisibility();
            confirmButton.setVisible(false);
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(prevButton, nextButton);
            updateButtonVisibility();
        });

        accessibilityPromptBox.getChildren().addAll(accessibilityLabel);
        contentBox.getChildren().addAll(accessibilityPromptBox, largerFontCheckbox, highContrastCheckbox, textToSpeechCheckbox, confirmButton);
        buttonBox.getChildren().add(confirmButton);

    }

    private void handleCheckBoxAction(CheckBox selectedCheckBox, VBox optionBox) {
        if (selectedCheckBox.isSelected()) {
            optionBox.getChildren().filtered(node -> node instanceof CheckBox && node != selectedCheckBox)
                    .forEach(node -> ((CheckBox) node).setSelected(false));
        }
    }

    private VBox createOptionsBox(String position, List<String> candidates) {
        VBox optionBox = new VBox(10);
        TextField writeInField = new TextField();
        writeInField.setPromptText("Enter candidate name...");
        writeInField.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
        writeInField.setVisible(false);


        for (String candidate : candidates) {
            String[] candidateInfo = candidate.split("\\(");
            if (candidateInfo.length > 1) {
                String candidateName = candidateInfo[0].trim();

                CheckBox checkBox = new CheckBox(candidateName);
                checkBox.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
                checkBox.setMaxWidth(Double.MAX_VALUE);

                String party = candidateInfo[1].replaceAll("[()]", "").trim();

                Label partyLabel = new Label(party);
                partyLabel.setStyle("-fx-font-size: 16px;");

                optionBox.getChildren().addAll(checkBox, partyLabel);

                checkBox.setOnAction(e -> {
                    handleCheckBoxAction(checkBox, optionBox);
                    writeInField.setVisible(false);
                });
            } else {
                CheckBox checkBox = new CheckBox(candidate);
                checkBox.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
                checkBox.setMaxWidth(Double.MAX_VALUE);

                optionBox.getChildren().add(checkBox);

                checkBox.setOnAction(e -> {
                    handleCheckBoxAction(checkBox, optionBox);
                    writeInField.setVisible(false);
                });
            }
        }

        CheckBox writeInOption = new CheckBox("Write-in");
        writeInOption.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
        writeInOption.setMaxWidth(Double.MAX_VALUE);
        writeInOption.setOnAction(e -> handleCheckBoxAction(writeInOption, optionBox));

        writeInOption.setOnAction(e -> {
            writeInField.setVisible(writeInOption.isSelected());
            if (writeInOption.isSelected()) {
                optionBox.getChildren().filtered(node -> node instanceof CheckBox && node != writeInOption)
                        .forEach(node -> ((CheckBox) node).setSelected(false));
            }
        });

        optionBox.getChildren().addAll(writeInOption, writeInField);

        optionElements.put(position, optionBox);

        return optionBox;
    }

    private VBox createHeader(String position, String instructions) {
        VBox headerDescriptionBox = new VBox(5);
        headerDescriptionBox.setAlignment(Pos.CENTER);
        headerDescriptionBox.setFillWidth(true);

        String defaultTitleStyle = "-fx-font-size: 30px; -fx-font-weight: bold; -fx-border-color: black; -fx-padding: 10px;";
        String defaultLabelStyle = "-fx-font-size: 20px; -fx-font-weight: bold;";
        String highlightLabelStyle = "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff;";
        String defaultDescriptionStyle = "-fx-border-color: black; -fx-padding: 5px;";

        String titleStyle = currentFontStyle.contains("40px") ? currentFontStyle : defaultTitleStyle;
        String labelStyle;

        if(accessibilityOptions.isHighContrastEnabled()){
            if(Objects.equals(currentFontStyle, LARGE_FONT_STYLE))
            {
                labelStyle = "-fx-font-size:40px; -fx-font-weight: bold; -fx-text-fill: #ffffff;";
            } else{
                labelStyle = highlightLabelStyle;
            }
        } else {
            labelStyle = currentFontStyle.contains("40px") ? currentFontStyle : defaultLabelStyle;
        }

        Label headerTitle = new Label("BERNALILLO COUNTY");
        if(accessibilityOptions.isHighContrastEnabled() == true){
            headerTitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-border-color: black; -fx-padding: 10px; -fx-text-fill: #ffffff;");
        }
        else {        headerTitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-border-color: black; -fx-padding: 10px;");}
        headerTitle.setAlignment(Pos.CENTER);
        headerTitle.setMaxWidth(Double.MAX_VALUE);

        VBox descriptionBox = new VBox(0);
        descriptionBox.setAlignment(Pos.CENTER);
        descriptionBox.setFillWidth(true);
        descriptionBox.setStyle(defaultDescriptionStyle);
        descriptionBox.setMaxWidth(Double.MAX_VALUE);

        Label line1 = new Label("OFFICIAL BALLOT");
        Label line2 = new Label("OFFICIAL GENERAL ELECTION BALLOT");
        Label line3 = new Label("OF THE STATE OF NEW MEXICO");
        Label line4 = new Label("NOVEMBER 6, 2023");

        if(accessibilityOptions.isHighContrastEnabled() == true){
            line1.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-padding: 10px;  -fx-text-fill: #ffffff;");
            line2.setStyle(highlightLabelStyle);
            line3.setStyle(highlightLabelStyle);
            line4.setStyle(highlightLabelStyle);
        }
        else {
            line1.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-padding: 10px;");
            line2.setStyle(defaultLabelStyle);
            line3.setStyle(defaultLabelStyle);
            line4.setStyle(defaultLabelStyle);
        }

        descriptionBox.getChildren().addAll(line1, line2, line3, line4);

        headerDescriptionBox.setSpacing(10);
        headerDescriptionBox.getChildren().addAll(headerTitle, descriptionBox);

        VBox candidateLabelBox = new VBox();
        Label candidateLabel = new Label(position);
        candidateLabel.setStyle(labelStyle);
        Label labelDirections = new Label(instructions);
        labelDirections.setStyle(labelStyle);

        candidateLabelBox.setAlignment(Pos.CENTER);
        candidateLabelBox.setMaxWidth(Double.MAX_VALUE);
        candidateLabelBox.setStyle("-fx-border-color: black;");
        candidateLabelBox.getChildren().addAll(candidateLabel, labelDirections);

        headerDescriptionBox.getChildren().add(candidateLabelBox);

        return headerDescriptionBox;
    }
    public void extractSaveData(String position) {
        VBox options = optionElements.get(position);
        if (options != null) {
            CheckBox writeInCheckBox = null;
            TextField writeInField = null;

            for (Node node : options.getChildren()) {
                if (node instanceof CheckBox && "Write-in".equals(((CheckBox) node).getText())) {
                    writeInCheckBox = (CheckBox) node;
                } else if (node instanceof TextField) {
                    writeInField = (TextField) node;
                }
            }

            if (writeInCheckBox != null && writeInCheckBox.isSelected() && writeInField != null && !writeInField.getText().isEmpty()) {
                controller.saveSelection(position, "Write-in: " + writeInField.getText());
            } else {
                for (Node node : options.getChildren()) {
                    if (node instanceof CheckBox && ((CheckBox) node).isSelected()) {
                        controller.saveSelection(position, ((CheckBox) node).getText());
                        return;
                    }
                }
                controller.saveSelection(position, "null");
            }
        }
    }
    private void restoreSelections(String position) {
        VBox options = optionElements.get(position);
        if (options != null && controller.selectionContainsKey(position)) {
            String selectedOption = controller.getCandidatesForPosition(position);

            CheckBox writeInCheckBox = null;
            TextField writeInField = null;
            for (Node node : options.getChildren()) {
                if (node instanceof CheckBox && "Write-in".equals(((CheckBox) node).getText())) {
                    writeInCheckBox = (CheckBox) node;
                } else if (node instanceof TextField) {
                    writeInField = (TextField) node;
                }
            }

            if (selectedOption.startsWith("Write-in: ") && writeInCheckBox != null && writeInField != null) {
                writeInCheckBox.setSelected(true);
                writeInField.setText(selectedOption.replace("Write-in: ", ""));
                writeInField.setVisible(true);
            } else {
                for (Node node : options.getChildren()) {
                    if (node instanceof CheckBox) {
                        ((CheckBox) node).setSelected(selectedOption.equals(((CheckBox) node).getText()));
                    }
                }
            }
        }
    }

    private void createSubmitPrompt() {
        if (submitPromptBox == null) {
            submitPromptBox = new VBox(20);
            submitPromptBox.setAlignment(Pos.CENTER);
            submitPromptBox.setFillWidth(true);
            submitPromptBox.setStyle("-fx-border-color: black; -fx-padding: 10px;");
            submitPromptBox.setMaxWidth(Double.MAX_VALUE);

            String promptMessage = "Submit Votes?";
            Label promptLabel = new Label(promptMessage);

            if (accessibilityOptions.isHighContrastEnabled() == true) {
                if (currentFontStyle == LARGE_FONT_STYLE) {
                    promptLabel.setStyle("-fx-font-size: 60px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
                } else {
                    promptLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
                }
            } else {
                if (currentFontStyle == LARGE_FONT_STYLE) {
                    promptLabel.setStyle("-fx-font-size: 60px; -fx-font-weight: bold;");
                } else {
                    promptLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
                }
            }

            submitButton = new Button("Submit");
            submitButton.setStyle(currentFontStyle);
            submitButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            HBox.setHgrow(submitButton, Priority.ALWAYS);

            submitButton.setOnAction(e -> {
                controller.handleSubmit();
                submitButton.setDisable(true);
                prevButton.setDisable(true);

                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            });

            submitPromptBox.getChildren().addAll(promptLabel);
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(prevButton, submitButton);
        }
        contentBox.getChildren().setAll(submitPromptBox);
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.1));
        pauseTransition.setOnFinished(event -> {
            if (textToSpeech == true) {
                accessibilityOptions.speakText("Would you like to Submit Votes?");
            }
        });
        pauseTransition.play();
    }

    private void updateButtonVisibility() {
        int totalVotingPages = controller.getOffices().size();
        prevButton.setVisible(currentPage > 0);
        prevButton.setDisable(currentPage <= 0);
        nextButton.setVisible(currentPage < totalVotingPages + 1);
        nextButton.setDisable(currentPage >= totalVotingPages + 1);
        submitButton.setVisible(currentPage == totalVotingPages + 1);
    }

    private void applyCurrentFontStyleToUI(String fontSizeStyle, String color) {
        currentFontStyle = fontSizeStyle;
        prevButton.setStyle(currentFontStyle);
        nextButton.setStyle(currentFontStyle);
        submitButton.setStyle(currentFontStyle);
        optionElements.values().forEach(optionBox -> accessibilityOptions.updateFontSizeRecursive(optionBox, currentFontStyle));
    }

    private void createVotingPage(int pageIndex) {
        if (pageIndex < 0 || pageIndex >= controller.getOffices().size()) return;
        List<Map.Entry<String, List<String>>> offices = controller.getOffices();
        Map.Entry<String, List<String>> office = offices.get(pageIndex);
        contentBox.getChildren().clear();

        StringBuilder description = new StringBuilder();
        description.append("You are voting for the office of ").append(office.getKey()).append(".\n\n");

        for (String candidate : office.getValue()) {
            String[] candidateInfo = candidate.split("\\(");
            if (candidateInfo.length > 1) {
                String candidateName = candidateInfo[0].trim();
                String party = candidateInfo[1].replaceAll("[()]", "").trim();
                description.append(candidateName).append(" (").append(party).append(")\n");
            } else {
                description.append(candidate).append("\n");
            }
        }

        description.append("\nOr write in candidate.");

        VBox headerDescriptionBox = createHeader(office.getKey(), "(Vote for One)");
        VBox optionsBox = createOptionsBox(office.getKey(), office.getValue());

        contentBox.getChildren().addAll(headerDescriptionBox, optionsBox);
        restoreSelections(office.getKey());
        applyCurrentFontStyleToUI(currentFontStyle, "");

        if (accessibilityOptions.isHighContrastEnabled() == true) {
            nextButton.setStyle(accessibilityOptions.getButtonStyle(currentFontStyle));
            prevButton.setStyle(accessibilityOptions.getButtonStyle(currentFontStyle));
        }

        currentPage = pageIndex + 1;
        System.out.println("Inside createVotingPage: " + currentPage);
        updateButtonVisibility();

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.2));
        pauseTransition.setOnFinished(event -> {
            if (textToSpeech == true) {
                accessibilityOptions.speakText(description.toString());
            }
        });
        pauseTransition.play();
    }
    public static void main(String[] args) {
        launch(args);
    }
}