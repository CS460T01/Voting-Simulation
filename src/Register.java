import java.util.*;

public class Register {
    private Map<String, Voter> registeredVoters = new HashMap<>();
    private int voterIDCounter = 0;

    public String registerVoter(String name) {
        String voterID = String.format("%05d", voterIDCounter++);
        Voter voter = new Voter(voterID, name);
        registeredVoters.put(voterID, voter);
        return voterID;
    }

    public Voter getVoter(String voterID) {
        return registeredVoters.get(voterID);
    }

    public void saveToFile() {
        // save registeredVoters to a file
    }

    public void loadFromFile() {
        //load registeredVoters from a file
    }
}
