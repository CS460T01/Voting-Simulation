import java.io.Serializable;

public class Voter implements Serializable {
    private final String voterID;
    private final String name;
    private boolean hasVoted;

    public Voter(String voterID, String name) {
        this.voterID = voterID;
        this.name = name;
        this.hasVoted = false;
    }

    public String getVoterID() {
        return voterID;
    }

    public String getName() {
        return name;
    }

    public boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
