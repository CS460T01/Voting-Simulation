package Ballot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.annotations.SerializedName;

public class PositionResult {

    @SerializedName("candidates")
    private List<String> candidates = new ArrayList<>();

    @SerializedName("voterChoice")
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

    @Override
    public String toString() {
        return "PositionResult{" +
                "candidates=" + candidates.stream().collect(Collectors.joining(", ", "[", "]")) +
                ", voterChoice='" + voterChoice + '\'' +
                '}';
    }
}
