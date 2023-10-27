import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteFormGUI extends Application {
    VBox contentBox;
    int currentPage;
    Map<String, String> selections = new HashMap<>();
    Button submitButton;
    Button prevButton;
    Button nextButton;
    VBox submitPromptBox;

    Map<String, VBox> optionElements = new HashMap<>();

    //testing this branch

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        contentBox = new VBox(5);
        contentBox.setPadding(new Insets(10));
        contentBox.setFillWidth(true);

        createGovernorPage(); // Start at the Governor page
        currentPage = 1;

        HBox buttonBox = new HBox();

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

        buttonBox.getChildren().addAll(prevButton, nextButton);

        prevButton.setOnAction(e -> {
            if (currentPage == 2) {
                createGovernorPage();
                currentPage = 1;
            } else if (currentPage == 3) {
                createPresidentPage();
                currentPage = 2;
                nextButton.setText("Next");
            }
        });

        nextButton.setOnAction(e -> {

            if (currentPage == 1) {
                saveSelection("Governor");
                createPresidentPage();
                currentPage = 2;
            } else if (currentPage == 2) {
                saveSelection("President");
                createSubmitPrompt();
                currentPage = 3;
                nextButton.setText("Submit");
            } else if (currentPage == 3) {
                handleSubmit();
            }
        });

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("Ballot");
        primaryStage.setScene(scene);
        primaryStage.show();

        showVoterIdDialog(primaryStage);
    }

    private void createGovernorPage() {
        contentBox.getChildren().clear();

        VBox headerDescriptionBox = createHeader("For Governor", "(Vote for One)");
        VBox governorOptions = createOptionsBox("Governor");

        contentBox.getChildren().addAll(headerDescriptionBox, governorOptions);
        restoreSelections("Governor");
    }

    private void createPresidentPage() {
        contentBox.getChildren().clear();

        VBox headerDescriptionBox = createHeader("For President", "(Vote for One)");
        VBox presidentOptions = createOptionsBox("President");

        contentBox.getChildren().addAll(headerDescriptionBox, presidentOptions);
        restoreSelections("President");
    }


    private VBox createOptionsBox(String position) {

        if (optionElements.containsKey(position)) {
            return optionElements.get(position);
        }

        VBox optionBox = new VBox(10);

        CheckBox option1 = new CheckBox("CANDIDATE 1");
        option1.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
        option1.setMaxWidth(Double.MAX_VALUE);

        CheckBox option2 = new CheckBox("CANDIDATE 2");
        option2.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
        option2.setMaxWidth(Double.MAX_VALUE);

        CheckBox option3 = new CheckBox("WRITE IN");
        option3.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
        option3.setMaxWidth(Double.MAX_VALUE);

        TextField writeInField = new TextField();
        writeInField.setPromptText("Enter candidate name...");
        writeInField.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-padding: 10px;");
        writeInField.setVisible(false);

        option1.setOnAction(e -> {
            if (option1.isSelected()) {
                option2.setSelected(false);
                option3.setSelected(false);
                writeInField.setVisible(false);
            }
        });

        option2.setOnAction(e -> {
            if (option2.isSelected()) {
                option1.setSelected(false);
                option3.setSelected(false);
                writeInField.setVisible(false);
            }
        });

        option3.setOnAction(e -> {
            if (option3.isSelected()) {
                option1.setSelected(false);
                option2.setSelected(false);
                writeInField.setVisible(true);
            } else {
                writeInField.setVisible(false);
            }
        });

        optionBox.getChildren().addAll(option1, option2, option3, writeInField);
        optionElements.put(position, optionBox);
        return optionBox;
    }

    private void saveSelection(String position) {
        VBox options = optionElements.get(position);
        if (options != null) {
            for (Node node : options.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    if (checkBox.isSelected()) {
                        if ("WRITE IN".equals(checkBox.getText())) {
                            TextField writeInField = (TextField) options.getChildren().get(3); // Assuming the TextField is the next node
                            if (!writeInField.getText().isEmpty()) {
                                selections.put(position, writeInField.getText());
                                return;
                            }
                        } else {
                            selections.put(position, checkBox.getText());
                            return;
                        }
                    }
                }
            }
        }
        selections.remove(position); // Remove the selection if no option is selected
    }


    private VBox createHeader(String position, String instructions) {
        VBox headerDescriptionBox = new VBox(5);
        headerDescriptionBox.setAlignment(Pos.CENTER);
        headerDescriptionBox.setFillWidth(true);

        Label headerTitle = new Label("BERNALILLO COUNTY");
        headerTitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-border-color: black; -fx-padding: 10px;");
        headerTitle.setAlignment(Pos.CENTER);
        headerTitle.setMaxWidth(Double.MAX_VALUE);

        VBox descriptionBox = new VBox(0);
        descriptionBox.setAlignment(Pos.CENTER);
        descriptionBox.setFillWidth(true);
        descriptionBox.setStyle("-fx-border-color: black; -fx-padding: 5px;");
        descriptionBox.setMaxWidth(Double.MAX_VALUE);

        Label line1 = new Label("OFFICIAL BALLOT");
        line1.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        Label line2 = new Label("OFFICIAL GENERAL ELECTION BALLOT");
        line2.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label line3 = new Label("OF THE STATE OF NEW MEXICO");
        line3.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label line4 = new Label("NOVEMBER 6, 2023");
        line4.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        descriptionBox.getChildren().addAll(line1, line2, line3, line4);

        headerDescriptionBox.setSpacing(10);
        headerDescriptionBox.getChildren().addAll(headerTitle, descriptionBox);

        VBox candidateLabelBox = new VBox();
        Label candidateLabel = new Label(position);
        Label labelDirections = new Label(instructions);

        candidateLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        labelDirections.setStyle("-fx-font-size: 20px;");

        candidateLabelBox.setAlignment(Pos.CENTER);
        candidateLabelBox.setMaxWidth(Double.MAX_VALUE);
        candidateLabelBox.setStyle("-fx-border-color: black;");
        candidateLabelBox.getChildren().addAll(candidateLabel, labelDirections);

        headerDescriptionBox.getChildren().add(candidateLabelBox);

        return headerDescriptionBox;
    }

    private void handleSubmit() {
        System.out.println("Voting Results:");
        List<String> positions = Arrays.asList("Governor", "President"); // Add all the positions here
        for (String position : positions) {
            if (selections.containsKey(position)) {
                System.out.println(position + ": " + selections.get(position));
            } else {
                System.out.println(position + ": blank");
            }
        }

        prevButton.setDisable(true);
        nextButton.setDisable(true);
        submitButton.setDisable(true);
    }

    private void restoreSelections(String position) {
        VBox options = optionElements.get(position);
        if (options != null && selections.containsKey(position)) {
            String selectedOption = selections.get(position);
            for (Node node : options.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    if (selectedOption.equals(checkBox.getText())) {
                        checkBox.setSelected(true);
                    }
                } else if (node instanceof TextField) {
                    TextField textField = (TextField) node;
                    if (selectedOption.startsWith("WRITE IN: ")) {
                        textField.setText(selectedOption.substring(10));
                        textField.setVisible(true);
                        // Assuming there's a write-in checkbox
                        ((CheckBox) options.getChildren().get(2)).setSelected(true);
                    }
                }
            }
        }
    }

    private void createSubmitPrompt() {
        contentBox.getChildren().clear();

        submitPromptBox = new VBox(20);
        submitPromptBox.setAlignment(Pos.CENTER);
        submitPromptBox.setFillWidth(true);
        submitPromptBox.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        submitPromptBox.setMaxWidth(Double.MAX_VALUE);

        Label promptLabel = new Label("Submit Votes?");
        promptLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        submitPromptBox.getChildren().add(promptLabel);

        contentBox.getChildren().add(submitPromptBox);
    }

    private void showVoterIdDialog(Stage primaryStage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(primaryStage);
        VBox dialogVBox = new VBox(20);
        dialogVBox.setPadding(new Insets(10));

        TextField voterIdField = new TextField();
        voterIdField.setPromptText("Enter Voter ID");
        Button submitButton = new Button("Submit");
        Label errorLabel = new Label();
        errorLabel.setTextFill(javafx.scene.paint.Color.RED);

        submitButton.setOnAction(e -> {
            String voterId = voterIdField.getText();
            if (voterId.matches("\\d{5}")) { // Check if voter ID is exactly 5 digits long
                dialogStage.close();
                primaryStage.show();
            } else {
                errorLabel.setText("Invalid Voter ID. Please enter a 5 digit ID.");
            }
        });

        dialogVBox.getChildren().addAll(new Label("Please Enter Your 5-Digit Voter ID:"), voterIdField, submitButton, errorLabel);
        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.setTitle("Voter ID Validation");
        dialogStage.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
