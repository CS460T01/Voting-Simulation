package Registration;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Register implements Serializable {
    private Map<String, Voter> registeredVoters = new HashMap<>();
    private static final String DATA_FILE = "data/registered_voters.dat";

    public Register() {
        loadFromFile();
    }

    public void registerVoter(Voter newVoter) {
        String uniqueID = newVoter.getVoterID();
        if (!registeredVoters.containsKey(uniqueID)) {
            registeredVoters.put(uniqueID, newVoter);
            saveToFile();
        } else {
            System.out.println("This voter is already registered.");
        }
    }

    public Voter findVoter(String firstName, String lastName, String ssnLast4, String address, LocalDate dob) {
        for (Voter voter : registeredVoters.values()) {
            if (voter.matches(firstName + " " + lastName, address, dob, ssnLast4)) {
                return voter;
            }
        }
        return null;
    }

    public boolean checkInVoter(String firstName, String lastName, String ssnLast4, String address, LocalDate dob) {
        Voter voter = findVoter(firstName, lastName, ssnLast4, address, dob);
        if (voter == null) {
            System.out.println("Voter not found.");
            return false;
        } else if (voter.hasVoted()) {
            System.out.println("Voter has already voted.");
            return false;
        } else {
            voter.markAsVoted();
            saveToFile();
            return true;
        }
    }


    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(registeredVoters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegistered(String voterID) {
        return registeredVoters.containsKey(voterID);
    }

    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                registeredVoters = (Map<String, Voter>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void printVoters() {
        for (Voter voter : registeredVoters.values()) {
            System.out.println(voter.getFullName() + " " + voter.getAddress() + " " + voter.getDateOfBirth() + " " + voter.getSsnLastFour() + " " + voter.hasVoted() + " " + voter.getVoterID());
        }
    }
}
