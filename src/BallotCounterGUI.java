import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;

public class BallotCounterGUI extends Application {
    private Register register = new Register();
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

        Label headerTitle = new Label("Header");
        headerTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        headerTitle.setAlignment(Pos.CENTER_LEFT);
        headerTitle.setMaxWidth(Double.MAX_VALUE);
        headerBox.setSpacing(10);
        headerBox.getChildren().addAll(headerTitle);

        // Container for the vote counter
        VBox voteCounterBox = new VBox(5);
        voteCounterBox.setAlignment(Pos.CENTER);
        voteCounterBox.setFillWidth(true);
        voteCounterBox.setMinHeight(400);
        voteCounterBox.setStyle("-fx-border-color: white; -fx-border-width: 3px; -fx-padding: 10px;");

        Label titleLabel = new Label("Total Votes:");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        titleLabel.setAlignment(Pos.CENTER);

        voteLabel = new Label(String.valueOf(voteCount));
        voteLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        voteLabel.setAlignment(Pos.CENTER);

        voteCounterBox.getChildren().addAll(titleLabel, voteLabel);
        contentBox.getChildren().addAll(headerBox, voteCounterBox);
        root.setCenter(contentBox);
        root.setStyle("-fx-background-color: #25283e;");

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Ballot Counter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}
