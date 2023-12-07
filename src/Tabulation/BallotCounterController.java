package Tabulation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.stream.Stream;


public class BallotCounterController {
    private static final String DATA_FILE_PATH = "data/Tabulator/vote_data.json";
    private static final String COUNTED_BALLOTS_DIR = "data/CountedBallots";
    private static final String ABSENTEE_BALLOTS_DIR = "data/AbsenteeBallots";

    private Map<String, Map<String, Integer>> voteCounts = new HashMap<>();
    private int totalBallotsProcessed = 0;

    public BallotCounterController() {
        File countedBallotsDir = new File(COUNTED_BALLOTS_DIR);
        if (!countedBallotsDir.exists()) {
            countedBallotsDir.mkdirs();
        }
        processAbsenteeBallots();
    }

    private void processAbsenteeBallots() {
        try (Stream<Path> paths = Files.walk(Paths.get(ABSENTEE_BALLOTS_DIR))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> processBallotFile(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processBallotFile(File ballotFile) {
        Gson gson = new Gson();
        String filePath = ballotFile.getAbsolutePath();
        Ballot ballot = null;

        try (FileReader reader = new FileReader(filePath)) {
            ballot = gson.fromJson(reader, Ballot.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ballot != null) {
            updateVoteCounts(ballot);
            saveState();
            printCurrentVoteCounts();
        }

        moveBallotFile(ballotFile);
    }

    private void moveBallotFile(File ballotFile) {
        try {
            File destFile = new File(COUNTED_BALLOTS_DIR + File.separator + ballotFile.getName());
            Files.move(ballotFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved ballot file to: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to move ballot file: " + ballotFile.getName());
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

    public void exportElectionResults() {
        String fileName = "ELECTION-RESULTS.txt";
        String projectDirectory = System.getProperty("user.dir");
        String filePath = projectDirectory + File.separator + fileName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Map<String, Integer>> positionEntry : voteCounts.entrySet()) {
                String position = positionEntry.getKey();
                writer.write("[" + position + "]\n");
                Map<String, Integer> counts = positionEntry.getValue();
                for (Map.Entry<String, Integer> candidateEntry : counts.entrySet()) {
                    String line = candidateEntry.getKey() + " : " + candidateEntry.getValue() + "\n";
                    writer.write(line);
                }
                writer.write("\n");
            }
            System.out.println("Election results exported to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
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
