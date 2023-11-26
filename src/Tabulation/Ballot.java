package Tabulation;

import java.util.Map;

public class Ballot {
    private Map<String, Position> positions;

    public Map<String, Position> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, Position> positions) {
        this.positions = positions;
    }

    public static class Position {
        private String[] candidates;
        private String voterChoice;

        public String[] getCandidates() {
            return candidates;
        }

        public void setCandidates(String[] candidates) {
            this.candidates = candidates;
        }

        public String getVoterChoice() {
            return voterChoice;
        }

        public void setVoterChoice(String voterChoice) {
            this.voterChoice = voterChoice;
        }
    }
}
