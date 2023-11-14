import java.io.Serializable;

public class Voter implements Serializable {
    private final String voterID;
    private final String name;
    private final String ssn;
    private boolean hasVoted;

    public Voter(String voterID, String name, String ssn) {
        this.voterID = voterID;
        this.name = name;
        this.ssn = ssn;
        this.hasVoted = false;
    }

    public String getVoterID() {
        return voterID;
    }

    public String getName() {
        return name;
    }

    public String getSSN() {
        return ssn;
    }

    public void markAsVoted() {
        setHasVoted(true);
    }


    public boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
