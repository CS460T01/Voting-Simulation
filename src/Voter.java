import java.io.Serializable;
import java.time.LocalDate;

public class Voter implements Serializable {
    private final String voterID;
    private final String fullName;
    private final String address;
    private final LocalDate dateOfBirth;
    private final String ssnLastFour;
    private boolean hasVoted;

    public Voter(String fullName, String address, LocalDate dateOfBirth, String ssnLastFour) {
        this.fullName = fullName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.ssnLastFour = ssnLastFour;
        this.voterID = fullName + dateOfBirth + ssnLastFour;
        this.hasVoted = false;
    }

    public String getVoterID() {
        return voterID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getSsnLastFour() {
        return ssnLastFour;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public void markAsVoted() {
        this.hasVoted = true;
    }

    public boolean matches(String fullName, String address, LocalDate dob, String ssnLastFour) {
        return this.fullName.equalsIgnoreCase(fullName) &&
                this.address.equalsIgnoreCase(address) &&
                this.dateOfBirth.equals(dob) &&
                this.ssnLastFour.equals(ssnLastFour);
    }
}
