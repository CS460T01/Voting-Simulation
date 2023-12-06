package Ballot;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public String toString() {
        return "BallotResult{" +
                "results=" + results.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue().toString())
                .collect(Collectors.joining(", ", "{", "}")) +
                '}';
    }

    public void setResults(Map<String, PositionResult> results) {
        this.results = results;
    }
}

