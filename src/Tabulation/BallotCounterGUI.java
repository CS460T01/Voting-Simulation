package Tabulation;

import Registration.Register;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BallotCounterGUI extends Application {
    private static int voteCount = 0;
    private Label voteLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Container for all content on the GUI
        VBox contentBox = new VBox(5);
        contentBox.setPadding(new Insets(10 ));
        contentBox.setMinHeight(600);
        contentBox.setFillWidth(true);

        // Container for the header
        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setFillWidth(true);
        headerBox.setStyle("-fx-border-color: white; -fx-padding: 10px; -fx-border-width: 3px;");

        Label headerTitle = new Label("Tabulator");
        headerTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        headerTitle.setAlignment(Pos.CENTER_LEFT);
        headerTitle.setMaxWidth(Double.MAX_VALUE);
        headerBox.setSpacing(10);
        headerBox.getChildren().addAll(headerTitle);

        // Container for the vote counter
        VBox voteCounterBox = new VBox(10); // Increased spacing for better layout
        voteCounterBox.setAlignment(Pos.CENTER);
        voteCounterBox.setFillWidth(true);
        voteCounterBox.setMinHeight(400);
        voteCounterBox.setStyle("-fx-border-color: white; -fx-border-width: 3px; -fx-padding: 10px;");

        Label castBallotLabel = new Label("Please cast your Ballot below!");
        castBallotLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        castBallotLabel.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Total Votes:");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        titleLabel.setAlignment(Pos.CENTER);

        Line divider = new Line(0, 0, 200, 0); // Width of 200
        divider.setStyle("-fx-stroke: white; -fx-stroke-width: 2;");

        voteLabel = new Label(String.valueOf(voteCount));
        voteLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        voteLabel.setAlignment(Pos.CENTER);

        Button submitBallotButton = new Button("Submit Ballot");
        submitBallotButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            // Configure file chooser if needed
            fileChooser.showOpenDialog(primaryStage);
            // Add logic to handle the selected file
        });

        voteCounterBox.getChildren().addAll(castBallotLabel, titleLabel, divider, voteLabel, submitBallotButton);



        // Container for the footer
        HBox footerBox = new HBox(5);
        footerBox.setFillHeight(true);
        footerBox.setStyle("-fx-border-color: white; -fx-padding: 10px; -fx-border-width: 3px;");

        //footer content
        //Location label
        Label locationLabel = new Label("Location: POLLING LOCATION 1");
        locationLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        locationLabel.setAlignment(Pos.CENTER_LEFT);
        locationLabel.setMaxWidth(Double.MAX_VALUE);
        footerBox.setSpacing(10);
        footerBox.getChildren().addAll(locationLabel);

        //Tabulator ID Label
        Label tabulatorIDLabel = new Label("Tabulator ID: 23480");
        tabulatorIDLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        tabulatorIDLabel.setAlignment(Pos.CENTER);
        tabulatorIDLabel.setMaxWidth(Double.MAX_VALUE);
        footerBox.setSpacing(10);
        footerBox.getChildren().addAll(tabulatorIDLabel);

        //Firmware Label
        Label firmwareLabel = new Label("Firmware: 1.0");
        firmwareLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        firmwareLabel.setAlignment(Pos.CENTER_RIGHT);
        firmwareLabel.setMaxWidth(Double.MAX_VALUE);
        footerBox.setSpacing(10);
        footerBox.getChildren().addAll(firmwareLabel);




        contentBox.getChildren().addAll(headerBox, voteCounterBox, footerBox);
        root.setCenter(contentBox);
        root.setStyle("-fx-background-color: #25283e;");

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Ballot Counter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
