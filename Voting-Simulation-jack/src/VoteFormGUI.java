import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VoteFormGUI extends Application {
    VBox contentBox;
    int currentPage;

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
        buttonBox.getChildren().addAll(prevButton, nextButton);

        nextButton.setOnAction(e -> {
            if (currentPage == 1) {
                createPresidentPage();
                currentPage = 2;
            }
            // You can add more pages here if needed
        });

        prevButton.setOnAction(e -> {
            if (currentPage == 2) {
                createGovernorPage();
                currentPage = 1;
            }
            // You can add functionality to go back to more pages here if needed
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
        VBox optionBox = createOptionsBox();
        contentBox.getChildren().addAll(headerDescriptionBox, optionBox);
    }

    private void createPresidentPage() {
        contentBox.getChildren().clear();

        VBox headerDescriptionBox = createHeader("For President", "(Vote for One)");
        VBox optionBox = createOptionsBox();
        contentBox.getChildren().addAll(headerDescriptionBox, optionBox);
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

    private VBox createOptionsBox() {
        VBox optionBox = new VBox();

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

    public static void main(String[] args) {
        launch(args);
    }
}
