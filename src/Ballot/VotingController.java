package Ballot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class VotingController {
    private BallotResult ballotResult;
    private List<Map.Entry<String, List<String>>> offices;
    private Map<String, String> selections = new HashMap<>();


    public VotingController() {
        this.ballotResult = new BallotResult();
        initializeOffices();
    }


    // Method to initialize offices and candidates
    public void initializeOffices() {
        offices = new ArrayList<>();
        offices.add(new AbstractMap.SimpleEntry<>("President", Arrays.asList(
                "Joe Biden (Democrat)", "Donald Trump (Republican)"
        )));
        offices.add(new AbstractMap.SimpleEntry<>("Governor", Arrays.asList(
                "Michelle Lujan Grisham (Democrat)", "Susana Martinez (Republican)"
        )));
        offices.add(new AbstractMap.SimpleEntry<>("Mayor", Arrays.asList(
                "Tim Keller (Democrat)", "Jehiel Luciana (Independent)", "Džejlana Avedis (Republican)"
        )));
    }


    public List<Map.Entry<String, List<String>>> getOffices() {
        return offices;
    }

    public boolean selectionContainsKey (String position){
        if(selections.containsKey(position)){return true;}

        else{
            return false;
        }
    }


    public String getCandidatesForPosition(String position) {

        return selections.get(position);
    }

    public void handleSubmit() {
        System.out.println("Voting Results:");

        BallotResult ballotResult = new BallotResult();

        for (Map.Entry<String, List<String>> officeEntry : offices) {
            String position = officeEntry.getKey();
            List<String> candidatesList = officeEntry.getValue();

            PositionResult positionResult = new PositionResult();
            positionResult.setCandidates(candidatesList);
            positionResult.setVoterChoice(selections.getOrDefault(position, ""));

            ballotResult.addResult(position, positionResult);

            System.out.println(position + ": " + positionResult.getVoterChoice());
        }

        String ballotName = "Ballot_" + UUID.randomUUID().toString() + ".json";
        String ballotFolderPath = "data/Ballots/";
        String ballotFileName = ballotFolderPath + ballotName;

        Map<String, Object> wrappedResults = new HashMap<>();
        wrappedResults.put("positions", ballotResult.getResults());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(wrappedResults);

        try (FileWriter file = new FileWriter(ballotFileName)) {
            file.write(json);
            System.out.println("Results saved to " + ballotFileName);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the results to a file.");
            e.printStackTrace();
        }
    }

    public void createEmptyBallot() {
        BallotResult ballotResult = new BallotResult();

        for (Map.Entry<String, List<String>> officeEntry : offices) {
            String position = officeEntry.getKey();
            List<String> candidatesList = officeEntry.getValue();

            PositionResult positionResult = new PositionResult();
            positionResult.setCandidates(candidatesList);
            positionResult.setVoterChoice("");

            ballotResult.addResult(position, positionResult);
        }

        String emptyBallotName = "Ballot_" + UUID.randomUUID().toString() + ".json";

        // Wrap the results in a new map with "positions" key
        Map<String, Object> wrappedResults = new HashMap<>();
        wrappedResults.put("positions", ballotResult.getResults());

        // Convert the wrapped results object to a JSON string
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(wrappedResults);

        // Save the JSON string to a file
        try (FileWriter file = new FileWriter(emptyBallotName)) {
            file.write(json);
            System.out.println("Empty ballot saved to " + emptyBallotName);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the empty ballot to a file.");
            e.printStackTrace();
        }
    }


    public void saveSelection(String position, String s) {

        if(s.equals("")){
            selections.remove(position);
        }

        else{selections.put(position, s);}
    }
}