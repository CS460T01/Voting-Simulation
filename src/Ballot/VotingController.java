package Ballot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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


    // Method to get candidates for a position
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

            // Print the voting result for the current position
            System.out.println(position + ": " + positionResult.getVoterChoice());
        }

        // Wrap the results in a new map with "positions" key
        Map<String, Object> wrappedResults = new HashMap<>();
        wrappedResults.put("positions", ballotResult.getResults());

        // Convert the wrapped results object to a JSON string
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(wrappedResults);

        // Save the JSON string to a file
        try (FileWriter file = new FileWriter("votingResults.json")) {
            file.write(json);
            System.out.println("Results saved to votingResults.json");
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

        // Wrap the results in a new map with "positions" key
        Map<String, Object> wrappedResults = new HashMap<>();
        wrappedResults.put("positions", ballotResult.getResults());

        // Convert the wrapped results object to a JSON string
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(wrappedResults);

        // Save the JSON string to a file
        try (FileWriter file = new FileWriter("emptyBallot.json")) {
            file.write(json);
            System.out.println("Empty ballot saved to emptyBallot.json");
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
