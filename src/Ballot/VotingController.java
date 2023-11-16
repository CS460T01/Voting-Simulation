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
        offices.add(new AbstractMap.SimpleEntry<>("Governor", Arrays.asList("1","Michelle Lujan Grisham", "Susana Martinez")));
        offices.add(new AbstractMap.SimpleEntry<>("Mayor", Arrays.asList("2","Tim Keller", "Jehiel Luciana", "Džejlana Avedis")));
        offices.add(new AbstractMap.SimpleEntry<>("Senator", Arrays.asList("3","Ben Ray Lujan", "Martin Heinrich")));
        offices.add(new AbstractMap.SimpleEntry<>("Dog", Arrays.asList("4","Husky", "Golden retriever", "German Shepard")));
        // Add more offices and candidates as needed
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

        // Convert the results object to a JSON string
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(ballotResult.getResults());

        // Save the JSON string to a file
        try (FileWriter file = new FileWriter("votingResults.json")) {
            file.write(json);
            System.out.println("Results saved to votingResults.json");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the results to a file.");
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
