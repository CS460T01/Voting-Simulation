package Tabulation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BallotCounterController {
    private static final String DATA_FILE_PATH = "data/Tabulator/vote_data.json";

    private Map<String, Map<String, Integer>> voteCounts = new HashMap<>();
    private int totalBallotsProcessed = 0;

    public BallotCounterController() {
    }

    public void processBallotFile(File ballotFile) {
        Gson gson = new Gson();
        String filePath = ballotFile.getAbsolutePath();
        try (FileReader reader = new FileReader(filePath)) {
            Ballot ballot = gson.fromJson(reader, Ballot.class);
            updateVoteCounts(ballot);
            saveState();
            printCurrentVoteCounts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateVoteCounts(Ballot ballot) {
        if (ballot.getPositions() != null) {
            ballot.getPositions().forEach((position, positionData) -> {
                String chosenCandidate = positionData.getVoterChoice();
                voteCounts.computeIfAbsent(position, k -> new HashMap<>());
                voteCounts.get(position).merge(chosenCandidate, 1, Integer::sum);
            });
            totalBallotsProcessed++;
        }
    }

    public void saveState() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(DATA_FILE_PATH)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadState() {
        Gson gson = new Gson();
        File dataFile = new File(DATA_FILE_PATH);
        if (dataFile.exists()) {
            try (FileReader reader = new FileReader(dataFile)) {
                Type controllerType = new TypeToken<BallotCounterController>(){}.getType();
                BallotCounterController loadedData = gson.fromJson(reader, controllerType);
                if (loadedData != null) {
                    this.voteCounts = loadedData.voteCounts;
                    this.totalBallotsProcessed = loadedData.totalBallotsProcessed;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTotalBallotsProcessed() {
        return totalBallotsProcessed;
    }

    public Map<String, Map<String, Integer>> getVoteCounts() {
        return voteCounts;
    }

    private void printCurrentVoteCounts() {
        System.out.println("Current Vote Counts:");
        voteCounts.forEach((position, candidates) -> {
            System.out.println("Position: " + position);
            candidates.forEach((candidate, count) ->
                    System.out.println("  " + candidate + ": " + count));
        });
        System.out.println("Total Ballots Processed: " + totalBallotsProcessed);
    }
}
