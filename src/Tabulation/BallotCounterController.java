package Tabulation;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BallotCounterController {

    private Map<String, Map<String, Integer>> voteCounts = new HashMap<>();
    private int totalBallotsProcessed = 0;

    public void processBallotFile(File ballotFile) {
        Gson gson = new Gson();
        String filePath = ballotFile.getAbsolutePath();
        try (FileReader reader = new FileReader(filePath)) {
            Ballot ballot = gson.fromJson(reader, Ballot.class);
            updateVoteCounts(ballot);
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
