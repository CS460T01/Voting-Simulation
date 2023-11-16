package Ballot;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class PositionResult {
    private List<String> candidates = new ArrayList<>();
    private String voterChoice;

    // Add candidate
    public void addCandidate(String candidate) {
        candidates.add(candidate);
    }

    // Getter and Setter for candidates and voterChoice
    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
    }

    public String getVoterChoice() {
        return voterChoice;
    }

    public void setVoterChoice(String voterChoice) {
        this.voterChoice = voterChoice;
    }
}
