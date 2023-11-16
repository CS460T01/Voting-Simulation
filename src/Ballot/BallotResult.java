package Ballot;

import java.util.LinkedHashMap;
import java.util.Map;

public class BallotResult {
    private Map<String, PositionResult> results;

    public BallotResult() {
        results = new LinkedHashMap<>();
    }

    public void addResult(String position, PositionResult result) {
        results.put(position, result);
    }

    public Map<String, PositionResult> getResults() {
        return results;
    }
}

