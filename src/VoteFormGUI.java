import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class VoteFormGUI extends Application {

    VBox contentBox;
    int currentPage;
    Map<String, String> selections = new HashMap<>();
    Button submitButton;
    Button prevButton;
    Button nextButton;
    VBox submitPromptBox;
    VBox accessibilityPromptBox;
    HBox buttonBox;
    private String currentFontStyle = NORMAL_FONT_STYLE;
    List<Map.Entry<String, List<String>>> offices;


    private static final String LARGE_FONT_STYLE = "-fx-font-size: 40px;";
    private static final String NORMAL_FONT_STYLE = "-fx-font-size: 20px;";
    Map<String, VBox> optionElements = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        contentBox = new VBox(5);
        contentBox.setPadding(new Insets(10));
        contentBox.setFillWidth(true);
        //createVoterIdPage(primaryStage);
        initializeOffices();
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
        submitButton.setOnAction(e -> handleSubmit());

        prevButton.setVisible(false);
        prevButton.setDisable(true);
        nextButton.setVisible(false);
        nextButton.setDisable(true);


        buttonBox.getChildren().addAll(prevButton, nextButton, submitButton);


        prevButton.setOnAction(e -> {
            System.out.println("Before Prev Action: " + currentPage);

            if (currentPage == offices.size() + 1) {
                // We are on the submit page and want to go back to the last voting page
                currentPage = offices.size();
                createVotingPage(currentPage - 1);
                buttonBox.getChildren().clear();
                buttonBox.getChildren().addAll(prevButton,nextButton);
            } else if (currentPage > 1) {
                // We are on a regular voting page and want to go back to the previous one
                saveSelection(offices.get(currentPage - 1).getKey());
                currentPage -= 2; // We need to subtract 2 because createVotingPage will add 1 back
                createVotingPage(currentPage);
            }

            System.out.println("After Prev Action: " + currentPage);
            updateButtonVisibility();
        });

        nextButton.setOnAction(e -> {
            System.out.println("Before Next Action: " + currentPage);

            if (currentPage <= offices.size()) {
                saveSelection(offices.get(currentPage - 1).getKey());
                if (currentPage < offices.size()) {
                    createVotingPage(currentPage); // This will increment currentPage
                }

                else {
                    createSubmitPrompt();
                    currentPage++; // Manually increment because createSubmitPrompt doesn't increment currentPage
                    buttonBox.getChildren().clear();
                    buttonBox.getChildren().addAll(prevButton,submitButton);
                    //submitButton.setVisible(true);
                    //nextButton.setVisible(false);
                    updateButtonVisibility();
                }
            }

            System.out.println("After Next Action: " + currentPage);
            updateButtonVisibility();
        });



        createAccessibilityOptionsPage(primaryStage);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);
        root.setBottom(buttonBox);

        //createVotingPage(0);

        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("Ballot");
        primaryStage.setScene(scene);
        primaryStage.show();

        //showVoterIdDialog(primaryStage);
    }

    private void handleCheckBoxAction(CheckBox selectedCheckBox, VBox optionBox) {
        if (selectedCheckBox.isSelected()) {
            // Unselect other checkboxes
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
            CheckBox checkBox = new CheckBox(candidate);
            checkBox.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
            checkBox.setMaxWidth(Double.MAX_VALUE);
            checkBox.setOnAction(e -> {handleCheckBoxAction(checkBox, optionBox);
                writeInField.setVisible(false);
            });
            optionBox.getChildren().add(checkBox);
        }

        // Add the write-in option
        CheckBox writeInOption = new CheckBox("Write-in");
        writeInOption.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
        writeInOption.setMaxWidth(Double.MAX_VALUE);
        writeInOption.setOnAction(e -> handleCheckBoxAction(writeInOption, optionBox));


        writeInOption.setOnAction(e -> {
            writeInField.setVisible(writeInOption.isSelected());
            if (writeInOption.isSelected()) {
                // Unselect other checkboxes
                optionBox.getChildren().filtered(node -> node instanceof CheckBox && node != writeInOption)
                        .forEach(node -> ((CheckBox) node).setSelected(false));
            }
        });

        optionBox.getChildren().addAll(writeInOption, writeInField);

        // Save the write-in TextField in a map if needed for later retrieval
        optionElements.put(position, optionBox);

        return optionBox;
    }

    private void saveSelection(String position) {
        VBox options = optionElements.get(position);
        if (options != null) {
            // Initialize the write-in checkbox and text field references
            CheckBox writeInCheckBox = null;
            TextField writeInField = null;

            // Find the write-in checkbox and text field
            for (Node node : options.getChildren()) {
                if (node instanceof CheckBox && "Write-in".equals(((CheckBox) node).getText())) {
                    writeInCheckBox = (CheckBox) node;
                } else if (node instanceof TextField) {
                    writeInField = (TextField) node;
                }
            }

            // Check if the write-in checkbox is selected and the text field is not empty
            if (writeInCheckBox != null && writeInCheckBox.isSelected() && writeInField != null && !writeInField.getText().isEmpty()) {
                selections.put(position, "Write-in: " + writeInField.getText());
            } else {
                // If not, find which checkbox is selected
                for (Node node : options.getChildren()) {
                    if (node instanceof CheckBox && ((CheckBox) node).isSelected()) {
                        selections.put(position, ((CheckBox) node).getText());
                        return;
                    }
                }
                // If no checkboxes are selected, remove the position from the selections
                selections.remove(position);
            }
        }
    }

    private VBox createHeader(String position, String instructions) {
        VBox headerDescriptionBox = new VBox(5);
        headerDescriptionBox.setAlignment(Pos.CENTER);
        headerDescriptionBox.setFillWidth(true);

        // Define the default styles
        String defaultTitleStyle = "-fx-font-size: 30px; -fx-font-weight: bold; -fx-border-color: black; -fx-padding: 10px;";
        String defaultLabelStyle = "-fx-font-size: 20px; -fx-font-weight: bold;";
        String defaultDescriptionStyle = "-fx-border-color: black; -fx-padding: 5px;";

        // Apply either the default style or the currentFontStyle based on the current selection
        String titleStyle = currentFontStyle.contains("40px") ? currentFontStyle : defaultTitleStyle;
        String labelStyle = currentFontStyle.contains("40px") ? currentFontStyle : defaultLabelStyle;

        Label headerTitle = new Label("BERNALILLO COUNTY");
        headerTitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-border-color: black; -fx-padding: 10px;");
        headerTitle.setAlignment(Pos.CENTER);
        headerTitle.setMaxWidth(Double.MAX_VALUE);

        VBox descriptionBox = new VBox(0);
        descriptionBox.setAlignment(Pos.CENTER);
        descriptionBox.setFillWidth(true);
        descriptionBox.setStyle(defaultDescriptionStyle);
        descriptionBox.setMaxWidth(Double.MAX_VALUE);

        // Use the default font size for these labels since they are part of the static header
        Label line1 = new Label("OFFICIAL BALLOT");
        //line1.setStyle(defaultTitleStyle);
        line1.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-padding: 10px;");
        Label line2 = new Label("OFFICIAL GENERAL ELECTION BALLOT");
        line2.setStyle(defaultLabelStyle);
        Label line3 = new Label("OF THE STATE OF NEW MEXICO");
        line3.setStyle(defaultLabelStyle);
        Label line4 = new Label("NOVEMBER 6, 2023");
        line4.setStyle(defaultLabelStyle);
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

    private void handleSubmit() {
        System.out.println("Voting Results:");

        BallotResult ballotResult = new BallotResult();

        for (Map.Entry<String, List<String>> officeEntry : offices) {
            String position = officeEntry.getKey();
            List<String> candidatesList = officeEntry.getValue();

            PositionResult positionResult = new PositionResult();
            positionResult.Candidates.addAll(candidatesList);
            positionResult.Voter_Choice = selections.getOrDefault(position, "");

            ballotResult.Ballot.put(position, positionResult);

            // Print the voting result for the current position
            System.out.println(position + ": " + positionResult.Voter_Choice);
        }

        // Convert the results object to a JSON string
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(ballotResult);

        // Save the JSON string to a file
        try (FileWriter file = new FileWriter("votingResults.json")) {
            file.write(json);
            System.out.println("Results saved to votingResults.json");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the results to a file.");
            e.printStackTrace();
        }

        prevButton.setDisable(true);
        nextButton.setDisable(true);
        submitButton.setDisable(true);
    }

    private void restoreSelections(String position) {
        VBox options = optionElements.get(position);
        if (options != null && selections.containsKey(position)) {
            String selectedOption = selections.get(position);

            // Find the write-in checkbox and text field
            CheckBox writeInCheckBox = null;
            TextField writeInField = null;
            for (Node node : options.getChildren()) {
                if (node instanceof CheckBox && "Write-in".equals(((CheckBox) node).getText())) {
                    writeInCheckBox = (CheckBox) node;
                } else if (node instanceof TextField) {
                    writeInField = (TextField) node;
                }
            }

            // Check if a write-in was previously selected
            if (selectedOption.startsWith("Write-in: ") && writeInCheckBox != null && writeInField != null) {
                writeInCheckBox.setSelected(true);
                writeInField.setText(selectedOption.replace("Write-in: ", ""));
                writeInField.setVisible(true);
            } else {
                // If not, restore the selection of other checkboxes
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

            Label promptLabel = new Label("Submit Votes?");
            promptLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

            submitButton = new Button("Submit");
            submitButton.setStyle(currentFontStyle);
            submitButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            HBox.setHgrow(submitButton, Priority.ALWAYS);
            submitButton.setOnAction(e -> handleSubmit());

            submitPromptBox.getChildren().addAll(promptLabel);
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(prevButton,submitButton);
        }

        contentBox.getChildren().setAll(submitPromptBox); // Replace all children with submitPromptBox
    }


    private static class BallotResult {
        Map<String, PositionResult> Ballot = new LinkedHashMap<>();
    }

    private static class PositionResult {
        List<String> Candidates = new ArrayList<>();
        String Voter_Choice;
    }

    private void updateButtonVisibility() {
        int totalVotingPages = offices.size();

        prevButton.setVisible(currentPage > 0);
        prevButton.setDisable(currentPage <= 0);

        nextButton.setVisible(currentPage < totalVotingPages + 1);
        nextButton.setDisable(currentPage >= totalVotingPages + 1);

        // Display the Submit button on the last voting page or as per your logic
        submitButton.setVisible(currentPage == totalVotingPages + 1);
    }


    private void createAccessibilityOptionsPage(Stage primaryStage) {
        contentBox.getChildren().clear();
        buttonBox.getChildren().clear();
        Button confirmButton = new Button("Confirm");

        accessibilityPromptBox = new VBox(20);
        accessibilityPromptBox.setAlignment(Pos.CENTER);
        accessibilityPromptBox.setFillWidth(true);
        accessibilityPromptBox.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        accessibilityPromptBox.setMaxWidth(Double.MAX_VALUE);

        Label accessibilityLabel = new Label("Select Accessibility Options:");
        accessibilityLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;"); // Use the current font style

        CheckBox largerFontCheckbox = new CheckBox("Use Larger Font");
        largerFontCheckbox.setStyle(currentFontStyle); // Use the current font style
        largerFontCheckbox.setSelected(currentFontStyle.equals(LARGE_FONT_STYLE)); // Set selected based on current style
        largerFontCheckbox.setOnAction(e -> {
            String newStyle = largerFontCheckbox.isSelected() ? LARGE_FONT_STYLE : NORMAL_FONT_STYLE;
            updateFontSize(newStyle);
        });

        CheckBox highContrastCheckbox = new CheckBox("Use High Contrast");
        highContrastCheckbox.setStyle(currentFontStyle); // Use the current font style

        CheckBox textToSpeechCheckbox = new CheckBox("Enable Text to Speech");
        textToSpeechCheckbox.setStyle(currentFontStyle); // Use the current font style

        confirmButton.setStyle(currentFontStyle); // Use the current font style
        confirmButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(confirmButton, Priority.ALWAYS); // Make the button grow horizontally

        confirmButton.setOnAction(e -> {
            if (largerFontCheckbox.isSelected()) {
                // Apply larger font settings across the UI
            }
            if (highContrastCheckbox.isSelected()) {
                // Apply high contrast settings across the UI
            }
            if (textToSpeechCheckbox.isSelected()) {
                // Activate text to speech functionality
            }

            createVotingPage(0); // Navigate to the first voting page
            //currentPage = 1;
            updateButtonVisibility();
            confirmButton.setVisible(false);
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(prevButton, nextButton); // Add back the navigation buttons
            updateButtonVisibility(); // Make sure to update the visibility based on the current page

            //createVoterIdPage(primaryStage); // Proceed to the voter ID page after confirming options
        });

        accessibilityPromptBox.getChildren().addAll(accessibilityLabel);
        contentBox.getChildren().addAll(accessibilityPromptBox, largerFontCheckbox, highContrastCheckbox, textToSpeechCheckbox, confirmButton);
        buttonBox.getChildren().add(confirmButton);

    }

    private void updateFontSize(String fontSizeStyle) {
        currentFontStyle = fontSizeStyle; // Update the current font style
        applyCurrentFontStyleToUI(); // Apply the new font style to the UI
    }

    private void applyCurrentFontStyleToUI() {
        // Update the style for buttons as an example
        prevButton.setStyle(currentFontStyle);
        nextButton.setStyle(currentFontStyle);
        submitButton.setStyle(currentFontStyle);

        // Apply the current font style to the options and headers
        optionElements.values().forEach(optionBox -> updateFontSizeRecursive(optionBox, currentFontStyle));
        // You might need additional lines here to update other parts of the UI
    }

    private void updateFontSizeRecursive(Pane parent, String fontSizeStyle) {
        for (Node child : parent.getChildren()) {
            if (child instanceof Text) {
                ((Text) child).setStyle(fontSizeStyle);
            } else if (child instanceof Label) {
                ((Label) child).setStyle(fontSizeStyle);
            } else if (child instanceof Button) {
                ((Button) child).setStyle(fontSizeStyle);
            } else if (child instanceof CheckBox) {
                ((CheckBox) child).setStyle(fontSizeStyle);
            } else if (child instanceof TextField) {
                ((TextField) child).setStyle(fontSizeStyle);
            } else if (child instanceof Pane) {
                updateFontSizeRecursive((Pane) child, fontSizeStyle);
            }
        }
    }

    private void initializeOffices() {
        offices = new ArrayList<>();
        offices.add(new AbstractMap.SimpleEntry<>("Governor", Arrays.asList("1","Michelle Lujan Grisham", "Susana Martinez")));
        offices.add(new AbstractMap.SimpleEntry<>("Mayor", Arrays.asList("2","Tim Keller", "Jehiel Luciana", "Džejlana Avedis")));
        offices.add(new AbstractMap.SimpleEntry<>("Senator", Arrays.asList("3","Ben Ray Lujan", "Martin Heinrich")));
        offices.add(new AbstractMap.SimpleEntry<>("Dog", Arrays.asList("4","Husky", "Golden retriever", "German Shepard")));
        // Add more offices and candidates as needed
    }

    private void createVotingPage(int pageIndex) {
        if (pageIndex < 0 || pageIndex >= offices.size()) {
            return; // Index out of bounds
        }

        Map.Entry<String, List<String>> office = offices.get(pageIndex);
        contentBox.getChildren().clear();

        VBox headerDescriptionBox = createHeader(office.getKey(), "(Vote for One)");
        VBox optionsBox = createOptionsBox(office.getKey(), office.getValue());

        contentBox.getChildren().addAll(headerDescriptionBox, optionsBox);
        restoreSelections(office.getKey()); // Restore selections for this position
        applyCurrentFontStyleToUI();

        currentPage = pageIndex + 1; // Update current page index
        System.out.println("Inside createVotingPage: " + currentPage);
        updateButtonVisibility(); // Update the visibility of navigation buttons
    }

    public static void main(String[] args) {
        launch(args);
    }
}