package Tabulation;

import Ballot.BallotResult;
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

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class BallotCounterGUI extends Application {
    private Stage primaryStage;
    private BallotCounterController controller;
    private Label voteLabel;
    private Label timeLabel;
    private Label dateLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new BallotCounterController();
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createVoteCounter());
        root.setBottom(createFooter());

        root.setStyle("-fx-background-color: #25283e;");

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Ballot Counter");
        primaryStage.setScene(scene);
        primaryStage.show();

        startClock();
    }

    private void processBallot() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File ballotFile = fileChooser.showOpenDialog(primaryStage);

        if (ballotFile != null) {
            controller.processBallotFile(ballotFile);
        }

        voteLabel.setText(String.format("%05d", controller.getTotalBallotsProcessed()));
    }



    private VBox createHeader() {
        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setFillWidth(true);
        headerBox.setStyle("-fx-border-color: white; -fx-padding: 10px; -fx-border-width: 3px;");
        BorderPane.setMargin(headerBox, new Insets(5));

        Label headerTitle = new Label("Ballot Counter");
        headerTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        headerTitle.setAlignment(Pos.CENTER_LEFT);
        headerTitle.setMaxWidth(Double.MAX_VALUE);
        headerBox.setSpacing(10);
        headerBox.getChildren().add(headerTitle);

        return headerBox;
    }

    private VBox createVoteCounter() {
        VBox voteCounterBox = new VBox();
        voteCounterBox.setFillWidth(true);
        voteCounterBox.setMinHeight(400);
        voteCounterBox.setStyle("-fx-border-color: white; -fx-border-width: 3px; -fx-padding: 10px;");
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

        voteLabel = new Label(String.format("%05d", 0));
        voteLabel.setStyle("-fx-text-fill: white; -fx-font-size: 38px; -fx-font-weight: bold; -fx-font-family: monospace;");

        Button submitBallotButton = new Button("Submit Ballot");
        submitBallotButton.setOnAction(e -> {
            processBallot();
        });

        bottomContent.getChildren().addAll(titleLabel, divider, voteLabel, submitBallotButton);
        voteCounterBox.getChildren().addAll(topContent, bottomContent);

        return voteCounterBox;
    }

    private HBox createFooter() {
        HBox footerBox = new HBox();
        footerBox.setFillHeight(true);
        footerBox.setStyle("-fx-border-color: white; -fx-padding: 10px; -fx-border-width: 3px;");
        footerBox.setAlignment(Pos.CENTER);
        BorderPane.setMargin(footerBox, new Insets(5));

        timeLabel = new Label();
        dateLabel = new Label();
        timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        dateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        VBox leftSection = new VBox();
        leftSection.getChildren().addAll(timeLabel, dateLabel);

        VBox centerSection = createCenterSection();
        VBox rightSection = createRightSection();

        footerBox.getChildren().addAll(leftSection, centerSection, rightSection);
        HBox.setHgrow(centerSection, Priority.ALWAYS);

        return footerBox;
    }

    private VBox createCenterSection() {
        VBox centerSection = new VBox(2);
        Label locationLabel = new Label("Location: POLLING LOCATION 1");
        locationLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Label tabulatorIDLabel = new Label("Tabulator ID: 23480");
        tabulatorIDLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        centerSection.getChildren().addAll(locationLabel, tabulatorIDLabel);
        centerSection.setAlignment(Pos.CENTER);

        return centerSection;
    }

    private VBox createRightSection() {
        VBox rightSection = new VBox();
        rightSection.setAlignment(Pos.CENTER_RIGHT);
        Label firmwareLabel = new Label("Firmware: 1.2.7");
        firmwareLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        rightSection.getChildren().add(firmwareLabel);
        Button loginButton = new Button("Admin Login");
        loginButton.setOnAction(e -> {
            System.out.println("Login button pressed");
            // Switch to login screen

        });

        loginButton.setStyle("-fx-background-color: #25283e; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-color: white; ");
        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle("-fx-background-color: #161825; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-color: white;");
        });
        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle("-fx-background-color: #25283e; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-color: white;");
        });
        rightSection.getChildren().add(loginButton);

        return rightSection;
    }

    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateTime()),
                new KeyFrame(Duration.seconds(60)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void updateTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(now.format(timeFormatter));
        dateLabel.setText(now.format(dateFormatter));
    }
}
