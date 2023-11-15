package Tabulation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BallotCounterGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setFillWidth(true);
        headerBox.setStyle("-fx-border-color: white; -fx-padding: 10px; -fx-border-width: 3px;");
        BorderPane.setMargin(headerBox, new Insets(5));
        root.setTop(headerBox);

        Label headerTitle = new Label("Ballot Counter");
        headerTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        headerTitle.setAlignment(Pos.CENTER_LEFT);
        headerTitle.setMaxWidth(Double.MAX_VALUE);
        headerBox.setSpacing(10);
        headerBox.getChildren().addAll(headerTitle);

        // Container for the vote counter
        VBox voteCounterBox = new VBox();
        voteCounterBox.setFillWidth(true);
        voteCounterBox.setMinHeight(400);
        voteCounterBox.setStyle("-fx-border-color: white; -fx-border-width: 3px; -fx-padding: 10px;");
        root.setCenter(voteCounterBox);
        BorderPane.setMargin(voteCounterBox, new Insets(5));

        VBox topContent = new VBox();
        topContent.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(topContent, new Insets(35, 0, 0, 0));

        Label castBallotLabel = new Label("Cast your Ballot below!");
        castBallotLabel.setStyle("-fx-text-fill: white; -fx-font-size: 30px;");
        topContent.getChildren().add(castBallotLabel);

        VBox bottomContent = new VBox(10);
        bottomContent.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(bottomContent, Priority.ALWAYS);
        VBox.setMargin(bottomContent, new Insets(0, 0, 45, 0));

        Label titleLabel = new Label("Total Votes:");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");

        Line divider = new Line(0, 0, 200, 0);
        divider.setStyle("-fx-stroke: white; -fx-stroke-width: 2;");

        int voteCount = 7;
        Label voteLabel = new Label(String.format("%05d", voteCount));
        voteLabel.setStyle("-fx-text-fill: white; -fx-font-size: 38px; -fx-font-weight: bold; -fx-font-family: monospace;");

        Button submitBallotButton = new Button("Submit Ballot");
        submitBallotButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.showOpenDialog(primaryStage);
        });

        bottomContent.getChildren().addAll(titleLabel, divider, voteLabel, submitBallotButton);
        voteCounterBox.getChildren().addAll(topContent, bottomContent);


        HBox footerBox = new HBox();
        footerBox.setFillHeight(true);
        footerBox.setStyle("-fx-border-color: white; -fx-padding: 10px; -fx-border-width: 3px;");
        footerBox.setAlignment(Pos.CENTER);
        BorderPane.setMargin(footerBox, new Insets(5));
        root.setBottom(footerBox);



        VBox leftSection = new VBox();
        Label timeLabel = new Label();
        Label dateLabel = new Label();
        timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        dateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        updateTime(timeLabel, dateLabel); // Initial update

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateTime(timeLabel, dateLabel)),
                new KeyFrame(Duration.seconds(60)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

        leftSection.getChildren().addAll(timeLabel, dateLabel);

        VBox centerSection = new VBox(2); // Spacing between location and ID
        Label locationLabel = new Label("Location: POLLING LOCATION 1");
        locationLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label tabulatorIDLabel = new Label("Tabulator ID: 23480");
        tabulatorIDLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        centerSection.getChildren().addAll(locationLabel, tabulatorIDLabel);
        centerSection.setAlignment(Pos.CENTER);

        VBox rightSection = new VBox();
        rightSection.setAlignment(Pos.CENTER_RIGHT);
        Label firmwareLabel = new Label("Firmware: 1.0");
        firmwareLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        rightSection.getChildren().add(firmwareLabel);

        footerBox.getChildren().addAll(leftSection, centerSection, rightSection);
        HBox.setHgrow(centerSection, Priority.ALWAYS);
        root.setStyle("-fx-background-color: #25283e;");

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Ballot Counter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateTime(Label timeLabel, Label dateLabel) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(now.format(timeFormatter));
        dateLabel.setText(now.format(dateFormatter));
    }


}
