import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class VoteFormGUI extends Application {
    VBox contentBox;
    int currentPage;
    Map<String, String> selections = new HashMap<>();
    Button submitButton;

    Map<String, VBox> optionElements = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        contentBox = new VBox(5);
        contentBox.setPadding(new Insets(10));
        contentBox.setFillWidth(true);


        createGovernorPage(); // Start at the Governor page
        currentPage = 1;

        HBox buttonBox = new HBox();
        Button prevButton = new Button("Previous");
        prevButton.setStyle("-fx-font-size: 20px;");
        Button nextButton = new Button("Next");
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

        buttonBox.getChildren().addAll(prevButton, submitButton, nextButton);

        prevButton.setOnAction(e -> {
            if (currentPage == 2) {
                createGovernorPage();
                currentPage = 1;
            }
            // You can add functionality to go back to more pages here if needed
        });

        nextButton.setOnAction(e -> {
            if (currentPage == 1) {
                saveSelection("Governor");
                createPresidentPage();
                currentPage = 2;
            } else if (currentPage == 2) {
                saveSelection("President");
                createLastPage(); // Assuming this is your last page
                currentPage = 3;
                nextButton.setVisible(false);
                submitButton.setVisible(true);
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
    }

    private void createGovernorPage() {
        contentBox.getChildren().clear();

        VBox headerDescriptionBox = createHeader("For Governor", "(Vote for One)");
        VBox governorOptions = createOptionsBox();
        optionElements.put("Governor", governorOptions);

        contentBox.getChildren().addAll(headerDescriptionBox, governorOptions);
    }

    private void createPresidentPage() {
        contentBox.getChildren().clear();

        VBox headerDescriptionBox = createHeader("For President", "(Vote for One)");
        VBox presidentOptions = createOptionsBox();
        optionElements.put("President", presidentOptions);

        contentBox.getChildren().addAll(headerDescriptionBox, presidentOptions);
    }



    private VBox createOptionsBox() {
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
        return optionBox;
    }


    private void saveSelection(String position) {
        VBox options = optionElements.get(position);
        if (options != null) {
            for (Node node : options.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    if (checkBox.isSelected()) {
                        selections.put(position, checkBox.getText());
                        return;
                    }
                } else if (node instanceof TextField) {
                    TextField textField = (TextField) node;
                    if (!textField.getText().isEmpty()) {
                        selections.put(position, "WRITE IN: " + textField.getText());
                        return;
                    }
                }
            }
        }
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

    private void createLastPage() {
        // Logic to create the last page of your voting process
    }

    private void handleSubmit() {
        System.out.println("Voting Results:");
        for (Map.Entry<String, String> entry : selections.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
