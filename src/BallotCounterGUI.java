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
import java.util.HashMap;
import java.util.Map;

public class BallotCounterGUI extends Application {
    private Map<String, Label> candidateVoteLabels = new HashMap<>();
    private Gson gson = new Gson();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));
        contentBox.setFillWidth(true);

        Label headerTitle = new Label("Election Results");
        headerTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        headerTitle.setAlignment(Pos.CENTER);
        headerTitle.setMaxWidth(Double.MAX_VALUE);

        // Read the election results from the file before creating the GUI elements
        VoteFormGUI.ElectionResults electionResults = readElectionResults();

        // Create the results boxes using the read election results
        VBox governorResultsBox = createResultsBox("Governor", electionResults.getResult().getGovernor());
        VBox presidentResultsBox = createResultsBox("President", electionResults.getResult().getPresident());

        contentBox.getChildren().addAll(headerTitle, governorResultsBox, presidentResultsBox);
        root.setCenter(contentBox);
        root.setStyle("-fx-background-color: #25283e;");

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Election Results");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createResultsBox(String position, Map<String, Integer> candidateVotes) {
        VBox resultsBox = new VBox(10);
        resultsBox.setAlignment(Pos.CENTER);
        resultsBox.setFillWidth(true);
        resultsBox.setStyle("-fx-border-color: white; -fx-border-width: 2px; -fx-padding: 10px;");

        Label positionLabel = new Label(position + " Results");
        positionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        resultsBox.getChildren().add(positionLabel);

        for (Map.Entry<String, Integer> entry : candidateVotes.entrySet()) {
            Label candidateLabel = new Label(entry.getKey() + ": " + entry.getValue());
            candidateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
            resultsBox.getChildren().add(candidateLabel);
            candidateVoteLabels.put(position + "_" + entry.getKey(), candidateLabel);
        }

        return resultsBox;
    }

    private VoteFormGUI.ElectionResults readElectionResults() {
        VoteFormGUI.ElectionResults electionResults = new VoteFormGUI.ElectionResults();
        try (FileReader reader = new FileReader("voteCount.json")) {
            electionResults = gson.fromJson(reader, VoteFormGUI.ElectionResults.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return electionResults;
    }
}
